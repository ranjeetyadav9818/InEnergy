package com.inenergis.controller.carousel;

import com.inenergis.controller.customerData.DataBean;
import com.inenergis.controller.customerData.DataBeanList;
import com.inenergis.controller.general.ConstantsProvider;
import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.util.BundleAccessor;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class LocationCarousel {

    @Inject
    ServiceAgreementCarousel serviceAgreementCarousel;
    @Inject
    BundleAccessor bundleAccessor;

    public List<DataBeanList> printLocationCarousel(LocationSubmissionStatus locationSubmissionStatus) {
        List<DataBeanList> result = new ArrayList<>();
        if (locationSubmissionStatus != null) {
            // Location Details
            List<DataBean> lBeans = new ArrayList<>();
            lBeans.add(new DataBean("Location Market ID", locationSubmissionStatus.getIsoResourceId()));
            lBeans.add(new DataBean("Location Status", locationSubmissionStatus.getStatus()));
            lBeans.add(new DataBean("Program", locationSubmissionStatus.getProgram().getName()));
            lBeans.add(new DataBean(bundleAccessor.getPropertyResourceBundle().getString("data.mapping.sublap"), locationSubmissionStatus.getIsoSublap ()));
            lBeans.add(new DataBean("LSE", locationSubmissionStatus.getIsoLse()));
            lBeans.add(new DataBean("Current Resource", locationSubmissionStatus.getActiveResource()!=null?locationSubmissionStatus.getActiveResource().getName():"None"));

            DataBeanList locationDetails = new DataBeanList("Location Details", ConstantsProvider.ICON_VCARD, lBeans);
            result.add(locationDetails);

            BaseServiceAgreement serviceAgreement = locationSubmissionStatus.getProgramServiceAgreementEnrollment().getServiceAgreement();
            if (serviceAgreement != null && serviceAgreement.getAgreementPointMaps() != null && !serviceAgreement.getAgreementPointMaps().isEmpty()) {
                result.addAll(serviceAgreementCarousel.printServiceAgreementCarousel(serviceAgreement.getAgreementPointMaps().get(0)));
            }
        }

        return result;
    }
}