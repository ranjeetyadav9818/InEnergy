package com.inenergis.entity.billing;

import com.inenergis.entity.BaseServicePoint;
import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.ServicePoint;
import com.inenergis.entity.program.rateProgram.RateProfileAncillaryFee;
import com.inenergis.entity.program.rateProgram.RateProfileAncillaryPercentageFee;
import com.inenergis.entity.program.rateProgram.RateProfileConsumptionFee;
import com.inenergis.entity.program.rateProgram.RateTier;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@Data
@Entity
@Table(name = "INVOICE_LINE")
@NoArgsConstructor
public class InvoiceLine extends IdentifiableEntity {
    @Column(name = "KWH")
    private BigDecimal kwh;

    // Gas Changes

    @Column(name = "THERMS")
    private BigDecimal therms;

    @Column(name = "PRICE_PER_THERMS")
    private BigDecimal pricePerTherms;

    @Column(name = "CONCEPT")
    private String concept;

    @ManyToOne
    @JoinColumn(name = "RATE_TIER_ID")
    private RateTier tier;

    @Column(name = "PRICE_PER_WATT")
    private BigDecimal pricePerWatt;

    @Column(name = "TOTAL")
    private Long total; //cents, pence...

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private ChargeType type;

    @ManyToOne
    @JoinColumn(name = "INVOICE_ID")
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "SERVICE_POINT_ID")
    private BaseServicePoint servicePoint;

    @ManyToOne
    @JoinColumn(name = "RATE_PROFILE_CONS_FEE_ID")
    private RateProfileConsumptionFee rateProfileConsumptionFee;

    @ManyToOne
    @JoinColumn(name = "RATE_PROFILE_ANC_FEE_ID")
    private RateProfileAncillaryFee rateProfileAncillaryFee;

    @ManyToOne
    @JoinColumn(name = "RATE_PROFILE_ANC_PERC_FEE_ID")
    private RateProfileAncillaryPercentageFee rateProfileAncillaryPercentageFee;

    public enum ChargeType {
        TIER, RATE
    }

    public String toString() {
        return type + " " + kwh + " * " + pricePerWatt + " = " + total;
    }

}

