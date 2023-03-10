package com.inenergis.controller.location;

import com.inenergis.commonServices.ProgramServiceContract;
import com.inenergis.controller.lazyDataModel.LazyLocationSubmissionExceptionDataModel;
import com.inenergis.controller.lazyDataModel.LazyLocationSubmissionStatusDataModel;
import com.inenergis.entity.History;
import com.inenergis.entity.Lse;
import com.inenergis.entity.SubLap;
import com.inenergis.entity.locationRegistration.LocationSubmissionException;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.marketIntegration.Iso;
import com.inenergis.entity.program.Program;
import com.inenergis.service.HistoryService;
import com.inenergis.service.IsoService;
import com.inenergis.service.LocationSubmissionStatusService;
import com.inenergis.service.LseService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.service.SubLapService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.picketlink.Identity;
import org.picketlink.idm.model.basic.User;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Transient;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus.DISPUTED;
import static com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus.DUPLICATED;
import static com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus.NON_REGISTRABLE;
import static com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus.NON_REGISTRABLE_CANCELED;
import static com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus.PENDING_APRPOVAL;
import static com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus.PENDING_REVIEW;


@Named
@ViewScoped
@Getter
@Setter
public class LocationSubmissionController implements Serializable {

    @Inject
    EntityManager entityManager;
    @Inject
    UIMessage uiMessage;
    @Inject
    @Transient
    LocationSubmissionStatusService service;

    @Inject
    SubLapService subLapService;

    @Inject
    LseService lseService;

    @Inject
    ProgramServiceContract programService;

    @Inject
    HistoryService historyService;

    @Inject
    IsoService isoService;

    @Inject
    Identity identity;

    Logger log = LoggerFactory.getLogger(LocationSubmissionController.class);

    LazyLocationSubmissionExceptionDataModel lazyLocationSubmissionExceptionDataModel;
    LazyLocationSubmissionStatusDataModel lazyLocationSubmissionStatusDataModelPending;
    LazyLocationSubmissionStatusDataModel lazyLocationSubmissionStatusDataModelInactive;
    LazyLocationSubmissionStatusDataModel lazyLocationSubmissionStatusDataModelActive;
    LazyLocationSubmissionStatusDataModel lazyLocationSubmissionStatusDataModelNonReg;
    LazyLocationSubmissionStatusDataModel lazyLocationSubmissionStatusDataModelDisputed;
    Map<String, Object> exceptionsPreFilter;
    Map<String, Object> statusPreFilter;

    //fields for filters
    private String programServiceAgreementId;
    private Program program;
    private String serviceAddress;
    private String serviceCity;
    private SubLap subLap;
    private Lse lse;
    private String uuid;
    private String zip;
    private String location;
    private Iso iso;
    boolean showChangeSubLapDialog = false;
    private SubLap newSublap;
    private List<SubLap> subLapList;
    private List<Lse> lseList;
    private List<Program> programList;

    boolean render = false;

    static final int PENDING = 0;
    static final int INACTIVE = 1;
    static final int ACTIVE_TAB = 2;
    static final int EXCEPTIONS = 3;
    static final int DISPUTED_TAB = 4;
    static final int NON_REGISTRABLE_TAB = 5;

    int tabIndex = PENDING;

    private String nonRegistrableReason;
    private boolean showReason = false;
    private LocationSubmissionStatus currentLocation = null;
    private LocationSubmissionException currentException = null;

    @PostConstruct
    public void init() {
        subLapList = subLapService.getAll();
        lseList = lseService.getAll();
        programList = programService.getPrograms();
        iso = ParameterEncoderService.retrieveIsoFromParameter(isoService, uiMessage);
    }

