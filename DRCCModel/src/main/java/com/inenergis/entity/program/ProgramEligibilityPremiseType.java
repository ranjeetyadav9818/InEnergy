package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Getter
@Setter
@EqualsAndHashCode(of = "strippedPremiseType")
@Entity
@Table(name = "PROGRAM_ELIG_PREMISE_TYPE")
public class ProgramEligibilityPremiseType extends IdentifiableEntity{

    @Column(name = "PREMISE_TYPE")
    private String premiseType;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_PROFILE_ID")
    private ProgramProfile profile;

    @Transient
    private final String strippedPremiseType = null; //fake field to force lombok's equals and hashcode

    @Override
    public String toString() {
        return "Premise type: " + premiseType;
    }

    public String getStrippedPremiseType(){
        if(premiseType!=null){
            return premiseType.replaceAll("[^A-Za-z0-9]","");
        }
        return null;
    }
}