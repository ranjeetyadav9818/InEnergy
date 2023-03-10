package com.inenergis.util;

import com.inenergis.entity.*;
import com.inenergis.entity.genericEnum.MeterType;
import com.inenergis.entity.genericEnum.Season;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramDemand;
import com.inenergis.entity.program.ProgramEligibilityCustomerType;
import com.inenergis.entity.program.ProgramEligibilityPremiseType;
import com.inenergis.entity.program.ProgramEligibilityRateSchedule;
import com.inenergis.entity.program.ProgramEligibilitySaStatus;
import com.inenergis.entity.program.ProgramEnroller;
import com.inenergis.entity.program.ProgramEquipment;
import com.inenergis.entity.program.ProgramMultipleParticipation;
import com.inenergis.entity.program.ProgramProfile;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.model.EligibilityCheck;
import com.inenergis.model.EligibilityResult;
import com.inenergis.model.PeakDemand;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.common.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.inenergis.entity.program.EnrollerSource.DIRECT_ENROLL;

public class EligibilityChecker {

    private static final String ELIGIBLE = "Eligible";

    public static void checkLifeSupport(ProgramProfile profile, EligibilityResult result, BaseServiceAgreement sa) {
        if (StringUtils.isNotEmpty(profile.getLifeSupport())) {
            final EligibilityCheck check = EligibilityCheck.builder().attribute("Life support").value("" + sa.isLifeSupportInd()).eligible(true).build();
            boolean eligible = profile.getLifeSupport().equalsIgnoreCase(ELIGIBLE);
            if (eligible != sa.isLifeSupportInd()) {
                assignError(result, check, "Life support");
            }
            result.getChecks().add(check);
        }
    }

    public static void checkMedicalBaseline(ProgramProfile profile, EligibilityResult result, BaseServiceAgreement sa) {
        if (StringUtils.isNotEmpty(profile.getMedicalBaseline())) {
            final EligibilityCheck check = EligibilityCheck.builder().attribute("Medical Baseline").value("" + sa.isMedicalBaselineInd()).eligible(true).build();
            boolean eligible = profile.getMedicalBaseline().equalsIgnoreCase(ELIGIBLE);
            if (eligible != sa.isMedicalBaselineInd()) {
                assignError(result, check, "Medical Baseline");
            }
            result.getChecks().add(check);
        }
    }

    public static void checkThirdPartyDRP(ProgramProfile profile, EligibilityResult result, BaseServiceAgreement sa) {
        if (StringUtils.isNotEmpty(profile.getThirdPartyDRP())) {
            final EligibilityCheck check = EligibilityCheck.builder().attribute("3rd party DRP").value(sa.getHas3rdPartyDrp()).eligible(true).build();
            boolean eligible = profile.getThirdPartyDRP().equalsIgnoreCase(ELIGIBLE);
            if (eligible != (sa.getHas3rdPartyDrp() != null)) {
                assignError(result, check, "3rd party DRP");
            }
            result.getChecks().add(check);
        }
    }

    public static void checkPremiseTypes(ProgramProfile profile, EligibilityResult result, BaseServicePoint sp) {
        final EligibilityCheck check = EligibilityCheck.builder().attribute("Premise type").value(sp.getPremise().getPremiseType()).eligible(true).build();
        ProgramEligibilityPremiseType premiseTypeToCheck = new ProgramEligibilityPremiseType();
        premiseTypeToCheck.setPremiseType(sp.getPremise().getPremiseType());
        if (!CollectionUtils.isEmpty(profile.getPremiseTypes())) {
            if (!profile.getPremiseTypes().contains(premiseTypeToCheck)) {
                assignError(result, check, "Premise type");
            }
        } else {
            assignError(result, check, "Premise type");
        }
        result.getChecks().add(check);
    }

    private static void assignError(EligibilityResult result, EligibilityCheck check, String e) {
        result.getErrors().add(e);
        check.setEligible(false);
    }

    public static void checkRateSchedules(ProgramProfile profile, EligibilityResult result, BaseServiceAgreement sa) {
        final EligibilityCheck check = EligibilityCheck.builder().attribute("Rate schedules").value(sa.getRateSchedule()).eligible(true).build();
        ProgramEligibilityRateSchedule rateScheduleToCheck = new ProgramEligibilityRateSchedule();
        rateScheduleToCheck.setRateSchedule(sa.getRateSchedule());
        if (!CollectionUtils.isEmpty(profile.getRateSchedules())) {
            if (!profile.getRateSchedules().contains(rateScheduleToCheck)) {
                assignError(result, check, "Rate schedules");
            }
        } else {
            assignError(result, check, "Rate schedules");
        }
        result.getChecks().add(check);
    }

    public static void checkCustomerType(ProgramProfile profile, EligibilityResult result, BaseServiceAgreement sa) {
        final List<ProgramEligibilityCustomerType> profileCustomerTypes = profile.getCustomerTypes();
        if (!CollectionUtils.isEmpty(profileCustomerTypes)) {
            final String saCustomerType = sa.getCustClassCd();
            final EligibilityCheck check = EligibilityCheck.builder().attribute("Customer type").value(sa.getCustClassCd()).eligible(true).build();
            result.getChecks().add(check);
            if (saCustomerType != null) {
                ProgramEligibilityCustomerType.CustomerType customerType = ProgramEligibilityCustomerType.CustomerType.getByText(saCustomerType);
                if (customerType != null) {
                    ProgramEligibilityCustomerType custTypeToSearch = new ProgramEligibilityCustomerType();
                    custTypeToSearch.setCustomerType(customerType);
                    if (profileCustomerTypes.contains(custTypeToSearch)) {
                        return;
                    }
                }
            }
            assignError(result, check, "Customer type");
        }
    }

