package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.ChargesFeeComparator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

@Getter
@Setter
@Entity
@Table(name = "CREDIT_DISCOUNT_FEE_COMPARISON")
@NoArgsConstructor
public class CreditsDiscountsFeeComparison extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "CREDIT_DISCOUNT_ID")
    private CreditDiscount creditDiscount;

    @ManyToOne
    @JoinColumn(name = "CREDIT_DISCOUNT_FEE_ONE_ID")
    private CreditDiscountFee creditDiscountFeeOne;

    @ManyToOne
    @JoinColumn(name = "CREDIT_DISCOUNT_FEE_TWO_ID")
    private CreditDiscountFee creditDiscountFeeTwo;

    @Column(name = "CREDIT_DISCOUNT_FEE_COMPARATOR")
    @Enumerated(EnumType.STRING)
    private ChargesFeeComparator comparator;

    @Transient
    private String feeOneUUID;
    @Transient
    private String feeTwoUUID;

    public CreditsDiscountsFeeComparison(CreditDiscount creditDiscount) {
        this.creditDiscount = creditDiscount;
    }

    public void setFeeOneUUID(String feeOneUUID) {
        this.feeOneUUID = feeOneUUID;
        creditDiscountFeeOne = findCreditDiscount(feeOneUUID);
    }

    public void setFeeTwoUUID(String feeTwoUUID) {
        this.feeTwoUUID = feeTwoUUID;
        creditDiscountFeeTwo = findCreditDiscount(feeTwoUUID);
    }

    private CreditDiscountFee findCreditDiscount(String uuid) {
        for (CreditDiscountFee creditDiscountFee : creditDiscount.getFees()) {
            if (creditDiscountFee.getUuid().equals(uuid)) {
                return creditDiscountFee;
            }
        }
        return null;
    }

    @PostLoad
    public void onLoad(){
        if (creditDiscountFeeOne != null) {
            feeOneUUID = creditDiscountFeeOne.getUuid();
        }
        if (creditDiscountFeeTwo != null) {
            feeTwoUUID = creditDiscountFeeTwo.getUuid();
        }
    }
}