package com.inenergis.billingEngine.service;

import com.inenergis.billingEngine.sa.AgreementPointMapDao;
import com.inenergis.billingEngine.sa.ServiceAgreementDao;
import com.inenergis.billingEngine.QueueListener;
import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.program.RatePlanProfile;
import com.inenergis.entity.program.rateProgram.RateProfileAncillaryFee;
import com.inenergis.entity.program.rateProgram.RateProfileAncillaryPercentageFee;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ServiceAgreementService {

    private static final Logger log = LoggerFactory.getLogger(QueueListener.class);

    @Autowired
    private AgreementPointMapDao agreementPointMapDao;

    @Autowired
    private ServiceAgreementDao serviceAgreementDao;

    @Transactional("mysqlTransactionManager")
    public BaseServiceAgreement getServiceAgreement(String saId) {
        AgreementPointMap apm = agreementPointMapDao.getFirstByServiceAgreementServiceAgreementId(saId);
        if (apm == null) {
            log.info("Service agreement {} has no service points associated", saId);
            return null;
        }
        BaseServiceAgreement serviceAgreement = apm.getServiceAgreement();
        serviceAgreement.getAgreementPointMaps().forEach(a -> a.getServicePoint());
        if (CollectionUtils.isNotEmpty( serviceAgreement.getRatePlanEnrollments())) {
            serviceAgreement.getRatePlanEnrollments().stream().filter(e -> e.getRatePlan().getActiveProfile() != null)
                    .forEach(e ->
                    {
                        final RatePlanProfile activeProfile = e.getRatePlan().getActiveProfile();
                        if (activeProfile != null) {
                            if (CollectionUtils.isNotEmpty(activeProfile.getConsumptionFees())) {
                                activeProfile.getConsumptionFees().size();
                            }
                            if (CollectionUtils.isNotEmpty(activeProfile.getAncillaryFees())) {
                                activeProfile.getAncillaryFees().size();
                                for (RateProfileAncillaryFee ancillaryFee : activeProfile.getAncillaryFees()) {
                                    if (ancillaryFee.getCalendar() != null && ancillaryFee.getCalendar().getTimeOfUses() != null) {
                                        ancillaryFee.getCalendar().getTimeOfUses().forEach(t -> {
                                            t.getTimeOfUseHours().size();
                                            t.getTimeOfUseDays().size();
                                        });
                                    }
                                }
                            }
                            if (CollectionUtils.isNotEmpty(activeProfile.getAncillaryPercentageFees())) {
                                for (RateProfileAncillaryPercentageFee ancillaryPercentageFee : activeProfile.getActiveAncillaryPercentageFees()) {
                                    ancillaryPercentageFee.getApplicableFees().size();
                                }

                            }
                        }
                    });
        }
        if (CollectionUtils.isNotEmpty( serviceAgreement.getEnrollments())) {
            serviceAgreement.getEnrollments().stream().filter(e -> CollectionUtils.isNotEmpty(e.getLocations()))
                    .forEach(e -> e.getLocations().stream().filter(l->CollectionUtils.isNotEmpty(l.getEventAppearances()))
                            .forEach(l -> l.getEventAppearances().size()));
        }
        return serviceAgreement;
    }
    public List<BaseServiceAgreement> getServiceAgreementByBillingCycle(String serial, Pageable pageable) {
        return serviceAgreementDao.findByBillCycleCd(serial, pageable);
    }
}