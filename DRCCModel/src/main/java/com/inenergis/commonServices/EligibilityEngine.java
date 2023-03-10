package com.inenergis.commonServices;

import com.inenergis.entity.*;
import com.inenergis.entity.genericEnum.CommodityType;
import com.inenergis.entity.genericEnum.ComodityType;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramProfile;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.exception.BusinessException;
import com.inenergis.model.EligibilityResult;
import com.inenergis.model.PeakDemand;
import com.inenergis.model.PeakDemandResponse;
import com.inenergis.model.ServiceAgreementEligibility;
import com.inenergis.util.EligibilityChecker;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.inenergis.util.ConstantsProviderModel.KW_PRECISION;
import static com.inenergis.util.ConstantsProviderModel.MW_PRECISION;

public class EligibilityEngine {

    public EligibilityResult isEligible(DataMappingServiceContract dataMappingServiceContract, AgreementPointMap apm, ProgramProfile profile, List<PeakDemand> peakDemandList) {
        EligibilityResult result = new EligibilityResult();
        result.setErrors(new ArrayList<>());
        result.setChecks(new ArrayList<>());

        final BaseServiceAgreement sa = apm.getServiceAgreement();
        final BaseServicePoint sp =  apm.getServicePoint();
        String meterType = dataMappingServiceContract.getDestinationBy(DataMappingType.METER_TYPE, sp.getMeter().getConfigType());

        EligibilityChecker.checkEnrollerSources(profile, result);
        EligibilityChecker.checkSaStatuses(profile, result, sa);
        EligibilityChecker.checkServiceType(profile, result, sp);
        EligibilityChecker.checkCustomerType(profile, result, sa);
        EligibilityChecker.checkRateSchedules(profile, result, sa);
        EligibilityChecker.checkPremiseTypes(profile, result, sp);
        EligibilityChecker.checkThirdPartyDRP(profile, result, sa);
        EligibilityChecker.checkMedicalBaseline(profile, result, sa);
        EligibilityChecker.checkLifeSupport(profile, result, sa);
        EligibilityChecker.checkMeterTypeEligible(profile, sp.getMeter(), result, meterType);
        EligibilityChecker.checkDemand(profile, peakDemandList, result);

        //TODO Rolling Usage flag
        //TODO Firm Service Level
        //TODO multiple participation with options

        if (CollectionUtils.isNotEmpty(sa.getEnrollments())) {
            EligibilityChecker.checkEligibilityForOtherEnrollments(sa.getEnrollments(), result, profile);
        }

        result.setEligible(result.getErrors().size() == 0);
        return result;
    }

    public boolean isCapEligible(ProgramServiceContract programServiceContract, ProgramProfile programProfile, ProgramServiceAgreementEnrollment enrollment) {
        Long averageSummerOnPeakWatt = enrollment.getServiceAgreement().getAverageSummerOnPeakWatt();
        long fslValue = ((enrollment.getCurrentFSL()==null)?0:enrollment.getCurrentFSL().getValue().longValue());
        Long plr = Math.max(0L, averageSummerOnPeakWatt - fslValue * KW_PRECISION);
        Program program = programProfile.getProgram();

        Long capValueWatt = null;


        if (program.isCapActive() && program.getCommodity().equals(ComodityType.Electricity)) {
            switch (program.getCapUnit()) {
                case MW:
                    capValueWatt = program.getCapNumber().longValue() * MW_PRECISION;
                case kW:
                    if (capValueWatt == null) {
                        capValueWatt = program.getCapNumber().longValue() * KW_PRECISION;
                    }

                    Long sum = programServiceContract.getActiveSAs(program).stream()
                            .mapToLong(e -> Math.max(0, e.getAverageSummerOnPeakWatt() - ((e.getCurrentFSL()==null)?0:e.getCurrentFSL().getValue().longValue()) * KW_PRECISION))
                            .sum();

                    if (capValueWatt <= sum + plr) {
//                        final EligibilityCheck check = EligibilityCheck.builder().attribute("Program CAP").value("FULL").build();
//                        assignError(result, check, "Program CAP is full");
                        return false;
                    }
                    break;
                case Customer:
                    if (program.getCapNumber().longValue() <= programServiceContract.getProgramNumberOfActiveSAs(program)) {
//                        final EligibilityCheck check = EligibilityCheck.builder().attribute("Program CAP").value("FULL").build();
//                        assignError(result, check, "Program CAP is full");
                        return false;
                    }
            }
        }
        else if (program.isCapActive() && program.getCommodity().equals(ComodityType.Gas)) {
            switch (program.getGasCapUnit()) {
//                case MW:
//                    capValueWatt = program.getCapNumber().longValue() * MW_PRECISION;
                case Therms:
                    if (capValueWatt == null) {
                        capValueWatt = program.getCapNumber().longValue() * KW_PRECISION;
                    }

                    Long sum = programServiceContract.getActiveSAs(program).stream()
                            .mapToLong(e -> Math.max(0, e.getAverageSummerOnPeakWatt() - ((e.getCurrentFSL()==null)?0:e.getCurrentFSL().getValue().longValue()) * KW_PRECISION))
                            .sum();

                    if (capValueWatt <= sum + plr) {
//                        final EligibilityCheck check = EligibilityCheck.builder().attribute("Program CAP").value("FULL").build();
//                        assignError(result, check, "Program CAP is full");
                        return false;
                    }
                    break;
                case Customer:
                    if (program.getCapNumber().longValue() <= programServiceContract.getProgramNumberOfActiveSAs(program)) {
//                        final EligibilityCheck check = EligibilityCheck.builder().attribute("Program CAP").value("FULL").build();
//                        assignError(result, check, "Program CAP is full");
                        return false;
                    }
            }
        }
        return true;
    }

