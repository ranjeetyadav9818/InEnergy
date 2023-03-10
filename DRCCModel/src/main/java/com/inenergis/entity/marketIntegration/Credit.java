package com.inenergis.entity.marketIntegration;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.AmountType;
import com.inenergis.entity.genericEnum.EnergyContractCreditType;
import com.inenergis.entity.genericEnum.FeeUnitOfMeasure;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString(exclude = "energyContract")
@Entity
@Table(name = "ENERGY_CONTRACT_CREDIT")
public class Credit extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "ENERGY_CONTRACT_ID")
    private EnergyContract energyContract;

    @Column(name = "CREDIT_TYPE")
    @Enumerated(EnumType.STRING)
    private EnergyContractCreditType creditType;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "AMOUNT_TYPE")
    @Enumerated(EnumType.STRING)
    private AmountType amountType;

    @Column(name = "UNIT_OF_MEASURE")
    @Enumerated(EnumType.STRING)
    private FeeUnitOfMeasure unitOfMeasure;

    @Column(name = "DATE_FROM")
    private Date dateFrom;

    @Column(name = "DATE_TO")
    private Date dateTo;

    public Credit() {
    }

    public Credit(EnergyContract energyContract) {
        this.energyContract = energyContract;
    }
}