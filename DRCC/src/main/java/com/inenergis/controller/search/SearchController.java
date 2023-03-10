package com.inenergis.controller.search;

import com.inenergis.controller.authentication.AuthorizationChecker;
import com.inenergis.controller.customerData.AgreementPointMapList;
import com.inenergis.controller.lazyDataModel.LazyElasticDataModel;
import com.inenergis.model.ElasticAggregator;
import com.inenergis.model.ElasticAgreementPointMap;
import com.inenergis.model.ElasticAsset;
import com.inenergis.model.ElasticContract;
import com.inenergis.model.ElasticContractEntity;
import com.inenergis.model.ElasticDevice;
import com.inenergis.model.ElasticEvent;
import com.inenergis.model.ElasticEventNotification;
import com.inenergis.model.ElasticISO;
import com.inenergis.model.ElasticInvoice;
import com.inenergis.model.ElasticLocation;
import com.inenergis.model.ElasticProgram;
import com.inenergis.model.ElasticRegistration;
import com.inenergis.model.ElasticResource;
import com.inenergis.model.SearchMatch;
import com.inenergis.model.SearchSuggestion;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.service.SearchKeywordsService;
import com.inenergis.service.aws.ElasticUtilsService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.inenergis.service.aws.ElasticUtilsService.AGGREGATOR;
import static com.inenergis.service.aws.ElasticUtilsService.APM;
import static com.inenergis.service.aws.ElasticUtilsService.ASSET;
import static com.inenergis.service.aws.ElasticUtilsService.CONTRACT;
import static com.inenergis.service.aws.ElasticUtilsService.CONTRACT_ENTITY;
import static com.inenergis.service.aws.ElasticUtilsService.DEVICE;
import static com.inenergis.service.aws.ElasticUtilsService.EVENT;
import static com.inenergis.service.aws.ElasticUtilsService.EVENT_NOTIFICATION;
import static com.inenergis.service.aws.ElasticUtilsService.INVOICE;
import static com.inenergis.service.aws.ElasticUtilsService.ISO;
import static com.inenergis.service.aws.ElasticUtilsService.LOCATION;
import static com.inenergis.service.aws.ElasticUtilsService.PROGRAM;
import static com.inenergis.service.aws.ElasticUtilsService.REGISTRATION;
import static com.inenergis.service.aws.ElasticUtilsService.RESOURCE;
import static com.inenergis.util.MapUtils.calculateZoom;

@Named
@ViewScoped
@Getter
@Setter
public class SearchController implements Serializable {

    public static final String ENERGY_ARRAY = "energy_array";

    @Inject
    UIMessage uiMessage;

    @Inject
    ElasticUtilsService elasticUtilsService;
    @Inject
    SearchKeywordsService searchKeywordsService;
    @Inject
    AuthorizationChecker authorizationChecker;

    Logger log = LoggerFactory.getLogger(SearchController.class);

    @SessionScoped
    private String querySearch;

    //    @SessionScoped
    private LazyElasticDataModel elasticAgreementPointMaps;
    private LazyElasticDataModel elasticDevices;
    private LazyElasticDataModel elasticAssets;
    private LazyElasticDataModel elasticLocations;
    private LazyElasticDataModel elasticResources;
    private LazyElasticDataModel elasticRegistrations;
    private LazyElasticDataModel elasticAggregators;
    private LazyElasticDataModel elasticPrograms;
    private LazyElasticDataModel elasticIsos;
    private LazyElasticDataModel elasticEventNotifications;
    private LazyElasticDataModel elasticEvents;
    private LazyElasticDataModel elasticContracts;
    private LazyElasticDataModel elasticContractEntities;
    private LazyElasticDataModel elasticInvoices;
    private List<SearchMatch> firstTenResults;

    private MapModel mapModel;
    private Marker marker;
    private LatLng mapCenter;
    private int zoom = 7;

    @PostConstruct
    public void init() {
        retrieveQuerySearch();
        if (isSearchContext()) {
            try {
                doElasticSearch();
                buildMap();
            } catch (IOException e) {
                log.error("Error loading Elastic search data", e);
            }
        }
    }

