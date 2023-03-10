package com.inenergis.rest.services;

import com.inenergis.dao.CriteriaCondition;
import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.ServicePoint;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import com.inenergis.rest.model.registrationDetails.RegistrationDetailsOutput;
import com.inenergis.rest.model.registrationDetails.RegistrationDetailsRequest;
import com.inenergis.rest.model.registrationDetails.RegistrationDetailsResponse;
import com.inenergis.rest.model.registrationDetails.RegistrationDetailsSAId;
import com.inenergis.rest.model.registrationDetails.RegistrationDetailsSPId;
import com.inenergis.service.IsoRegistrationService;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;

@Stateless
public class RegistrationDetailRESTService {

    @Inject
    IsoRegistrationService isoRegistrationService;

    public RegistrationDetailsResponse getRegistrationDetails(RegistrationDetailsRequest input) {

        String sId = input.getRegistrationDetailsRequest().getRegistrationId();
        RegistrationDetailsResponse result = new RegistrationDetailsResponse();
        if (! StringUtils.isEmpty(sId)) {
            RegistrationSubmissionStatus registration;

            registration = isoRegistrationService.getUniqueResultWithCriteria(Arrays.asList(CriteriaCondition.builder().key("isoRegistrationId").value(sId).matchMode(MatchMode.EXACT).build()));
            if(registration == null){
                try{
                    registration = isoRegistrationService.getUniqueResultWithCriteria(Arrays.asList(CriteriaCondition.builder().key("id").value(Long.parseLong(sId)).matchMode(MatchMode.EXACT).build()));
                } catch (Exception e){

                }
            }


            if (registration != null) {
                changeRegistrationStatus(registration);

                final RegistrationDetailsOutput registrationDetailsResponse = new RegistrationDetailsOutput();
                registrationDetailsResponse.setCaisoRegistrationId(registration.getIsoRegistrationId());
                registrationDetailsResponse.setEndDate(registration.getActiveEndDate());
                registrationDetailsResponse.setPgeRegistrationId(registration.getId().toString());
                registrationDetailsResponse.setStartDate(registration.getActiveStartDate());
                registrationDetailsResponse.setStatus(registration.getRegistrationStatus().toString());

                final ArrayList<RegistrationDetailsSAId> saIds = new ArrayList<>();
                for (LocationSubmissionStatus locationSubmissionStatus : registration.getLocations()) {
                    RegistrationDetailsSAId registrationDetailsSAId = new RegistrationDetailsSAId();
                    BaseServiceAgreement serviceAgreement = locationSubmissionStatus.getProgramServiceAgreementEnrollment().getServiceAgreement();
                    registrationDetailsSAId.setSaId(serviceAgreement.getServiceAgreementId());
                    final ArrayList<RegistrationDetailsSPId> spIds = new ArrayList<>();
                    for (AgreementPointMap agreementPointMap : serviceAgreement.getAgreementPointMaps()) {
                        RegistrationDetailsSPId registrationDetailsSPId = new RegistrationDetailsSPId();
                        ServicePoint servicePoint = (ServicePoint) agreementPointMap.getServicePoint();
                        registrationDetailsSPId.setSpId(servicePoint.getServicePointId());
                        registrationDetailsSPId.setVoltageIndicator(servicePoint.getServiceVoltageClass());
                        spIds.add(registrationDetailsSPId);
                    }
                    registrationDetailsSAId.setSpIds(spIds);
                    saIds.add(registrationDetailsSAId);
                }
                registrationDetailsResponse.setSaIds(saIds);
                result.setRegistrationDetailsResponse(registrationDetailsResponse);

                isoRegistrationService.save(registration);
            }
        }
        return result;
    }

    private void changeRegistrationStatus(RegistrationSubmissionStatus registration) {
        if (registration.getRegistrationStatus().equals(RegistrationSubmissionStatus.RegistrationStatus.WAITING_FOR_SQMD)) {
            registration.setRegistrationStatus(RegistrationSubmissionStatus.RegistrationStatus.REGISTERED);
        }
    }
}