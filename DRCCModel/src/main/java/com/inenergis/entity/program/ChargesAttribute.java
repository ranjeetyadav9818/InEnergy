package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(of = {"category", "option", "serviceType", "equipmentType", "waiveable"})
@Entity
@Table(name = "CHARGES_ATTRIBUTE")
@NoArgsConstructor
public class ChargesAttribute extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "RATE_PLAN_PROFILE_ID")
    private RatePlanProfile ratePlanProfile;

    @Column(name = "CATEGORY")
    @Enumerated(EnumType.STRING)
    private ChargesCategory category;

    @Column(name = "CHARGE_OPTION")
    @Enumerated(EnumType.STRING)
    private ChargesOption option;

    @Column(name = "SERVICE_TYPE")
    @Enumerated(EnumType.STRING)
    private ChargesServiceType serviceType;

    @Column(name = "EQUIPMENT_TYPE")
    @Enumerated(EnumType.STRING)
    private ChargesEquipmentType equipmentType;

    @Column(name = "WAIVEABLE")
    private boolean waiveable;

    @OneToMany(mappedBy = "chargesAttribute", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChargesFee> fees;

    @OneToMany(mappedBy = "chargesAttribute", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChargesFeeComparison> chargesFeeComparisons;

    public ChargesAttribute(RatePlanProfile selectedProfile) {
        super();
        ratePlanProfile = selectedProfile;
        fees = new ArrayList<>();
    }
}