    private boolean isSearchContext() {
        return "/Search.xhtml".equals(FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath());
    }

    private void retrieveQuerySearch() {
        String paramQuery = ParameterEncoderService.getDecodedParameter("q");
        if (StringUtils.isNotEmpty(paramQuery)) {
            querySearch = paramQuery;
        }
    }

    public void search() throws IOException {
        if (!isSearchContext()) {
            if (searchKeywordsService.isQueryExactSuggestion(querySearch)) {
                return;
            }
            final String redirectString = (StringUtils.isEmpty(querySearch) ? "Search.xhtml" : "Search.xhtml?q=" + ParameterEncoderService.encode(querySearch));
            redirect(redirectString);
            FacesContext.getCurrentInstance().responseComplete();
        } else {
            doElasticSearch();
            buildMap();
        }
    }

    private void doElasticSearch() throws IOException {
        elasticAgreementPointMaps = new LazyElasticDataModel(elasticUtilsService, StringUtils.isEmpty(querySearch) ? StringUtils.EMPTY : querySearch, ENERGY_ARRAY, APM);
        elasticDevices = new LazyElasticDataModel(elasticUtilsService, StringUtils.isEmpty(querySearch) ? StringUtils.EMPTY : querySearch, ENERGY_ARRAY, DEVICE);
        elasticAssets = new LazyElasticDataModel(elasticUtilsService, StringUtils.isEmpty(querySearch) ? StringUtils.EMPTY : querySearch, ENERGY_ARRAY, ASSET);
        elasticLocations = new LazyElasticDataModel(elasticUtilsService, StringUtils.isEmpty(querySearch) ? StringUtils.EMPTY : querySearch, ENERGY_ARRAY, LOCATION);
        elasticResources = new LazyElasticDataModel(elasticUtilsService, StringUtils.isEmpty(querySearch) ? StringUtils.EMPTY : querySearch, ENERGY_ARRAY, RESOURCE);
        elasticRegistrations = new LazyElasticDataModel(elasticUtilsService, StringUtils.isEmpty(querySearch) ? StringUtils.EMPTY : querySearch, ENERGY_ARRAY, REGISTRATION);
        elasticAggregators = new LazyElasticDataModel(elasticUtilsService, StringUtils.isEmpty(querySearch) ? StringUtils.EMPTY : querySearch, ENERGY_ARRAY, AGGREGATOR);
        elasticPrograms = new LazyElasticDataModel(elasticUtilsService, StringUtils.isEmpty(querySearch) ? StringUtils.EMPTY : querySearch, ENERGY_ARRAY, PROGRAM);
        elasticIsos = new LazyElasticDataModel(elasticUtilsService, StringUtils.isEmpty(querySearch) ? StringUtils.EMPTY : querySearch, ENERGY_ARRAY, ISO);
        elasticEventNotifications = new LazyElasticDataModel(elasticUtilsService, StringUtils.isEmpty(querySearch) ? StringUtils.EMPTY : querySearch, ENERGY_ARRAY, EVENT_NOTIFICATION);
        elasticEvents = new LazyElasticDataModel(elasticUtilsService, StringUtils.isEmpty(querySearch) ? StringUtils.EMPTY : querySearch, ENERGY_ARRAY, EVENT);
        elasticContracts = new LazyElasticDataModel(elasticUtilsService, StringUtils.isEmpty(querySearch) ? StringUtils.EMPTY : querySearch, ENERGY_ARRAY, CONTRACT);
        elasticContractEntities = new LazyElasticDataModel(elasticUtilsService, StringUtils.isEmpty(querySearch) ? StringUtils.EMPTY : querySearch, ENERGY_ARRAY, CONTRACT_ENTITY);
        elasticInvoices = new LazyElasticDataModel(elasticUtilsService, StringUtils.isEmpty(querySearch) ? StringUtils.EMPTY : querySearch, ENERGY_ARRAY, INVOICE);
        firstTenResults = doSearch(StringUtils.isEmpty(querySearch) ? StringUtils.EMPTY : querySearch);
    }

