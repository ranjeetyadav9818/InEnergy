package com.inenergis.controller.location;

import com.inenergis.controller.carousel.LocationCarousel;
import com.inenergis.controller.customerData.DataBeanList;
import com.inenergis.controller.lazyDataModel.LazyBidHistoryModel;
import com.inenergis.controller.servicePointData.SelectableAgreementPointMap;
import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.History;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.locationRegistration.LocationSubmissionException;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import com.inenergis.service.HistoryService;
import com.inenergis.service.LocationSubmissionStatusService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named
@ViewScoped
@Getter
@Setter
public class LocationSubmissionDetailController implements Serializable {

    static final int SERVICE_POINTS = 0;
    static final int EXCEPTIONS = 1;
    int tabIndex = SERVICE_POINTS;
    Logger log = LoggerFactory.getLogger(LocationSubmissionController.class);
    Long lrsId;
    Map<String, Object> preFilter;
    LocationSubmissionStatus locationSubmissionStatus;
    private ServiceAgreement selectedServiceAgreement;
    private SelectableAgreementPointMap selectableAgreementPointMap;

    private List<DataBeanList> locationDetails = new ArrayList<>();
    private String nonRegistrableReason;
    private boolean showReason = false;
    private LocationSubmissionException currentException = null;

    @Inject
    EntityManager entityManager;

    @Inject
    UIMessage uiMessage;

    @Inject
    private LocationCarousel locationCarousel;

    @Inject
    LocationSubmissionStatusService service;

    @Inject
    private HistoryService historyService;

    Map<String, Object> bidHistoryPreFilter;
    LazyBidHistoryModel lazyBidHistoryModel;
    private List<History> historyList;

    @PostConstruct
    public void init() {
        lrsId = ParameterEncoderService.getDefaultDecodedParameterAsLong();
        if (lrsId != null) {
            locationSubmissionStatus = service.getStatus(lrsId);
            if (locationSubmissionStatus != null) {
                selectedServiceAgreement = (ServiceAgreement)locationSubmissionStatus.getProgramServiceAgreementEnrollment().getServiceAgreement();
                selectableAgreementPointMap = new SelectableAgreementPointMap(selectedServiceAgreement.getAgreementPointMaps());
                locationDetails = locationCarousel.printLocationCarousel(locationSubmissionStatus);
                List<Long> registrationIds = locationSubmissionStatus.getRegistrations().stream()
                        .map(RegistrationSubmissionStatus::getId)
                        .collect(Collectors.toList());
                if (!registrationIds.isEmpty()) {
                    bidHistoryPreFilter = generateBidHistoryPreFilter(registrationIds);
                    lazyBidHistoryModel = new LazyBidHistoryModel(entityManager, bidHistoryPreFilter);
                }
                historyList = historyService.getHistory(locationSubmissionStatus);
            } else {
                uiMessage.addMessage("Status not found {0}", lrsId.toString());
                log.error("Status not found " + lrsId.toString());
            }
        } else {
            uiMessage.addMessage("Status should not be empty");
        }
    }

    public void reprocessException(LocationSubmissionException exception) {
        exception.setMarkedRetry(Boolean.TRUE);
        service.saveOrUpdateException(exception);
        tabIndex = EXCEPTIONS;
    }

    public void markAsReprocess(LocationSubmissionException exception) {
        reprocessException(exception);
        exception.getLocationSubmissionStatus().setStatus(LocationStatus.PENDING_REPROCESS.getText());
        service.saveOrUpdateStatus(exception.getLocationSubmissionStatus());
    }

    public void prepareMarkAsNonRegistrable(LocationSubmissionException exception) {
        showReason = true;
        currentException = exception;
        nonRegistrableReason = null;
    }

    public void markAsNonRegistrable() {
        reprocessException(currentException);
        currentException.getLocationSubmissionStatus().setStatus(LocationStatus.NON_REGISTRABLE.getText());
        currentException.getLocationSubmissionStatus().setIsoNonRegistrableReason(nonRegistrableReason);
        service.saveOrUpdateStatus(currentException.getLocationSubmissionStatus());
        cancelMarkAsNonRegistrable();
    }

    public void cancelMarkAsNonRegistrable() {
        nonRegistrableReason = null;
        showReason = false;
        currentException = null;
    }

    public void onServicePointSelect(SelectEvent event) throws IOException {
        AgreementPointMap apm = ((AgreementPointMap) event.getObject());
        FacesContext.getCurrentInstance().getExternalContext().redirect("ServicePointList.xhtml?o=" + ParameterEncoderService.encode(apm.getServicePoint().getServicePointId()));
        FacesContext.getCurrentInstance().responseComplete();
    }

    private Map<String, Object> generateBidHistoryPreFilter(List<Long> registrationIds) {
        Map<String, Object> prefFilters = new HashMap<>();
        prefFilters.put("registration.id", registrationIds);
        return prefFilters;
    }
}
