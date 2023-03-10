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
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "CHARGES_FEE_COMPARISON")
@NoArgsConstructor
public class ChargesFeeComparison extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "CHARGES_ATTRIBUTE_ID")
    private ChargesAttribute chargesAttribute;

    @ManyToOne
    @JoinColumn(name = "CHARGES_FEE_ONE_ID")
    private ChargesFee chargesFeeOne;

    @ManyToOne
    @JoinColumn(name = "CHARGES_FEE_TWO_ID")
    private ChargesFee chargesFeeTwo;

    @Column(name = "CHARGES_FEE_COMPARATOR")
    @Enumerated(EnumType.STRING)
    private ChargesFeeComparator comparator;

    public ChargesFeeComparison(ChargesAttribute attribute) {
        chargesAttribute = attribute;
    }
}