    public List<SearchMatch> doSearch(String query) throws IOException {
        if (StringUtils.isEmpty(query)) {
            return elasticUtilsService.partialSearch(query, new String[]{ENERGY_ARRAY}, new String[]{APM});
        }
        String[] types = searchKeywordsService.getTypesForSearch(query);
        query = searchKeywordsService.stripOutReservedWords(query);
        return elasticUtilsService.partialSearch(query, new String[]{ENERGY_ARRAY}, types);
    }

    public List<SearchMatch> partialSearch(String query) throws IOException {
        List<SearchMatch> suggestions = searchKeywordsService.getSuggestions(query);
        suggestions = suggestions.stream().filter(s -> StringUtils.isEmpty(s.getPermission()) || authorizationChecker.authorized(s.getPermission())).collect(Collectors.toList());
        suggestions.addAll(doSearch(query));
        return suggestions.stream().limit(10).collect(Collectors.toList());
    }

    public String getGroup(SearchMatch match) {
        if (match instanceof ElasticAgreementPointMap) {
            return "<i class=\"material-icons\" style=\"font-size: 16px;\">person</i><span style=\"padding-left: 10px;font-size: 18px;\">Customers</span>";
        } else if (match instanceof ElasticAggregator) {
            return "<i class=\"material-icons\" style=\"font-size: 16px;\">settings_input_component</i><span style=\"padding-left: 10px;font-size: 18px;\">Aggregators</span>";
        } else if (match instanceof ElasticISO) {
            ElasticISO iso = (ElasticISO) match;
            return "<i class=\"material-icons\" style=\"font-size: 16px;\">account_balance</i><span style=\"padding-left: 10px;font-size: 18px;\">" + iso.getName() + "</span>";
        } else if (match instanceof ElasticAsset) {
            return "<i class=\"material-icons\" style=\"font-size: 16px;\">list</i><span style=\"padding-left: 10px;font-size: 18px;\">Catalog</span>";
        } else if (match instanceof ElasticLocation) {
            return "<i class=\"material-icons\" style=\"font-size: 16px;\">location_on</i><span style=\"padding-left: 10px;font-size: 18px;\">Location</span>";
        } else if (match instanceof ElasticResource) {
            return "<i class=\"material-icons\" style=\"font-size: 16px;\">location_city</i><span style=\"padding-left: 10px;font-size: 18px;\">Resource</span>";
        } else if (match instanceof ElasticRegistration) {
            return "<i class=\"material-icons\" style=\"font-size: 16px;\">image</i><span style=\"padding-left: 10px;font-size: 18px;\">Registration</span>";
        } else if (match instanceof ElasticDevice) {
            return "<i class=\"material-icons\" style=\"font-size: 16px;\">apps</i><span style=\"padding-left: 10px;font-size: 18px;\">Inventory</span>";
        } else if (match instanceof ElasticProgram) {
            ElasticProgram program = (ElasticProgram) match;
            return "<i class=\"material-icons\" style=\"font-size: 16px;\">lightbulb_outline</i><span style=\"padding-left: 10px;font-size: 18px;\">" + program.getName() + "</span>";
        } else if (match instanceof ElasticEventNotification) {
            return "<i class=\"material-icons\" style=\"font-size: 16px;\">publish</i><span style=\"padding-left: 10px;font-size: 18px;\">Event Notifications</span>";
        } else if (match instanceof ElasticEvent) {
            return "<i class=\"material-icons\" style=\"font-size: 16px;\">multiline_chart</i><span style=\"padding-left: 10px;font-size: 18px;\">Events</span>";
        } else if (match instanceof ElasticContract) {
            return "<i class=\"material-icons\" style=\"font-size: 16px;\">spellcheck</i><span style=\"padding-left: 10px;font-size: 18px;\">Contracts</span>";
        } else if (match instanceof ElasticContractEntity) {
            return "<i class=\"material-icons\" style=\"font-size: 16px;\">perm_contact_calendar</i><span style=\"padding-left: 10px;font-size: 18px;\">Contract Entities</span>";
        } else if (match instanceof ElasticInvoice) {
            return "<i class=\"material-icons\" style=\"font-size: 16px;\">attach_money</i><span style=\"padding-left: 10px;font-size: 18px;\">Invoices</span>";
        } else if (match instanceof SearchSuggestion) {
            SearchSuggestion suggestion = (SearchSuggestion) match;
            return "<i class=\"material-icons\" style=\"font-size: 16px;\">" + suggestion.getIconGroup() + "</i><span style=\"padding-left: 10px;font-size: 18px;\">" + suggestion.getGroup() + "</span>";
        }
        return "Others";
    }


