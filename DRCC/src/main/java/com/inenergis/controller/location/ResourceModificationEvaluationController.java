package com.inenergis.controller.location;

import com.inenergis.commonServices.ProgramServiceContract;
import com.inenergis.entity.Lse;
import com.inenergis.entity.ProductType;
import com.inenergis.entity.SubLap;
import com.inenergis.entity.genericEnum.ElectricalUnit;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.locationRegistration.LocationChangelog;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.locationRegistration.PmaxPmin;
import com.inenergis.entity.locationRegistration.RegistrationReview;
import com.inenergis.entity.marketIntegration.Iso;
import com.inenergis.entity.program.Program;
import com.inenergis.service.IsoResourceService;
import com.inenergis.service.IsoService;
import com.inenergis.service.LocationChangelogService;
import com.inenergis.service.LocationSubmissionStatusService;
import com.inenergis.service.LseService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.service.SubLapService;
import com.inenergis.util.ConstantsProviderModel;
import com.inenergis.util.EnergyUtil;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.primefaces.event.DragDropEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus.ASSIGNED_TO_RESOURCE;
import static com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus.INACTIVE;
import static com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus.INACTIVE_INFORMED_TO_ISO;

@Named
@ViewScoped
@Getter
@Setter
public class ResourceModificationEvaluationController implements Serializable {

    @Inject
    SubLapService subLapService;

    @Inject
    LseService lseService;

    @Inject
    IsoResourceService isoResourceService;

    @Inject
    ProgramServiceContract programService;

    @Inject
    LocationSubmissionStatusService locationSubmissionStatusService;

    @Inject
    LocationChangelogService locationChangelogService;

    @Inject
    IsoService isoService;

    @Inject
    UIMessage uiMessage;

    private static final int RESOURCE_NAME_AUTOCOMPLETE_MAX_RESULTS = 10;

    Logger log = LoggerFactory.getLogger(ResourceModificationEvaluationController.class);

    private SubLap subLap;
    private Lse lse;
    private ProductType productType = ProductType.RDRR;
    private IsoResource selectedResource;

    private List<SubLap> subLapList;
    private List<Lse> lseList;
    private List<Program> programList;
    private List<LocationChangelog> availableLocationList = new ArrayList<>();

    private List<String> selectedProgramList = new ArrayList<>();
    private Map<IsoResource, List<LocationChangelog>> selectedLocations = new HashMap<>();

    boolean render = false;

    boolean renderFilters = true;
    boolean renderAvailableLocations = true;
    boolean renderSavePanel = false;
    boolean renderReviewPanel = true;

    Date effectiveDate;
    private Iso iso;

    @PostConstruct
    public void init() {
        subLapList = subLapService.getAll();
        lseList = lseService.getAll();
        programList = programService.getPrograms();
        selectedProgramList = programList.stream().map(Program::getName).collect(Collectors.toList());
        iso = ParameterEncoderService.retrieveIsoFromParameter(isoService, uiMessage);
    }

    public void calculateRecommendation() {
        List<LocationChangelog> assignedChangelogList = new ArrayList<>();

        for (LocationChangelog changelog : availableLocationList) {
            IsoResource minCapacityIsoResource = null;

            for (IsoResource isoResource : selectedLocations.keySet()) {
                PmaxPmin pmaxPmin = isoResource.getActivePmaxPmin();
                long pmax = EnergyUtil.convertToWatts(pmaxPmin.getPmax().longValue(), ElectricalUnit.MW);
                long resourceCapacity = calculateResourceCapacity(isoResource);
                long locationCapacity = changelog.getLocation().getCalculatedCapacity();
                long sum = resourceCapacity + locationCapacity;
                if (sum > pmax) {
                    continue;
                }

                if (minCapacityIsoResource == null || resourceCapacity < calculateResourceCapacity(minCapacityIsoResource)) {
                    minCapacityIsoResource = isoResource;
                }
            }

            if (minCapacityIsoResource != null) {
                changelog.setType(LocationChangelog.LocationChangelogType.ADDED);
                changelog.setRecommended(true);
                changelog.setModified(true);
                changelog.setIsoResource(minCapacityIsoResource);
                selectedLocations.get(minCapacityIsoResource).add(changelog);
                assignedChangelogList.add(changelog);
            }
        }

        availableLocationList.removeAll(assignedChangelogList);
    }

