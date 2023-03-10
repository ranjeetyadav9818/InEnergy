package com.inenergis.model.adapter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inenergis.entity.Account;
import com.inenergis.entity.AgreementPointMapPK;
import com.inenergis.entity.Premise;
import com.inenergis.model.ServiceAgreementEligibility;
import com.inenergis.util.FslRange;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by egamas on 12/10/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceAgreementEligibilityAdapter {

    private BigDecimal firmServiceLevel;
    private String thirdPartyName;
    private boolean eligible;
    private String ineligibleReason;

    private String accountId;
    private String uuid;
    private String customerName;
    private String businessName;
    private String serviceAddress1;
    private String serviceAddress2;
    private String city;
    private String state;
    private String zip;
    private String meterBadgeNumber;
    private List<AggregatorAdapter> aggregators;
    private AggregatorAdapter preselectedAggregator;
    private AgreementPointMapPK agreementPointMapPK;

    //To be set after calling viewEnroll
    private FslRange fslRange;
    private List<EnrollmentAttributeAdapter> enrollmentAttributeAdapters;


    @JsonIgnore
    private ServiceAgreementEligibility serviceAgreementEligibility;

    public static ServiceAgreementEligibilityAdapter build(ServiceAgreementEligibility saEligibility) {
        final ServiceAgreementEligibilityAdapterBuilder builder = builder()
                .serviceAgreementEligibility(saEligibility)
                .firmServiceLevel(saEligibility.getFirmServiceLevel())
                .thirdPartyName(saEligibility.getThirdPartyName())
                .eligible(saEligibility.isEligibile())
                .ineligibleReason(saEligibility.getIneligibleReason());

        if (saEligibility.getAgreementPointMap() != null &&
                saEligibility.getAgreementPointMap().getServiceAgreement() != null &&
                saEligibility.getAgreementPointMap().getServiceAgreement().getAccount() != null) {
            final Account account = saEligibility.getAgreementPointMap().getServiceAgreement().getAccount();
            builder.accountId(account.getAccountId());
            if (account.getPerson() != null) {
                builder.customerName(account.getPerson().getCustomerName());
                builder.businessName(account.getPerson().getBusinessName());
            }
        }
        if (saEligibility.getAgreementPointMap() != null && saEligibility.getAgreementPointMap().getServiceAgreement() != null) {
            builder.uuid(saEligibility.getAgreementPointMap().getServiceAgreement().getSaUuid());
        }
        if (saEligibility.getAgreementPointMap() != null && saEligibility.getAgreementPointMap().getServicePoint() != null &&
                saEligibility.getAgreementPointMap().getServicePoint().getPremise() != null) {
            final Premise premise = saEligibility.getAgreementPointMap().getServicePoint().getPremise();
            builder.serviceAddress1(premise.getServiceAddress1());
            builder.serviceAddress2(premise.getServiceAddress2());
            builder.city(premise.getServiceCityUpr());
            builder.state(premise.getServiceState());
            builder.zip(premise.getServicePostal());
        }
        if (saEligibility.getAgreementPointMap() != null && saEligibility.getAgreementPointMap().getId() != null) {
            builder.agreementPointMapPK(saEligibility.getAgreementPointMap().getId());
        }
        if (saEligibility.getAgreementPointMap() != null && saEligibility.getAgreementPointMap().getServicePoint() != null &&
                saEligibility.getAgreementPointMap().getServicePoint().getMeter() != null) {
            builder.meterBadgeNumber(saEligibility.getAgreementPointMap().getServicePoint().getMeter().getBadgeNumber());
        }
        if (saEligibility.getAggregator() != null) {
            builder.preselectedAggregator(AggregatorAdapter.builder().name(saEligibility.getAggregator().getName()).id(saEligibility.getAggregator().getId()).build());
        }
        return builder.build();
    }


}
