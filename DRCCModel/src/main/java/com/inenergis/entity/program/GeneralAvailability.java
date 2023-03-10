package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.GeneralEligibilityAttributeType;
import lombok.Getter;
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
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "GENERAL_AVAILABILITY")
public class GeneralAvailability extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "RATE_PLAN_PROFILE_ID")
    private RatePlanProfile ratePlanProfile;

    @Column(name = "ATTRIBUTE_TYPE")
    @Enumerated(EnumType.STRING)
    private GeneralEligibilityAttributeType attributeType;

    @OneToMany(mappedBy = "generalAvailability", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GeneralAvailabilityApplicableValue> applicableValues;

    public GeneralAvailability() {
    }

    public GeneralAvailability(RatePlanProfile ratePlanProfile) {
        this.ratePlanProfile = ratePlanProfile;
    }
}