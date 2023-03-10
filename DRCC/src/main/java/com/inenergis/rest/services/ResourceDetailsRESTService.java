package com.inenergis.rest.services;


import com.inenergis.dao.CriteriaCondition;
import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.ServicePoint;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import com.inenergis.rest.model.resourceDetails.ResourceDetailsInput;
import com.inenergis.rest.model.resourceDetails.ResourceDetailsList;
import com.inenergis.rest.model.resourceDetails.ResourceDetailsOutput;
import com.inenergis.rest.model.resourceDetails.ResourceDetailsRegistrationId;
import com.inenergis.rest.model.resourceDetails.ResourceDetailsResponse;
import com.inenergis.rest.model.resourceDetails.ResourceDetailsSAId;
import com.inenergis.rest.model.resourceDetails.ResourceDetailsSPId;
import com.inenergis.service.IsoResourceService;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Stateless
public class ResourceDetailsRESTService {

    @Inject
    IsoResourceService isoResourceService;

    public ResourceDetailsResponse getRResourceDetails(ResourceDetailsList resourceDetailsRequest) {
        List<Long> pgeIds = new ArrayList<>(resourceDetailsRequest.getResourceIds().size());
        List<String> caisoIds = new ArrayList<>(resourceDetailsRequest.getResourceIds().size());
        splitIds(resourceDetailsRequest.getResourceIds(), caisoIds, pgeIds);
        List<ResourceDetailsOutput> resourceDetailsOutputs = new ArrayList<>();
        ResourceDetailsResponse result = new ResourceDetailsResponse();
        List<IsoResource> resources = new ArrayList<>();
        if (!pgeIds.isEmpty()) {
            resources.addAll(isoResourceService.getByIds(pgeIds));
        }
        if (!caisoIds.isEmpty()) {
            resources.addAll(isoResourceService.getWithCriteria(Collections.singletonList(CriteriaCondition.builder().key("name").value(caisoIds).matchMode(MatchMode.EXACT).build())));

        }
        if (CollectionUtils.isEmpty(resources)) { //Return resources list with empty registrations collection
            for (ResourceDetailsInput resourceId : resourceDetailsRequest.getResourceIds()) {
                String sResourceId = resourceId.getResourceId();
                ResourceDetailsOutput resourceDetailsOutput = new ResourceDetailsOutput();
                resourceDetailsOutput.setResourceId(sResourceId);
                resourceDetailsOutputs.add(resourceDetailsOutput);
            }
        } else {
            for (IsoResource resource : resources) {
                ResourceDetailsOutput resourceDetailsOutput = new ResourceDetailsOutput();
                resourceDetailsOutput.setResourceId(resource.getName());
                ArrayList<ResourceDetailsRegistrationId> registrationIds = new ArrayList<>();
                resourceDetailsOutput.setRegistrationIds(registrationIds);
                for (RegistrationSubmissionStatus registrationSubmissionStatus : resource.getRegistrations()) {
                    ResourceDetailsRegistrationId resourceDetailsRegistrationId = populateResourceDetailsRegistrationId(registrationSubmissionStatus);
                    registrationIds.add(resourceDetailsRegistrationId);
                }
                resourceDetailsOutputs.add(resourceDetailsOutput);
            }
            result.setResourceIds(resourceDetailsOutputs);
        }
        return result;
    }

    private void splitIds(List<ResourceDetailsInput> resourceIds, List<String> caisoIds, List<Long> pgeIds) {
        if (resourceIds != null) {
            for (ResourceDetailsInput resourceId : resourceIds) {
                caisoIds.add(resourceId.getResourceId());
                    caisoIds.add(resourceId.getResourceId());
            }
        }
    }

    private ResourceDetailsRegistrationId populateResourceDetailsRegistrationId(RegistrationSubmissionStatus registration) {

        final ResourceDetailsRegistrationId resourceDetailsRegistrationId = new ResourceDetailsRegistrationId();
        resourceDetailsRegistrationId.setEndDate(registration.getActiveEndDate());
        resourceDetailsRegistrationId.setStartDate(registration.getActiveStartDate());
        resourceDetailsRegistrationId.setStatus(registration.getRegistrationStatus().toString());
        resourceDetailsRegistrationId.setCaisoRegistrationId(registration.getIsoRegistrationId());
        resourceDetailsRegistrationId.setPgeRegistrationId(registration.getId().toString());
        final ArrayList<ResourceDetailsSAId> saIds = new ArrayList<>();
        for (LocationSubmissionStatus locationSubmissionStatus : registration.getLocations()) {
            ResourceDetailsSAId resourceDetailsSAId = new ResourceDetailsSAId();
            BaseServiceAgreement serviceAgreement = locationSubmissionStatus.getProgramServiceAgreementEnrollment().getServiceAgreement();
            resourceDetailsSAId.setSaId(serviceAgreement.getServiceAgreementId());
            resourceDetailsSAId.setSaUUID(serviceAgreement.getSaUuid());
            final ArrayList<ResourceDetailsSPId> spIds = new ArrayList<>();
            for (AgreementPointMap agreementPointMap : serviceAgreement.getAgreementPointMaps()) {
                ResourceDetailsSPId resourceDetailsSPId = new ResourceDetailsSPId();
                ServicePoint servicePoint = (ServicePoint) agreementPointMap.getServicePoint();
                resourceDetailsSPId.setSpId(servicePoint.getServicePointId());
                resourceDetailsSPId.setVoltageIndicator(servicePoint.getServiceVoltageClass());
                spIds.add(resourceDetailsSPId);
            }
            resourceDetailsSAId.setSpIds(spIds);
            saIds.add(resourceDetailsSAId);
        }
        resourceDetailsRegistrationId.setSaIds(saIds);
        return resourceDetailsRegistrationId;
    }
}