    public static void checkServiceType(ProgramProfile profile, EligibilityResult result, BaseServicePoint sp) {
        if (StringUtils.isNotEmpty(profile.getServiceType())) {
            final EligibilityCheck check = EligibilityCheck.builder().attribute("Service type").value(sp.getServiceType()).eligible(true).build();
            if (!profile.getServiceType().substring(0, 1).equalsIgnoreCase(sp.getServiceType())) {
                assignError(result, check, "Service type");
            }
            result.getChecks().add(check);
        }
    }

    public static void checkSaStatuses(ProgramProfile profile, EligibilityResult result, BaseServiceAgreement sa) {
        ProgramEligibilitySaStatus statusToCheck = new ProgramEligibilitySaStatus();
        statusToCheck.setStatus(sa.getSaStatus());
        final EligibilityCheck check = EligibilityCheck.builder().attribute("SA status").value(sa.getSaStatus()).eligible(true).build();
        if (!CollectionUtils.isEmpty(profile.getSaStatuses())) {
            if (!profile.getSaStatuses().contains(statusToCheck)) {
                assignError(result, check, "SA status");
            }
        } else {
            assignError(result, check, "SA status");
        }
        result.getChecks().add(check);
    }

    public static void checkEnrollerSources(ProgramProfile profile, EligibilityResult result) {
        ProgramEnroller enrollerToCheck = new ProgramEnroller();
        enrollerToCheck.setName(DIRECT_ENROLL.name());
        final EligibilityCheck check = EligibilityCheck.builder().attribute("Enroller sources").value(DIRECT_ENROLL.getLabel()).eligible(true).build();
        if (!CollectionUtils.isEmpty(profile.getEnrollers())) {
            if (!profile.getEnrollers().contains(enrollerToCheck)) {
                assignError(result, check, "Enroller sources");
            }
        } else {
            assignError(result, check, "Enroller sources");
        }
        result.getChecks().add(check);
    }

    public static void checkEligibilityForOtherEnrollments(List<ProgramServiceAgreementEnrollment> enrollments, EligibilityResult result, ProgramProfile profile) {

        for (ProgramServiceAgreementEnrollment enrollment : enrollments) {
            if (enrollment.isActivelyEnrolled()) {
                if (!CollectionUtils.isEmpty(profile.getProgramMultiParticipations())) {
                    final EligibilityCheck check = EligibilityCheck.builder().attribute("Multiple participation - " + enrollment.getProgram().getName()).value("Not enrolled").eligible(true).build();
                    checkErrorsForActiveEnrollment(result, profile, enrollment.getProgram(), check);
                    result.getChecks().add(check);
                }
            }
        }
    }

    private static void checkErrorsForActiveEnrollment(EligibilityResult result, ProgramProfile profile, Program program, EligibilityCheck check) {
        for (ProgramMultipleParticipation multipleParticipation : profile.getProgramMultiParticipations()) {
            if (CollectionUtils.isEmpty(multipleParticipation.getPrograms()) || multipleParticipation.getPrograms().contains(program)) {
                assignError(result, check, "Multiple participation - " + program.getName());
                check.setValue("Enrolled");
                return;
            }
        }
    }

    public static void checkMeterTypeEligible(ProgramProfile programProfile, BaseMeter meter, EligibilityResult result, String meterType) {
        if (programProfile.isEquipmentRequired() && !CollectionUtils.isEmpty(programProfile.getEquipments())) {
            switch (programProfile.getEquipmentInstallPriorTo()) {
                case "DAYS_5_PRIOR_TODAY":
                    LocalDateTime installDate = meter.getInstallDate().toInstant().atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toLocalDateTime();
                    if (!installDate.isBefore(LocalDateTime.now().minusDays(5))) {
                        final EligibilityCheck check = EligibilityCheck.builder().attribute("DAYS_5_PRIOR_TODAY").value(installDate.toString()).build();
                        assignError(result, check, "DAYS_5_PRIOR_TODAY - " + installDate.toString());
                        return;
                    }
                case "TODAY":
                    List<MeterType> acceptableMeterTypes = programProfile.getEquipments().stream()
                            .map(ProgramEquipment::getMeterType)
                            .map(MeterType::valueOf)
                            .collect(Collectors.toList());

                    if (meterType == null || !acceptableMeterTypes.contains(MeterType.valueOf(meterType))) {
                        final EligibilityCheck check = EligibilityCheck.builder().attribute("METER TYPE").value(meter.getConfigType()).build();
                        assignError(result, check, "METER TYPE - " + meter.getConfigType());
                        return;
                    }
                case "ENROLLMENT_DATE":
            }
        }
    }

    public static void checkDemand(ProgramProfile programProfile, List<PeakDemand> peakDemandList, EligibilityResult result) {
        Collections.sort(peakDemandList);
        Collections.reverse(peakDemandList);

        for (ProgramDemand programDemand : programProfile.getProgramDemands()) {
            int numberOfMatches = 0;

            for (int i = 0; i < programDemand.getPriorMonths() && i < peakDemandList.size(); i++) {
                PeakDemand peakDemand = peakDemandList.get(i);

                long valueWatts = peakDemand.getValueWatts(programDemand.getDemandMinType(), Season.SUMMER);
                long minValueWatts = EnergyUtil.convertToWatts(programDemand.getMinValue().longValue(), programDemand.getMinUnit());

                if (valueWatts >= minValueWatts) {
                    numberOfMatches++;
                }
            }

            if (numberOfMatches < programDemand.getTimeHorizon()) {
                final EligibilityCheck check = EligibilityCheck.builder().attribute("Demand").value("TOO SMALL").build();
                assignError(result, check, "Demand is too small");
                break;
            }
        }
    }
}