    private void buildMap() throws IOException {
        if (CollectionUtils.isNotEmpty(firstTenResults)) {
            mapModel = new DefaultMapModel();

            BigDecimal sumLatitude = BigDecimal.ZERO;
            BigDecimal sumLongitude = BigDecimal.ZERO;

            double minLat = 190.0;
            double minLon = 190.0;
            double maxLat = -190.0;
            double maxLon = -190.0;

            for (SearchMatch match : firstTenResults) {

                if (match instanceof ElasticAgreementPointMap) {
                    ElasticAgreementPointMap apm = ((ElasticAgreementPointMap) match);
                    //Add marker to map
                    LatLng coord1 = new LatLng(new Double(apm.getLatitude().doubleValue()), new Double(apm.getLongitude().doubleValue()));
                    Marker overlay = new Marker(coord1, apm.getServiceAgreementId(), apm);
                    mapModel.addOverlay(overlay);


                    //Collect statistics to build the map center
                    sumLatitude = sumLatitude.add(apm.getLatitude());
                    sumLongitude = sumLongitude.add(apm.getLongitude());
                    if (apm.getLatitude().doubleValue() - minLat < 0) {
                        minLat = apm.getLatitude().doubleValue();
                    }
                    if (apm.getLatitude().doubleValue() - maxLat > 0) {
                        maxLat = apm.getLatitude().doubleValue();
                    }
                    if (apm.getLongitude().doubleValue() - minLon < 0) {
                        minLon = apm.getLongitude().doubleValue();
                    }
                    if (apm.getLongitude().doubleValue() - maxLon > 0) {
                        maxLon = apm.getLongitude().doubleValue();
                    }
                }

            }
            mapCenter = new LatLng((minLat + maxLat) / 2.0, (minLon + maxLon) / 2.0);
            zoom = calculateZoom(calculateMaxDistance(mapCenter, firstTenResults));
        }
    }

    private Double calculateMaxDistance(LatLng mapCenter, List<SearchMatch> tenFirstResults) {
        Double result = 0.0;
        for (SearchMatch match : tenFirstResults) {
            if (match instanceof ElasticAgreementPointMap) {
                ElasticAgreementPointMap apm = ((ElasticAgreementPointMap) match);
                LatLng coord = new LatLng(new Double(apm.getLatitude().doubleValue()), new Double(apm.getLongitude().doubleValue()));
                double sqrt = Math.sqrt(Math.pow(mapCenter.getLat() - coord.getLat(), 2) + Math.pow(mapCenter.getLng() - coord.getLng(), 2));
                result = Math.max(result, sqrt);
            }
        }
        return result;
    }