    public PeakDemandResponse verify(String filterSAIDs, AgreementPointMapServiceContract agreementPointMapService, PeakDemandServiceCommon peakDemandService,
                                     DataMappingServiceContract dataMappingService, List<ServiceAgreementEligibility>  potentialServiceAgreements, ProgramProfile programProfile,
                                     Program program, ArrayList<String>  messages)
            throws BusinessException {

        PeakDemandResponse peakDemandResponse;
        List<String> saIds = Arrays.asList(filterSAIDs.trim().split(","));

        List<AgreementPointMap> agreementPointMaps = agreementPointMapService.getListByIds(saIds);

        List<BaseServiceAgreement> serviceAgreements = agreementPointMaps.stream()
                .map(AgreementPointMap::getServiceAgreement)
                .collect(Collectors.toList());

        peakDemandResponse = peakDemandService.getPeakDemandData(serviceAgreements);

        for (AgreementPointMap apm : agreementPointMaps) {

            if (!peakDemandResponse.containsServiceAgreement(apm.getServiceAgreement()) ) {

                messages.add("It was not possible to get data for Service Agreement ID "+apm.getServiceAgreement().getServiceAgreementId());

                continue;
            }

            potentialServiceAgreements.add(createPotentialServiceAgreement(dataMappingService,peakDemandResponse,programProfile,apm, program));
        }

        Map<BaseServiceAgreement, ServiceAgreementEligibility> mergedServiceAgreements = new HashMap<>();
        for (ServiceAgreementEligibility saEligibility : potentialServiceAgreements) {

            BaseServiceAgreement serviceAgreement = saEligibility.getAgreementPointMap().getServiceAgreement();
            if (mergedServiceAgreements.get(serviceAgreement) == null) {

                mergedServiceAgreements.put(serviceAgreement, saEligibility);

            } else {
                if (!saEligibility.isEligibile()) {

                    mergedServiceAgreements.get(serviceAgreement).setEligibile(false);

                }
                if (saEligibility.getIneligibleReason() != null) {

                    StringBuilder reason = new StringBuilder(mergedServiceAgreements.get(serviceAgreement).getIneligibleReason());
                    if (StringUtils.isNotEmpty(reason)) {

                        reason.append("; ");
                    }
                    reason.append(saEligibility.getIneligibleReason());

                    mergedServiceAgreements.get(serviceAgreement).setIneligibleReason(reason.toString());

                }
            }
        }

        potentialServiceAgreements.clear();
        for (Map.Entry<BaseServiceAgreement, ServiceAgreementEligibility> entry : mergedServiceAgreements.entrySet()) {

            potentialServiceAgreements.add(entry.getValue());
        }
        return peakDemandResponse;
    }

    private ServiceAgreementEligibility createPotentialServiceAgreement(DataMappingServiceContract dataMappingService, PeakDemandResponse peakDemandResponse, ProgramProfile currentProfile, AgreementPointMap apm, Program program) {

        apm.getServiceAgreement().setAverageSummerOnPeakWatt(peakDemandResponse.getAverageSummerOnPeakWatt( apm.getServiceAgreement()));
        EligibilityResult eligibilityResult = isEligible(dataMappingService, apm, currentProfile, peakDemandResponse.getPeakDemandGenericList( apm.getServiceAgreement()));

        return ServiceAgreementEligibility.builder().
                agreementPointMap(apm).
                ineligibleReason(getIneligibleReasons(eligibilityResult.getErrors())).
                eligibile(eligibilityResult.isEligible()).
                currentProgramEnrolled(getCurrentProgramEnrolled(apm,program)).build();

    }


    private String getIneligibleReasons(List<String> errors) {
        return errors.stream().collect(Collectors.joining(", "));
    }

    private ProgramServiceAgreementEnrollment getCurrentProgramEnrolled(AgreementPointMap apm, Program program) {

        final List<ProgramServiceAgreementEnrollment> enrollments = (apm.getServiceAgreement()).getEnrollments();
        if (CollectionUtils.isNotEmpty(enrollments)) {
            for (ProgramServiceAgreementEnrollment enrollment : enrollments) {
                if (enrollment.isActivelyEnrolledOrInProgress() && enrollment.getProgram().equals(program)) {
                    return enrollment;
                }
            }
        }
        return null;
    }
}