package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "GENERAL_AVAILABILITY_APPLICABLE_VALUE")
@EqualsAndHashCode(of = "value", callSuper = false)
public class GeneralAvailabilityApplicableValue extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "GENERAL_AVAILABILITY_ID")
    private GeneralAvailability generalAvailability;

    @Column(name = "VALUE")
    private String value;

    public GeneralAvailabilityApplicableValue() {
    }

    public GeneralAvailabilityApplicableValue(GeneralAvailability generalAvailability, String value) {
        this.generalAvailability = generalAvailability;
        this.value = value;
    }
}