    public void onSelectSearchMatch(SelectEvent event) throws IOException {
        final SearchMatch match = (SearchMatch) event.getObject();
        if (match == null) {
            log.error("match null", event);
            return;
        }
        if (match instanceof ElasticAgreementPointMap) {
            ElasticAgreementPointMap apm = ((ElasticAgreementPointMap) match);
            if (StringUtils.isNotEmpty(apm.getServiceAgreementId())) {
                redirect("CustomerList.xhtml?o=" + ParameterEncoderService.encode(apm.getServiceAgreementId()));
            }
        } else if (match instanceof ElasticAggregator) {
            ElasticAggregator aggregator = ((ElasticAggregator) match);
            if (aggregator.getId() != null) {
                redirect("AggregatorList.xhtml?o=" + ParameterEncoderService.encode(aggregator.getId()));
            }
        } else if (match instanceof ElasticEvent) {
            ElasticEvent elasticEvent = ((ElasticEvent) match);
            if (elasticEvent.getId() != null) {
                redirect("EventDispatchDetail.xhtml?o=" + ParameterEncoderService.encode(elasticEvent.getId()));
            }
        } else if (match instanceof ElasticEventNotification) {
            ElasticEventNotification elasticEvent = ((ElasticEventNotification) match);
            if (elasticEvent.getId() != null) {
                redirect("EventList.xhtml?o=" + ParameterEncoderService.encode(elasticEvent.getId()));
            }
        } else if (match instanceof ElasticISO) {
            ElasticISO iso = ((ElasticISO) match);
            if (iso.getId() != null) {
                redirect(iso.getUrl() + "?o=" + ParameterEncoderService.encode(iso.getId()));
            }
        } else if (match instanceof ElasticContract) {
            ElasticContract contract = (ElasticContract) match;
            if (contract.getId() != null) {
                redirect("EnergyContractDetail.xhtml?o=" + ParameterEncoderService.encode(contract.getId()));
            }
        } else if (match instanceof ElasticContractEntity) {
            ElasticContractEntity contractEntity = (ElasticContractEntity) match;
            if (contractEntity.getId() != null) {
                redirect("ContractEntityDetail.xhtml?o=" + ParameterEncoderService.encode(contractEntity.getId()));
            }
        } else if (match instanceof ElasticAsset) {
            ElasticAsset elasticAsset = (ElasticAsset) match;
            if (elasticAsset.getId() != null) {
                redirect(elasticAsset.getAssetType() + "Details.xhtml?o=" + ParameterEncoderService.encode(elasticAsset.getId()));
            }
        } else if (match instanceof ElasticDevice) {
            ElasticDevice elasticDevice = (ElasticDevice) match;
            if (elasticDevice.getId() != null) {
                redirect(elasticDevice.getDeviceType() + "Details.xhtml?o=" + ParameterEncoderService.encode(elasticDevice.getId()));
            }
        } else if (match instanceof ElasticLocation) {
            ElasticLocation elasticLocation = (ElasticLocation) match;
            if (elasticLocation.getId() != null) {
                redirect("LocationSubmissionDetail.xhtml?o=" + ParameterEncoderService.encode(elasticLocation.getId()));
            }
        } else if (match instanceof ElasticResource) {
            ElasticResource elasticResource = (ElasticResource) match;
            if (elasticResource.getId() != null) {
                redirect("ResourceDetails.xhtml?o=" + ParameterEncoderService.encode(elasticResource.getId()));
            }
        } else if (match instanceof ElasticRegistration) {
            ElasticRegistration elasticRegistration = (ElasticRegistration) match;
            if (elasticRegistration.getId() != null) {
                redirect("ResourceRegistrationDetail.xhtml?o=" + ParameterEncoderService.encode(elasticRegistration.getId()));
            }
        } else if (match instanceof ElasticProgram) {
            ElasticProgram iso = ((ElasticProgram) match);
            if (iso.getId() != null) {
                redirect(iso.getUrl() + "?o=" + ParameterEncoderService.encode(iso.getId()));
            }
        } else if (match instanceof ElasticInvoice) {
            ElasticInvoice elasticInvoice = (ElasticInvoice) match;
            redirect("CustomerList.xhtml?o=" + ParameterEncoderService.encode(elasticInvoice.getServiceAgreement()) + "&" + AgreementPointMapList.INVOICE_TAB + "=1");
        } else if (match instanceof SearchSuggestion) {
            SearchSuggestion suggestion = ((SearchSuggestion) match);
            redirect(suggestion.getOutcome());
        }
    }

