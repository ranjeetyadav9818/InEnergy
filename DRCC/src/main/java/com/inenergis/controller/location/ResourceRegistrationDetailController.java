package com.inenergis.controller.location;

import com.inenergis.controller.carousel.ResourceCaroussel;
import com.inenergis.controller.customerData.DataBeanList;
import com.inenergis.controller.lazyDataModel.LazyLocationSubmissionStatusDataModel;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import com.inenergis.service.IsoRegistrationService;
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
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Named
@ViewScoped
@Getter
@Setter
public class ResourceRegistrationDetailController implements Serializable {

    @Inject
    UIMessage uiMessage;

    @Inject
    ResourceCaroussel caroussel;

    @Inject
    EntityManager entityManager;

    @Inject
    IsoRegistrationService isoRegistrationService;

    Logger log = LoggerFactory.getLogger(ResourceRegistrationDetailController.class);
    public static final int LOCATIONS_TAB = 0;

    private int selectedTab = LOCATIONS_TAB;

    LazyLocationSubmissionStatusDataModel lazyLocationSubmissionStatusDataModel;
    RegistrationSubmissionStatus registration;
    Map<String, Object> prefFilters;
    private List<DataBeanList> resourceDetails = new ArrayList<>();

    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    NumberFormat nf = NumberFormat.getInstance(Locale.US);

    @PostConstruct
    public void init() {
        Long registrationId = ParameterEncoderService.getDefaultDecodedParameterAsLong();
        if (registrationId != null) {
            registration = isoRegistrationService.getById(registrationId);
            if (registration == null) {
                uiMessage.addMessage("Registration not found {0}", registrationId);
                log.error("Registration not found " + registrationId);
            } else {
                prefFilters = generateFilters(registration.getId());
                lazyLocationSubmissionStatusDataModel = new LazyLocationSubmissionStatusDataModel(entityManager, prefFilters);
                resourceDetails = caroussel.printResourceCaroussel(registration.getIsoResource(),df,nf);
            }
        } else {
            uiMessage.addMessage("Registration Id should not be null");
        }
    }

    private Map<String, Object> generateFilters(Long registrationId) {
        Map<String, Object> prefFilters = new HashMap<String, Object>();
        prefFilters.put("registrations.id", registrationId);
        return prefFilters;
    }


    public void onSelectLocation(SelectEvent event) throws IOException {
        LocationSubmissionStatus locationSubmissionStatus = (LocationSubmissionStatus) event.getObject();
        FacesContext.getCurrentInstance().getExternalContext().redirect("LocationSubmissionDetail.xhtml?o=" + ParameterEncoderService.encode(locationSubmissionStatus.getId()));
        FacesContext.getCurrentInstance().responseComplete();
    }

}
