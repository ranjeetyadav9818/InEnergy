package com.inenergis.controller.location;

import com.inenergis.controller.lazyDataModel.LazyIsoResourceDataModel;
import com.inenergis.controller.lazyDataModel.LazyRegistrationSubmissionExceptionDataModel;
import com.inenergis.entity.Lse;
import com.inenergis.entity.ProductType;
import com.inenergis.entity.SubLap;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.locationRegistration.LocationSubmissionException;
import com.inenergis.entity.locationRegistration.RegistrationReview;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionException;
import com.inenergis.entity.marketIntegration.Iso;
import com.inenergis.entity.program.Program;
import com.inenergis.service.IsoResourceService;
import com.inenergis.service.IsoService;
import com.inenergis.service.LseService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.service.SubLapService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
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

@Named
@ViewScoped
@Getter
@Setter
public class ResourceController implements Serializable {

    @Inject
    EntityManager entityManager;

    @Inject
    SubLapService subLapService;

    @Inject
    LseService lseService;

    @Inject
    IsoResourceService isoResourceService;

    @Inject
    IsoService isoService;

    @Inject
    UIMessage uiMessage;

    public static final int RESOURCE_NAME_AUTOCOMPLETE_MAX_RESULTS = 10;

    Logger log = LoggerFactory.getLogger(ResourceController.class);

    LazyIsoResourceDataModel lazyIsoResourceDataModel;
    LazyRegistrationSubmissionExceptionDataModel lazyRegistrationSubmissionExceptionDataModel;
    Map<String, Object> exceptionsPreFilter;

    //fields for filters
    private SubLap subLap;
    private Lse lse;
    private RegistrationReview registrationReview;
    private ProductType productType = ProductType.RDRR;
    private String resourceName;

    private List<SubLap> subLapList;
    private List<Lse> lseList;
    private Iso iso;
    private ProductType[] productTypes = ProductType.values();
    private RegistrationReview[] registrationReviews = RegistrationReview.values();

    boolean render = false;

    @PostConstruct
    public void init() {
        subLapList = subLapService.getAll();
        lseList = lseService.getAll();
        iso = ParameterEncoderService.retrieveIsoFromParameter(isoService, uiMessage);
    }

    private void populateLocationExceptions() {
        exceptionsPreFilter = generateExceptionsPreFilter();
        lazyRegistrationSubmissionExceptionDataModel = new LazyRegistrationSubmissionExceptionDataModel(entityManager, exceptionsPreFilter);
    }

    private Map<String, Object> generateExceptionsPreFilter() {
        Map<String, Object> preFilter = new HashMap<>();

        preFilter.put("registrationSubmissionStatus.isoResource.type", productType);

        if (resourceName != null) {
            preFilter.put("registrationSubmissionStatus.isoResource.name", resourceName);
        }
        if (subLap != null) {
            preFilter.put("registrationSubmissionStatus.isoResource.isoSublap", subLap.getCode());
        }
        if (lse != null) {
            preFilter.put("registrationSubmissionStatus.isoResource.isoLse", lse.getCode());
        }
        if (registrationReview != null) {
            preFilter.put("registrationSubmissionStatus.isoResource.registrationReview", registrationReview);
        }
        if (iso != null) {
            preFilter.put("registrationSubmissionStatus.isoName", iso.getName());
        }
        return preFilter;
    }

    private Map<String, Object> generatePreFilter() {
        Map<String, Object> preFilter = new HashMap<>();

        if (StringUtils.isNotBlank(resourceName)) {
            preFilter.put("name", resourceName);
        }

        preFilter.put("type", productType);

        if (registrationReview != null) {
            preFilter.put("registrationReview", registrationReview);
        }

        if (lse != null) {
            preFilter.put("isoLse", lse.getCode());
        }

        if (subLap != null) {
            preFilter.put("isoSublap", subLap.getCode());
        }
        if (iso != null) {
            preFilter.put("isoProduct.profile.iso.id", iso.getId());
        }
        return preFilter;
    }

    public void search() {
        lazyIsoResourceDataModel = new LazyIsoResourceDataModel(entityManager, generatePreFilter());
        populateLocationExceptions();

        render = true;
    }

    public void clear() {
        resourceName = null;
        productType = ProductType.RDRR;
        registrationReview = null;
        lse = null;
        subLap = null;

        render = false;
    }

    public void onSelect(SelectEvent event) throws IOException {
        IsoResource isoResource = (IsoResource) event.getObject();

        FacesContext.getCurrentInstance().getExternalContext()
                .redirect("ResourceDetails.xhtml?o=" + ParameterEncoderService.encode(isoResource.getId()));
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void onSelectException(SelectEvent event) throws IOException {
        RegistrationSubmissionException exception = (RegistrationSubmissionException) event.getObject();
        if (exception.getRegistrationSubmissionStatus() != null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("ResourceDetails.xhtml?o=" + ParameterEncoderService.encode(exception.getRegistrationSubmissionStatus().getIsoResource().getId()));
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    public List<String> completeResource(String query) {
        List<String> resources = new ArrayList<>();

        List<IsoResource> isoResources = isoResourceService.findWithNameLike(query, RESOURCE_NAME_AUTOCOMPLETE_MAX_RESULTS);
        for (IsoResource isoResource : isoResources) {
            resources.add(isoResource.getName());
        }

        return resources;
    }
}