    public void onSelectStatus(SelectEvent event) throws IOException {
        LocationSubmissionStatus locationSubmissionStatus = (LocationSubmissionStatus) event.getObject();
        FacesContext.getCurrentInstance().getExternalContext().redirect("LocationSubmissionDetail.xhtml?o=" + ParameterEncoderService.encode(locationSubmissionStatus.getId()));
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void onSelectException(SelectEvent event) throws IOException {
        LocationSubmissionException exception = (LocationSubmissionException) event.getObject();
        if (exception.getLocationSubmissionStatus() != null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("LocationSubmissionDetail.xhtml?o=" + ParameterEncoderService.encode(exception.getLocationSubmissionStatus().getId()));
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    private Map<String, Object> generateExceptionsPreFilter() {
        Map<String, Object> preFilter = new HashMap<>();
        preFilter.put("resolved", Boolean.FALSE);
        if (StringUtils.isNotBlank(programServiceAgreementId)) {
            preFilter.put("locationSubmissionStatus.programServiceAgreementEnrollment.serviceAgreement.serviceAgreementId", programServiceAgreementId);
        }
        if (program != null) {
            preFilter.put("locationSubmissionStatus.programServiceAgreementEnrollment.program.name", program.getName());
        }
        if (StringUtils.isNotBlank(serviceAddress)) {
            preFilter.put("locationSubmissionStatus.programServiceAgreementEnrollment.serviceAgreement.agreementPointMaps.servicePoint.premise.serviceAddress1", serviceAddress);
        }
        if (StringUtils.isNotBlank(serviceCity)) {
            preFilter.put("locationSubmissionStatus.programServiceAgreementEnrollment.serviceAgreement.agreementPointMaps.servicePoint.premise.serviceCityUpr", serviceCity);
        }
        if (subLap != null) {
            preFilter.put("locationSubmissionStatus.isoSublap", subLap.getCode());
        }
        if (StringUtils.isNotBlank(uuid)) {
            preFilter.put("locationSubmissionStatus.programServiceAgreementEnrollment.serviceAgreement.saUuid", uuid);
        }
        if (lse != null) {
            preFilter.put("locationSubmissionStatus.isoLse", lse.getCode());
        }
        if (StringUtils.isNotBlank(location)) {
            preFilter.put("locationSubmissionStatus.isoResourceId", location);
        }
        if (StringUtils.isNotBlank(zip)) {
            preFilter.put("locationSubmissionStatus.programServiceAgreementEnrollment.serviceAgreement.agreementPointMaps.servicePoint.premise.servicePostal", zip);
        }
        if (iso != null) {
            preFilter.put("locationSubmissionStatus.iso.id", iso.getId());
        }
        return preFilter;
    }

    private Map<String, Object> generateStatusPrefFilter(List<String> status) {
        Map<String, Object> preFilter = new HashMap<>();
        preFilter.put("status", status);

        if (StringUtils.isNotBlank(programServiceAgreementId)) {
            preFilter.put("programServiceAgreementEnrollment.serviceAgreement.serviceAgreementId", programServiceAgreementId);
        }
        if (program != null) {
            preFilter.put("programServiceAgreementEnrollment.program.name", program.getName());
        }
        if (StringUtils.isNotBlank(serviceAddress)) {
            preFilter.put("programServiceAgreementEnrollment.serviceAgreement.agreementPointMaps.servicePoint.premise.serviceAddress1", serviceAddress);
        }
        if (StringUtils.isNotBlank(serviceCity)) {
            preFilter.put("programServiceAgreementEnrollment.serviceAgreement.agreementPointMaps.servicePoint.premise.serviceCityUpr", serviceCity);
        }
        if (subLap != null) {
            preFilter.put("isoSublap", subLap.getCode());
        }
        if (StringUtils.isNotBlank(uuid)) {
            preFilter.put("programServiceAgreementEnrollment.serviceAgreement.saUuid", uuid);
        }
        if (lse != null) {
            preFilter.put("isoLse", lse.getCode());
        }
        if (StringUtils.isNotBlank(location)) {
            preFilter.put("isoResourceId", location);
        }
        if (StringUtils.isNotBlank(zip)) {
            preFilter.put("programServiceAgreementEnrollment.serviceAgreement.agreementPointMaps.servicePoint.premise.servicePostal", zip);
        }
        if (iso != null) {
            preFilter.put("iso.id", iso.getId());
        }
        return preFilter;
    }


    public void search() {
        String status;
        if (!render) { //first time
            render = true;
        }
        populateActiveLocations();
        populateInactiveLocations();
        populatePendingLocations();
        populateNonRegistrableLocations();
        populateLocationExceptions();
        populateDisputedLocations();
    }

    private void populateLocationExceptions() {
        exceptionsPreFilter = generateExceptionsPreFilter();
        lazyLocationSubmissionExceptionDataModel = new LazyLocationSubmissionExceptionDataModel(entityManager, exceptionsPreFilter);
    }

    private void populateNonRegistrableLocations() {
        List<String> statuses = Arrays.asList(NON_REGISTRABLE.getText(), NON_REGISTRABLE_CANCELED.getText());
        statusPreFilter = generateStatusPrefFilter(statuses);
        lazyLocationSubmissionStatusDataModelNonReg = new LazyLocationSubmissionStatusDataModel(entityManager, statusPreFilter);
    }

    private void populatePendingLocations() {
        List<String> statuses = Arrays.asList(PENDING_APRPOVAL.getText(), PENDING_REVIEW.getText());
        statusPreFilter = generateStatusPrefFilter(statuses);
        lazyLocationSubmissionStatusDataModelPending = new LazyLocationSubmissionStatusDataModel(entityManager, statusPreFilter);
    }

    private void populateDisputedLocations() {
        List<String> statuses = Arrays.asList(DISPUTED.getText(), DUPLICATED.getText());
        statusPreFilter = generateStatusPrefFilter(statuses);
        lazyLocationSubmissionStatusDataModelDisputed = new LazyLocationSubmissionStatusDataModel(entityManager, statusPreFilter);
    }

    private void populateInactiveLocations() {
        String status;
        status = LocationSubmissionStatus.LocationStatus.INACTIVE.getText();
        statusPreFilter = generateStatusPrefFilter(Arrays.asList(status));
        lazyLocationSubmissionStatusDataModelInactive = new LazyLocationSubmissionStatusDataModel(entityManager, statusPreFilter);
    }

    private void populateActiveLocations() {
        String status;
        status = LocationSubmissionStatus.LocationStatus.ASSIGNED_TO_RESOURCE.getText();
        statusPreFilter = generateStatusPrefFilter(Arrays.asList(status));
        lazyLocationSubmissionStatusDataModelActive = new LazyLocationSubmissionStatusDataModel(entityManager, statusPreFilter);
    }

    public void reprocessException(LocationSubmissionException exception) {
        exception.setMarkedRetry(true);
        service.saveOrUpdateException(exception);
        tabIndex = EXCEPTIONS;
    }

    public void changeSublapManually(LocationSubmissionException exception) {
        showChangeSubLapDialog = true;
        currentException = exception;
        tabIndex = EXCEPTIONS;
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('changueSublap').show();");
    }

    public void acceptNewSublap() {
        currentException.setMarkedRetry(true);
        currentException.getLocationSubmissionStatus().setIsoSublap (newSublap.getCode());
        service.saveOrUpdateStatus(currentException.getLocationSubmissionStatus());
        service.saveOrUpdateException(currentException);
        showChangeSubLapDialog = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('changueSublap').hide();");
    }

    public void cancelNewSublap() {
        showChangeSubLapDialog = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('changueSublap').hide();");
    }

    public void markAsReprocess(LocationSubmissionStatus location) {
        String previousStatus = location.getStatus();
        location.setStatus(LocationSubmissionStatus.LocationStatus.PENDING_REPROCESS.getText());
        service.saveOrUpdateStatus(location);
        historyService.saveHistory(createHistoryChange(currentLocation,previousStatus));
        tabIndex = DISPUTED_TAB;
    }

    public void prepareMarkAsNonRegistrable(LocationSubmissionStatus location) {
        showReason = true;
        currentLocation = location;
        nonRegistrableReason = null;
        tabIndex = DISPUTED_TAB;
    }

    public void markAsNonRegistrable() {
        String previousStatus = currentLocation.getStatus();
        currentLocation.setStatus(NON_REGISTRABLE.getText());
        currentLocation.setIsoNonRegistrableReason(nonRegistrableReason);
        currentLocation.setIsoNonRegistrableDate(new Date());
        service.saveOrUpdateStatus(currentLocation);
        historyService.saveHistory(createHistoryChange(currentLocation,previousStatus));
        cancelMarkAsNonRegistrable();
    }

    private History createHistoryChange(LocationSubmissionStatus location, String previousStatus) {
        History history = new History();
        history.setAuthor(((User) identity.getAccount()).getEmail());
        history.setChangeType(History.HistoryChangeType.FIELD);
        history.setCreationDate(new Date());
        history.setEntity(location.getClass().getSimpleName());
        history.setEntityId(location.getId().toString());
        history.setField("status");
        history.setOldValue(previousStatus);
        history.setNewValue(location.getStatus());
        return history;
    }

    public void cancelMarkAsNonRegistrable() {
        nonRegistrableReason = null;
        showReason = false;
        currentLocation = null;
    }
}
