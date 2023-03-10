package com.inenergis.model.adapter;

import com.inenergis.entity.Meter;
import com.inenergis.entity.Premise;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.ServicePoint;
import com.inenergis.entity.marketIntegration.EnergyContract;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.entity.program.RatePlanEnrollment;
import com.inenergis.entity.program.RatePlanProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by egamas on 25/09/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortalServiceAgreementAdapter {

    private String serviceAgreementId;
    private String phone;
    private Date startDate;
    private Date endDate;
    private String mailingAddress1;
    private String mailingAddress2;
    private String mailingCityUpr;
    private String mailingPostal;
    private String mailingState;

    //account
    private String accountId;

    //person
    private String personId;
    private String businessName;
    private String customerName;
    private String phoneExtension;
    private String businessOwner;

    //Collections

    private List<AgreementPointMapAdapter> servicePoints;

    public static PortalServiceAgreementAdapter build(ServiceAgreement sa) {


        return PortalServiceAgreementAdapter.builder()

                .serviceAgreementId(sa.getServiceAgreementId())
                .phone(sa.getPhone())
                .startDate(sa.getStartDate())
                .endDate(sa.getEndDate())
                .mailingAddress1(sa.getMailingAddress1())
                .mailingAddress2(sa.getMailingAddress2())
                .mailingCityUpr(sa.getMailingCityUpr())
                .mailingPostal(sa.getMailingPostal())
                .mailingState(sa.getMailingState())

                .accountId(sa.getAccount().getAccountId())

                .personId(sa.getAccount().getPerson().getPersonId())
                .businessName(sa.getAccount().getPerson().getBusinessName())
                .customerName(sa.getAccount().getPerson().getCustomerName())
                .phoneExtension(sa.getAccount().getPerson().getPhoneExtension())
                .businessOwner(sa.getAccount().getPerson().getBusinessOwner())

                .servicePoints(buildAgreementPointMaps(sa))

                .build();
    }

    private static List<EnergyContractAdapter> buildEnergyContracts(ServiceAgreement sa) {
        final List<EnergyContract> contracts = sa.getEnergyContracts();
        final EnergyContractAdapter.EnergyContractAdapterBuilder builder = EnergyContractAdapter.builder();
        if (CollectionUtils.isNotEmpty(contracts)) {

            return contracts.stream().map(c -> builder
                    .id(c.getId())
                    .name(c.getName())
                    .type(c.getType().getName())
                    .lastUpdate(c.getLastUpdate())
                    .agreementStartDate(c.getAgreementStartDate())
                    .agreementEndDate(c.getAgreementEndDate())
                    .paymentFrequency(c.getPaymentFrequency() != null ? c.getPaymentFrequency().getName() : null)
                    .status(c.getStatus()).build()
            ).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public static List<RatePlanAdapter> buildRatePlans(ServiceAgreement sa) {
        final RatePlanAdapter.RatePlanAdapterBuilder builder = RatePlanAdapter.builder();
        final List<RatePlanEnrollment> enrollments = sa.getRatePlanEnrollments();
        if (CollectionUtils.isNotEmpty(enrollments)) {
            return enrollments.stream().map(e -> {
                        final RatePlanProfile activeProfile = e.getRatePlan().getActiveProfile();
                        builder
                                .id(e.getId())
                                .name(e.getRatePlan().getName())
                                .sector(e.getRatePlan().getSector().getName())
                                .status(e.getStatus())
                                .startDate(e.getStartDate());

                        if (activeProfile != null) {
                            builder
                                    .profileName(activeProfile.getName())
                                    .effectiveStartDate(activeProfile.getEffectiveStartDate())
                                    .designType(activeProfile.getDesignType() != null ? activeProfile.getDesignType().getName() : null)
                                    .tierType(activeProfile.getTierType() != null ? activeProfile.getTierType().getName() : null)
                                    .serviceType(activeProfile.getServiceType() != null ? activeProfile.getServiceType().getLabel() : null)
                                    .rateScheduleTitle(activeProfile.getRateScheduleTitle());
                        }
                        return builder.build();
                    }

            ).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private static List<ProgramAdapter> buildPrograms(ServiceAgreement sa) {
        final ProgramAdapter.ProgramAdapterBuilder builder = ProgramAdapter.builder();
        final List<ProgramServiceAgreementEnrollment> enrollments = sa.getEnrollments();
        if (CollectionUtils.isNotEmpty(enrollments)) {
            return enrollments.stream().map(e -> builder
                    .id(e.getProgram().getId())
                    .name(e.getProgram().getName())
                    .active(e.getProgram().isActive())
                    .programType(e.getProgram().getProgramType() != null ? e.getProgram().getProgramType().getLabel() : null)
                    .build()).collect(Collectors.toList());
        }
        return new ArrayList<>();

    }

    private static List<AgreementPointMapAdapter> buildAgreementPointMaps(ServiceAgreement sa) {
        if (CollectionUtils.isNotEmpty(sa.getAgreementPointMaps())) {
            return sa.getAgreementPointMaps().stream().map(apm -> {
                final AgreementPointMapAdapter.AgreementPointMapAdapterBuilder builder = AgreementPointMapAdapter.builder()
                        .endDate(apm.getEndDate())
                        .startDate(apm.getStartDate());
                final ServicePoint sp = apm.getServicePoint();
                if (sp != null) {
                    builder
                            .servicePointId(sp.getServicePointId())
                            .customerMdmaCompanyName(sp.getCustomerMdmaCompanyName())
                            .customerMspCompanyName(sp.getCustomerMspCompanyName())
                            .serviceType(sp.getServiceType())
                            .latitude(sp.getLatitude())
                            .longitude(sp.getLongitude());

                    final Premise premise = sp.getPremise();
                    if (premise != null) {
                        builder
                                .premiseId(premise.getPremiseId())
                                .serviceAddress1(premise.getServiceAddress1())
                                .serviceAddress2(premise.getServiceAddress2())
                                .serviceCityUpr(premise.getServiceCityUpr())
                                .servicePostal(premise.getServicePostal())
                                .serviceState(premise.getServiceState())
                                .baseLineChar(premise.getBaseLineChar())
                                .county(premise.getCounty())
                                .premiseType(premise.getPremiseType());
                    }

                    final Meter meter = sp.getMeter();
                    if (meter != null) {
                        builder
                                .meterId(meter.getMeterId())
                                .badgeNumber(meter.getBadgeNumber())
                                .smStatus(meter.getSmStatus())
                                .installDate(meter.getInstallDate())
                                .uninstallDate(meter.getUninstallDate())
                                .readFreq(meter.getReadFreq());
                    }
                }
                return builder.build();
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }


    //Skip CustomerNotificationPreferences


}
