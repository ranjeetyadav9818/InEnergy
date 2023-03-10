package com.inenergis.controller.award;

import com.inenergis.controller.general.ConstantsProvider;
import com.inenergis.controller.lazyDataModel.LazyAwardDataModel;
import com.inenergis.controller.lazyDataModel.LazyAwardExceptiolnDataModel;
import com.inenergis.controller.location.ResourceController;
import com.inenergis.entity.ProductType;
import com.inenergis.entity.award.Award;
import com.inenergis.entity.award.AwardException;
import com.inenergis.entity.genericEnum.AwardInstruction;
import com.inenergis.entity.genericEnum.RetailDispatchScheduleType;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.marketIntegration.Iso;
import com.inenergis.service.AwardService;
import com.inenergis.service.IsoResourceService;
import com.inenergis.service.IsoService;
import com.inenergis.service.LseService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.service.SubLapService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.primefaces.context.PrimeFacesContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
@Getter
@Setter
public class AwardSummaryController implements Serializable {

    private Date tradeDateFrom;
    private Date tradeDateTo;
    private ProductType productType = ProductType.RDRR;
    private String resourceName;

    private boolean render = false;
    LazyAwardDataModel lazyAwardDataModel;
    LazyAwardExceptiolnDataModel lazyAwardExceptionDataModel;
    Map<String, Object> awardPermanentFilters;
    Map<String, Object> exceptionPermanentFilters;
    private Iso iso;

    @Inject
    EntityManager entityManager;

    @Inject
    SubLapService subLapService;

    @Inject
    LseService lseService;

    @Inject
    IsoResourceService isoResourceService;

    @Inject
    AwardService awardService;

    @Inject
    IsoService isoService;

    @Inject
    UIMessage uiMessage;

    Award award;
    Logger log = LoggerFactory.getLogger(AwardSummaryController.class);

    @PostConstruct
    public void init() {
        retrieveDatesFromParams();
        resourceName = ParameterEncoderService.getDecodedParameter("n");
        if (tradeDateFrom != null) {
            search();
        }
        awardPermanentFilters = new HashedMap();
        exceptionPermanentFilters = new HashedMap();
        iso = ParameterEncoderService.retrieveIsoFromParameter(isoService, uiMessage);
    }

    private void retrieveDatesFromParams() {
        String sDatefrom = null;
        String sDateTo = null;
        try {
            sDatefrom = PrimeFacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("f");
            if (sDatefrom != null) {
                tradeDateFrom = ConstantsProvider.DATE_FORMAT.parse(sDatefrom);
            }
        } catch (ParseException e) {
            log.error("error parsing dates sDatefrom" + sDatefrom, e);
        }
        try {
            sDateTo = PrimeFacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("t");
            if (sDateTo != null) {
                tradeDateTo = ConstantsProvider.DATE_FORMAT.parse(sDateTo);
            }
        } catch (ParseException e) {
            log.error("error parsing dates sDateTo" + sDateTo, e);
        }
    }

    public List<String> completeResource(String query) {

        List<String> resources = new ArrayList<>();

        List<IsoResource> isoResources = isoResourceService.findWithNameLike(query, ResourceController.RESOURCE_NAME_AUTOCOMPLETE_MAX_RESULTS);
        for (IsoResource isoResource : isoResources) {
            resources.add(isoResource.getName());
        }

        return resources;
    }

    public void onRowEdit(RowEditEvent event) {
        AwardException exception = (AwardException) event.getObject();
        awardService.saveException(exception);
    }

    public void onSelect(SelectEvent event) throws IOException {
        Award award = (Award) event.getObject();
        String redirectionString = "AwardDetail.xhtml?o=" + ParameterEncoderService.encode(award.getId());

        if (tradeDateFrom != null) {
            redirectionString += "&f=" + ConstantsProvider.DATE_FORMAT.format(tradeDateFrom);
        }

        if (tradeDateTo != null) {
            redirectionString += "&t=" + ConstantsProvider.DATE_FORMAT.format(tradeDateTo);
        }

        if (!StringUtils.isEmpty(resourceName)) {
            redirectionString += "&n=" + ParameterEncoderService.encode(resourceName);
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect(redirectionString);
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void onRowCancel(RowEditEvent event) throws IOException {
    }

    public void search() {
        render = true;
        exceptionPermanentFilters = buildExceptionPermanentFilters();
        awardPermanentFilters = buildAwardPermanentFilters();
        lazyAwardDataModel = new LazyAwardDataModel(entityManager, awardPermanentFilters);
        lazyAwardExceptionDataModel = new LazyAwardExceptiolnDataModel(entityManager, exceptionPermanentFilters);
    }

    private Map<String, Object> buildAwardPermanentFilters() {

        Map<String, Object> permanentFilters = new HashedMap();
        if (tradeDateFrom != null) {
            permanentFilters.put("tradeDateFrom", tradeDateFrom);
        }
        if (tradeDateTo != null) {
            permanentFilters.put("tradeDateTo", tradeDateTo);
        }
        if (productType != null) {
            permanentFilters.put("resource.isoProduct.type", productType);
//            permanentFilters.put("resource.isoProduct.type", productTypeString);
        }
        if (!StringUtils.isEmpty(resourceName)) {
            permanentFilters.put("resource.name", resourceName);
        }
        if (iso != null) {
            permanentFilters.put("resource.isoProduct.profile.iso.id", iso.getId());
        }
        return permanentFilters;
    }

    private Map<String, Object> buildExceptionPermanentFilters() {

        Map<String, Object> permanentFilters = new HashedMap();
        if (tradeDateFrom != null) {
            permanentFilters.put("tradeDateFrom", tradeDateFrom);
        }
        if (tradeDateTo != null) {
            permanentFilters.put("tradeDateTo", tradeDateTo);
        }
        if (productType != null) {
            permanentFilters.put("award.resource.isoProduct.type", productType);
//            permanentFilters.put("instruction.resource.isoProduct.type", productTypeString);
        }
        if (!StringUtils.isEmpty(resourceName)) {
            permanentFilters.put("award.resource.name", resourceName);
        }
        if (iso != null) {
            permanentFilters.put("award.resource.isoProduct.profile.iso.id", iso.getId());
        }
        return permanentFilters;
    }
}
