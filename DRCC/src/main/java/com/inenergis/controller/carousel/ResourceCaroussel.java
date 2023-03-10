package com.inenergis.controller.carousel;


import com.inenergis.controller.customerData.DataBean;
import com.inenergis.controller.customerData.DataBeanList;
import com.inenergis.controller.general.ConstantsProvider;
import com.inenergis.entity.genericEnum.DispatchType;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.locationRegistration.PmaxPmin;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import com.inenergis.util.BundleAccessor;
import com.inenergis.util.ConstantsProviderModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ResourceCaroussel implements Serializable {

    @Inject
    BundleAccessor bundleAccessor;

    enum STATUS {
        ACTIVE("Active", true),
        INACTIVE("Inactive", false);
        private String statusName;

        STATUS(String currentStatus, boolean boolStatus) {
            this.statusName = currentStatus;
        }

        static STATUS getByValue(boolean value) {
            return value ? ACTIVE : INACTIVE;
        }

        public String getStatusName() {
            return statusName;
        }

    }

    public String getProgramSet(List<LocationSubmissionStatus> locations) {
        if (CollectionUtils.isEmpty(locations)) {
            return null;
        }
        String programName = null;
        for (LocationSubmissionStatus location : locations) {
            String locationProgramName = location.getProgramServiceAgreementEnrollment().getProgram().getName();
            if(programName!=null && !programName.equals(locationProgramName)){
                return "Mixed";
            }else{
                programName = locationProgramName;
            }
        }
        return programName;
    }

    public List<DataBeanList> printResourceCaroussel(IsoResource resource, DateFormat df, NumberFormat nf) {
        List<DataBeanList> resourceDetails = new ArrayList<>();

        List<DataBean> cBeans = new ArrayList<>();
        cBeans.add(new DataBean("Resource ID", resource.getName()));
//        cBeans.add(new DataBean("MARKET Id", resource.getIsoId()));
//        cBeans.add(new DataBean("MARKET Status", getISOStatus(resource)));
        cBeans.add(new DataBean("Type", resource.getType().getName()));
        cBeans.add(new DataBean(bundleAccessor.getPropertyResourceBundle().getString("data.mapping.sublap"), resource.getIsoSublap ()));
        cBeans.add(new DataBean("LSE", resource.getIsoLse()));
        DataBeanList resourceSummary = new DataBeanList("Resource Summary", ConstantsProvider.ICON_VCARD, cBeans);
        resourceDetails.add(resourceSummary);

        cBeans = new ArrayList<>();
        PmaxPmin pmaxPmin = resource.getActivePmaxPmin();

        cBeans.add(new DataBean("Estimated Capacity (kW)", nf.format(BigDecimal.valueOf(resource.getCalculatedCapacity()).divide(BigDecimal.valueOf(ConstantsProviderModel.KW_PRECISION)))));
        cBeans.add(new DataBean("Pmax", pmaxPmin == null ? StringUtils.EMPTY : pmaxPmin.getPmax().toString()));
        if(resource.getIsoProduct()!=null){
            cBeans.add(new DataBean("Discrete Load", (DispatchType.Discrete.equals(resource.getIsoProduct().getDispatchType()))?"Yes":"No"));
        }
        DataBeanList resourceCapacity = new DataBeanList("Resource Capacity", ConstantsProvider.ICON_VCARD, cBeans);
        resourceDetails.add(resourceCapacity);

        cBeans = new ArrayList<>();
        RegistrationSubmissionStatus activeRegistration = resource.getActiveRegistration();
        int numActiveLocations = 0;
        if (activeRegistration != null && activeRegistration.getLocations() != null) {
            numActiveLocations = activeRegistration.getLocations().size();
        }
        cBeans.add(new DataBean("Active Reg ID", activeRegistration == null ? StringUtils.EMPTY : activeRegistration.getIsoRegistrationId()));
        cBeans.add(new DataBean("Active Reg Start", activeRegistration == null ? StringUtils.EMPTY : df.format(activeRegistration.getActiveStartDate())));
        cBeans.add(new DataBean("Active Locations", Integer.toString(numActiveLocations)));
        cBeans.add(new DataBean("Program", activeRegistration == null ? StringUtils.EMPTY : getProgramSet(activeRegistration.getLocations())));
        DataBeanList registration = new DataBeanList("Registration", ConstantsProvider.ICON_VCARD, cBeans);
        resourceDetails.add(registration);

        return resourceDetails;
    }

    private String getISOStatus(IsoResource resource) {
        if(resource.getIsoProduct() !=null){
            return STATUS.getByValue(resource.getIsoProduct().getProfile().getIso().isActive()).getStatusName();
        }
        return null;
    }
}
