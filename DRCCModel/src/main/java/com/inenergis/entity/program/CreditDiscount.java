package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.CreditDiscountFrequency;
import com.inenergis.entity.genericEnum.DiscountCategory;
import com.inenergis.entity.genericEnum.DiscountType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Entity
@Table(name = "CREDIT_DISCOUNT")
@NoArgsConstructor
public class CreditDiscount extends IdentifiableEntity {

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    DiscountType type;

    @Column(name = "CATEGORY")
    @Enumerated(EnumType.STRING)
    DiscountCategory category;

    @Column(name = "FREQUENCY")
    @Enumerated(EnumType.STRING)
    private CreditDiscountFrequency frequency;

    @ManyToOne
    @JoinColumn(name = "SEASON_ID")
    private SeasonCalendar season;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_SEASON_ID")
    private ProgramSeason programSeason;

    @Column(name = "EQUIPMENT_TYPE")
    @Enumerated(EnumType.STRING)
    private ChargesEquipmentType equipmentType; //TODO RENAME

    @ManyToOne
    @JoinColumn(name = "RATE_PLAN_PROFILE_ID")
    private RatePlanProfile ratePlanProfile;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_PROFILE_ID")
    private ProgramProfile programProfile;

    @OneToMany(mappedBy = "creditDiscount", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CreditDiscountFee> fees;

    @OneToMany(mappedBy = "creditDiscount", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CreditsDiscountsFeeComparison> creditsDiscountsFeeComparisons;

    public CreditDiscount(RatePlanProfile selectedProfile) {
        super();
        ratePlanProfile = selectedProfile;
        fees = new ArrayList<>();
    }

    public CreditDiscount(ProgramProfile selectedProfile) {
        super();
        programProfile = selectedProfile;
        fees = new ArrayList<>();
    }
}
