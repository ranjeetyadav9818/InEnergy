package com.inenergis.controller.location;

import com.inenergis.controller.carousel.ResourceCaroussel;
import com.inenergis.controller.customerData.DataBeanList;
import com.inenergis.controller.lazyDataModel.LazyBidHistoryModel;
import com.inenergis.controller.lazyDataModel.LazyPmaxPminDataModel;
import com.inenergis.controller.lazyDataModel.LazyRegistrationSubmissionStatusDataModel;
import com.inenergis.entity.award.Award;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.locationRegistration.PmaxPmin;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import com.inenergis.entity.program.ImpactedResource;
import com.inenergis.service.IsoResourceService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.util.ConstantsProviderModel;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.picketlink.Identity;
import org.picketlink.idm.model.basic.User;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
@Getter
@Setter
public class ResourceDetailController implements Serializable {
    static final int REGISTRATION_HISTORY = 0;
    static final int PMAX_PMIN_HISTORY = 1;
    int tabIndex = REGISTRATION_HISTORY;
    Logger log = LoggerFactory.getLogger(ResourceDetailController.class);
    Map<String, Object> registrationStatusPreFilter;
    Map<String, Object> pmaxPminPreFilter;
    Map<String, Object> bidHistoryPreFilter;
    private List<DataBeanList> resourceDetails = new ArrayList<>();


    @Inject
    UIMessage uiMessage;

    @Inject
    ResourceCaroussel caroussel;

    @Inject
    IsoResourceService isoResourceService;

    @Inject
    Identity identity;

    @Inject
    EntityManager entityManager;

    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    NumberFormat nf = NumberFormat.getInstance(ConstantsProviderModel.LOCALE);

    LazyRegistrationSubmissionStatusDataModel lazyRegistrationSubmissionStatusDataModel;
    LazyPmaxPminDataModel lazyPmaxPminDataModel;
    LazyBidHistoryModel lazyBidHistoryModel;

    IsoResource resource;

    @PostConstruct
    public void init() {
        Long resourceId = ParameterEncoderService.getDefaultDecodedParameterAsLong();
        if (resourceId != null) {
            resource = isoResourceService.getById(resourceId);
            if (resource != null) {
                resourceDetails = caroussel.printResourceCaroussel(resource, df, nf);
                registrationStatusPreFilter = generateResourcePrefFilter(resourceId);
                lazyRegistrationSubmissionStatusDataModel = new LazyRegistrationSubmissionStatusDataModel(entityManager, registrationStatusPreFilter);
                pmaxPminPreFilter = generatePmaxPminPreFilter(resourceId);
                lazyPmaxPminDataModel = new LazyPmaxPminDataModel(entityManager, pmaxPminPreFilter);
                bidHistoryPreFilter = generateBidHistoryPreFilter(resourceId);
                lazyBidHistoryModel = new LazyBidHistoryModel(entityManager, bidHistoryPreFilter);

            } else {
                uiMessage.addMessage("Iso Resource not found {0}", resourceId.toString());
                log.error("Market resource not found " + resourceId);
            }
        } else {
            uiMessage.addMessage("Market Resource should not be empty ");
        }
    }

    private Map<String, Object> generatePmaxPminPreFilter(Long resourceId) {
        Map<String, Object> prefFilters = new HashMap<>();
        prefFilters.put("isoResource.id", resourceId);
        return prefFilters;
    }

    private Map<String, Object> generateResourcePrefFilter(Long resourceId) {
        Map<String, Object> prefFilters = new HashMap<>();
        prefFilters.put("isoResource.id", resourceId);
        return prefFilters;
    }

    private Map<String, Object> generateBidHistoryPreFilter(Long resourceId) {
        Map<String, Object> prefFilters = new HashMap<>();
        prefFilters.put("isoResource.id", resourceId);
        return prefFilters;
    }

    public void onSelectStatus(SelectEvent event) throws IOException {
        RegistrationSubmissionStatus registrationSubmissionStatus = (RegistrationSubmissionStatus) event.getObject();
        FacesContext.getCurrentInstance().getExternalContext().redirect("ResourceRegistrationDetail.xhtml?o=" + ParameterEncoderService.encode(registrationSubmissionStatus.getId()));
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void onPmaxPminRowEdit(RowEditEvent event) {
        PmaxPmin transientPmaxPmin = (PmaxPmin) event.getObject();
        if (transientPmaxPmin.getEffectiveEndDate() != null) {
            if (transientPmaxPmin.getEffectiveEndDate().before(transientPmaxPmin.getEffectiveStartDate())) {
                uiMessage.addMessage("Effective end date should be after {0}", FacesMessage.SEVERITY_ERROR, df.format(transientPmaxPmin.getEffectiveStartDate()));
                return;
            }
            addNewPmaxPminWhenEndDate(transientPmaxPmin);
        }
        isoResourceService.savePmaxPmin(transientPmaxPmin);
        resource = transientPmaxPmin.getIsoResource();
        tabIndex = PMAX_PMIN_HISTORY;
        RequestContext.getCurrentInstance().update("form:TbPmaxPminHist");
    }

    private void addNewPmaxPminWhenEndDate(PmaxPmin transientPmaxPmin) {
        PmaxPmin pmaxPmin = new PmaxPmin();
        pmaxPmin.setEffectiveStartDate(transientPmaxPmin.getEffectiveEndDate());
        pmaxPmin.setEstimated_capacity(transientPmaxPmin.getEstimated_capacity());
        pmaxPmin.setIsoResource(transientPmaxPmin.getIsoResource());
        pmaxPmin.setLastUpdateBy(((User) identity.getAccount()).getEmail());
        pmaxPmin.setLastUpdateDate(new Date());
        pmaxPmin.setPmax(transientPmaxPmin.getPmax());
        pmaxPmin.setPmin(transientPmaxPmin.getPmin());
        transientPmaxPmin.getIsoResource().getPmaxPminList().add(pmaxPmin);
        isoResourceService.savePmaxPmin(pmaxPmin);
    }

    public String formatDate(Date date) {
        if (date != null) {
            try {
                return df.format(date);
            } catch (Exception e) {
                log.error(" Failed to parse date " + date, e);
            }
        }
        return StringUtils.EMPTY;
    }

    public String formatMeterStatus(String success) {
        if ("1".equals(success)) {
            return "Success";
        } else {
            return "Error";
        }
    }

    public void onAwardSelect(SelectEvent event) throws IOException {
        Award award = (Award) event.getObject();
        FacesContext.getCurrentInstance().getExternalContext().redirect("AwardDetail.xhtml?o=" + ParameterEncoderService.encode(award.getId()));
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void onEventSelect(SelectEvent event) throws IOException {
        ImpactedResource impactedResource = (ImpactedResource) event.getObject();
        FacesContext.getCurrentInstance().getExternalContext().redirect("EventDispatchDetail.xhtml?o=" + ParameterEncoderService.encode(impactedResource.getEvent().getId()));
        FacesContext.getCurrentInstance().responseComplete();
    }
}