    public void search() {
        availableLocationList.clear();
        selectedLocations.clear();
        // Available Location
        List<LocationSubmissionStatus> locations = locationSubmissionStatusService.searchBy(subLap, lse, selectedProgramList,
                Arrays.asList(INACTIVE.getText(), INACTIVE_INFORMED_TO_ISO.getText(), ASSIGNED_TO_RESOURCE.getText()),iso);
        for (LocationSubmissionStatus location : locations) {
            if (!location.getMeterDataRecheck() && location.getProgramServiceAgreementEnrollment().isActivelyEnrolled()) {
                LocationChangelog changelog = new LocationChangelog();
                changelog.setLocation(location);
                availableLocationList.add(changelog);
            }
        }

        // Submitted Locations
        for (IsoResource isoResource : isoResourceService.searchBy(subLap, lse, selectedResource, iso)) {
            List<LocationChangelog> changelogList = new ArrayList<>();
            if (isoResource.getActiveRegistration() != null) {
                for (LocationSubmissionStatus location : isoResource.getActiveRegistration().getLocations()) {
                    if (!location.getMeterDataRecheck()) {
                        LocationChangelog changelog = new LocationChangelog();
                        changelog.setIsoResource(isoResource);
                        changelog.setLocation(location);
                        changelogList.add(changelog);
                    }
                }
            }
            selectedLocations.put(isoResource, changelogList);

            for (LocationChangelog log : changelogList) {
                availableLocationList = availableLocationList.stream()
                        .filter(map -> !map.getLocation().getId().equals(log.getLocation().getId()))
                        .collect(Collectors.toList());
            }
        }

        // Changed Locations
        List<LocationChangelog> changelogList = locationChangelogService.geAllUnProcessed(iso);
        changelogList.stream()
                .filter(map -> map.getLinkedChange() != null)
                .map(LocationChangelog::getLinkedChange)
                .forEach(log -> log.setDependant(true));

        for (LocationChangelog changelog : changelogList) {
            switch (changelog.getType()) {
                case ADDED:
                    availableLocationList.removeIf(map -> map.getLocation().getId().equals(changelog.getLocation().getId()));
                    break;
                case REMOVED:
                case UNENROLLED:
                    if (selectedLocations.containsKey(changelog.getIsoResource())) {
                        selectedLocations.get(changelog.getIsoResource())
                                .removeIf(map -> map.getLocation().getId().equals(changelog.getLocation().getId()));
                    }
                    break;
            }

            selectedLocations.getOrDefault(changelog.getIsoResource(), new ArrayList<>()).add(changelog);
        }

        render = true;
    }

    public void clear() {
        productType = ProductType.RDRR;
        lse = null;
        subLap = null;
        selectedResource = null;
        selectedProgramList = programList.stream().map(Program::getName).collect(Collectors.toList());
        selectedLocations.clear();
        availableLocationList.clear();

        render = false;
    }

    public void onLocationDrop(DragDropEvent ddEvent) {
        LocationChangelog changelog = ((LocationChangelog) ddEvent.getData());

        availableLocationList.remove(changelog);

//        final String clientId = ((Droppable) ddEvent.getSource()).getClientId();
        final String clientId = ddEvent.getDropId();

        if ((StringUtils.isEmpty(clientId))) {
            return;
        }
        String resourceId = clientId.replaceAll("[^0-9]", "");
        if (!NumberUtils.isNumber(resourceId)) {
            return;
        }
        IsoResource isoResource = isoResourceService.getById(Long.parseLong(resourceId));

        changelog.setModified(true);

        switch (changelog.getType()) {
            case MOVING:
            case REMOVED:
                if (isoResource.equals(changelog.getIsoResource())) {
                    changelog.setType(LocationChangelog.LocationChangelogType.NONE);
                } else {
                    LocationChangelog linkedChangelog = new LocationChangelog();
                    linkedChangelog.setLocation(changelog.getLocation());
                    linkedChangelog.setIsoResource(changelog.getIsoResource());
                    linkedChangelog.setType(LocationChangelog.LocationChangelogType.REMOVED);
                    linkedChangelog.setDependant(true);
                    linkedChangelog.setModified(true);

                    selectedLocations.get(changelog.getIsoResource()).remove(changelog);
                    selectedLocations.get(changelog.getIsoResource()).add(linkedChangelog);

                    changelog.setType(LocationChangelog.LocationChangelogType.ADDED);
                    changelog.setLinkedChange(linkedChangelog);
                }
                break;
            default:
                changelog.setType(LocationChangelog.LocationChangelogType.ADDED);
        }

        changelog.setIsoResource(isoResource);

        List<LocationChangelog> locations = selectedLocations.getOrDefault(isoResource, new ArrayList<>());
        if (!locations.contains(changelog)) {
            locations.add(changelog);
        }
        selectedLocations.put(isoResource, locations);
    }