    public void onSelectSP(SelectEvent event) throws IOException {
        final ElasticAgreementPointMap apm = (ElasticAgreementPointMap) event.getObject();
        if (apm == null) {
            log.error("Apm null", event);
            return;
        }
        if (StringUtils.isNotEmpty(apm.getServicePointId())) {
            redirect("ServicePointList.xhtml?o=" + ParameterEncoderService.encode(apm.getServicePointId()));
        }
    }

    public void onSelectSA(SelectEvent event) throws IOException {
        final ElasticAgreementPointMap apm = (ElasticAgreementPointMap) event.getObject();
        if (apm == null) {
            log.error("Apm null", event);
            return;
        }
        if (StringUtils.isNotEmpty(apm.getServiceAgreementId())) {
            redirect("CustomerList.xhtml?o=" + ParameterEncoderService.encode(apm.getServiceAgreementId()));
        }
    }

    public void onSelectDevice(SelectEvent event) throws IOException {
        final ElasticDevice device = (ElasticDevice) event.getObject();
        redirect(event, device, device.getDeviceType() + "Details.xhtml?o=");
    }

    public void onSelectAsset(SelectEvent event) throws IOException {
        final ElasticAsset asset = (ElasticAsset) event.getObject();
        redirect(event, asset, asset.getAssetType() + "Details.xhtml?o=");
    }

    public void onSelectLocation(SelectEvent event) throws IOException {
        redirect(event, (SearchMatch) event.getObject(), "LocationSubmissionDetail.xhtml?o=");
    }

    public void onSelectResource(SelectEvent event) throws IOException {
        redirect(event, (SearchMatch) event.getObject(), "ResourceDetails.xhtml?o=");
    }

    public void onSelectRegistration(SelectEvent event) throws IOException {
        redirect(event, (SearchMatch) event.getObject(), "ResourceRegistrationDetail.xhtml?o=");
    }

    public void onSelectAggregator(SelectEvent event) throws IOException {
        redirect(event, (SearchMatch) event.getObject(), "AggregatorList.xhtml?o=");
    }

    public void onSelectProgram(SelectEvent event) throws IOException {
        redirect(event, (SearchMatch) event.getObject(), "ProgramManagement.xhtml?o=");
    }

    public void onSelectIso(SelectEvent event) throws IOException {
        redirect(event, (SearchMatch) event.getObject(), "IsoProfiles.xhtml?o=");
    }

    public void onSelectEventNotification(SelectEvent event) throws IOException {
        redirect(event, (SearchMatch) event.getObject(), "EventList.xhtml?o=");
    }

    public void onSelectEvent(SelectEvent event) throws IOException {
        redirect(event, (SearchMatch) event.getObject(), "EventDispatchDetail.xhtml?o=");
    }

    public void onSelectContract(SelectEvent event) throws IOException {
        redirect(event, (SearchMatch) event.getObject(), "EnergyContractDetail.xhtml?o=");
    }

    public void onSelectContractEntity(SelectEvent event) throws IOException {
        redirect(event, (SearchMatch) event.getObject(), "ContractEntityDetail.xhtml?o=");
    }

    public void onSelectInvoice(SelectEvent event) throws IOException {
        final ElasticInvoice elasticInvoice = (ElasticInvoice) event.getObject();
        if (elasticInvoice == null) {
            log.error("Invoice null", event);
            return;
        }
        if (elasticInvoice.getInvoiceNumber() != null) {
            redirect("CustomerList.xhtml?o=" + ParameterEncoderService.encode(elasticInvoice.getServiceAgreement()) + "&" + AgreementPointMapList.INVOICE_TAB + "=1");
        }
    }

    public void redirect(SelectEvent event, SearchMatch match, String url) throws IOException {
        if (match == null) {
            log.error("object for redirection is null", event);
            return;
        }
        if (match.getId() != null) {
            redirect(url + ParameterEncoderService.encode(match.getId()));
        }
    }

    public void redirect(String url) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        if (event.getOverlay() instanceof Marker) {
            marker = (Marker) event.getOverlay();
        } else {
            marker = null;
        }
    }
}