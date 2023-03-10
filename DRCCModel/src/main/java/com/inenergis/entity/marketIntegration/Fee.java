package com.inenergis.entity.marketIntegration;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.AmountType;
import com.inenergis.entity.genericEnum.EnergyContractFeeType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(exclude = "energyContract")
@Entity
@Table(name = "ENERGY_CONTRACT_FEE")
public class Fee extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "ENERGY_CONTRACT_ID")
    private EnergyContract energyContract;

    @Column(name = "FEE_TYPE")
    @Enumerated(EnumType.STRING)
    private EnergyContractFeeType feeCategory;

    @Column(name = "AMOUNT_TYPE")
    @Enumerated(EnumType.STRING)
    private AmountType amountType;

    @OneToMany(mappedBy = "fee", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeeDetail> feeDetailList;

    public Fee() {
    }

    public Fee(EnergyContract energyContract) {
        this.energyContract = energyContract;
        this.feeDetailList = new ArrayList<>();
    }
}