    public void onDelete(IsoResource isoResource, LocationChangelog changelog) {
        changelog.toggleModified();

        switch (changelog.getType()) {
            case ADDED:
                if (changelog.hasLinkedChange()) {
                    changelog.setHidden(true);
                    changelog.getLinkedChange().setType(LocationChangelog.LocationChangelogType.NONE);
                    changelog.getLinkedChange().setModified(true);
                }
                changelog.setRecommended(false);
                availableLocationList.add(changelog);

                selectedLocations.get(isoResource).remove(changelog);
                if (changelog.getLinkedChange() != null) {
                    changelog.getLinkedChange().setDependant(false);
                }
                break;
            case REMOVED:
                availableLocationList.remove(changelog);
                changelog.setType(LocationChangelog.LocationChangelogType.NONE);
                changelog.setIsoResource(isoResource);
                break;
            case NONE:
                changelog.setType(LocationChangelog.LocationChangelogType.REMOVED);
                break;
            case MOVING:
                availableLocationList.remove(changelog);
                changelog.setType(LocationChangelog.LocationChangelogType.NONE);
                break;
        }
    }

    public void onMove(LocationChangelog changelog) {
        availableLocationList.add(changelog);
        changelog.setHighlighted(true);
        changelog.setType(LocationChangelog.LocationChangelogType.MOVING);
    }

    public List<IsoResource> completeResource(String query) {
        return isoResourceService.findWithNameLike(query, RESOURCE_NAME_AUTOCOMPLETE_MAX_RESULTS);
    }

    public void updateSearchForm(AjaxBehaviorEvent event) {
        IsoResource isoResource = (IsoResource) event.getComponent().getAttributes().get("value");

        lse = lseService.getByCode(isoResource.getIsoLse());
        subLap = subLapService.getByCode(isoResource.getIsoSublap ());
    }

    public void onReview() {
        for (IsoResource isoResource : selectedLocations.keySet()) {
            PmaxPmin pmaxPmin = isoResource.getActivePmaxPmin();
            long newCapacity = calculateResourceCapacity(isoResource);
            long pmin = EnergyUtil.convertToWatts(pmaxPmin.getPmin().longValue(), ElectricalUnit.MW);
            long pmax = EnergyUtil.convertToWatts(pmaxPmin.getPmax().longValue(), ElectricalUnit.MW);
            long resourceMinCapacity = EnergyUtil.convertToWatts(isoResource.getIsoProduct().getResourceMinCapacity(), isoResource.getIsoProduct().getResourceMinCapacityUnit());

            if (newCapacity < pmin) {
                uiMessage.addMessage("{0} resource new capacity is under Pmin", FacesMessage.SEVERITY_ERROR, isoResource.getName());
                return;
            }

            if (newCapacity > pmax) {
                uiMessage.addMessage("{0} resource new capacity is above Pmax", FacesMessage.SEVERITY_ERROR, isoResource.getName());
                return;
            }

            if (newCapacity < resourceMinCapacity) {
                uiMessage.addMessage("{0} resource new capacity is under Resource Min Capacity", FacesMessage.SEVERITY_ERROR, isoResource.getName());
                return;
            }
        }

        renderFilters = !renderFilters;
        renderAvailableLocations = !renderAvailableLocations;
        renderSavePanel = !renderSavePanel;
        renderReviewPanel = !renderReviewPanel;
        effectiveDate = null;
    }

    @Transactional
    public void save() {
        for (IsoResource isoResource : selectedLocations.keySet()) {
            for (LocationChangelog changelog : selectedLocations.get(isoResource)) {
                if (!changelog.isModified()) {
                    continue;
                }

                if (!isoResource.equals(changelog.getIsoResource())) {
                    locationChangelogService.delete(changelog);
                    changelog.setModified(false);

                    if (changelog.getType().equals(LocationChangelog.LocationChangelogType.REMOVED)) {
                        continue;
                    }
                }

                switch (changelog.getType()) {
                    case NONE:
                        if (changelog.isModified()) {
                            locationChangelogService.delete(changelog);
                        }
                        break;
                    case REMOVED:
                        availableLocationList.remove(changelog);
                        changelog.setEffectiveDate(effectiveDate);
                        locationChangelogService.save(changelog);
                        break;
                    default:
                        changelog.setEffectiveDate(effectiveDate);
                        locationChangelogService.save(changelog);
                }

                changelog.setModified(false);
            }
        }

        for (LocationChangelog changelog : availableLocationList) {
            if (!changelog.isModified()) {
                continue;
            }

            locationChangelogService.delete(changelog);
            changelog.setModified(false);
        }

        onReview();
    }

    public BigDecimal getTotalResourceCapacity(String resourceId) {
        IsoResource isoResource = isoResourceService.getById(Long.parseLong(resourceId));
        return new BigDecimal(calculateResourceCapacity(isoResource)).divide(new BigDecimal(ConstantsProviderModel.KW_PRECISION));
    }

    private long calculateResourceCapacity(IsoResource isoResource) {
        return selectedLocations.get(isoResource).stream()
                .filter(changelog -> !changelog.getType().equals(LocationChangelog.LocationChangelogType.REMOVED))
                .mapToLong(changelog -> changelog.getLocation().getCalculatedCapacity())
                .sum();
    }
}