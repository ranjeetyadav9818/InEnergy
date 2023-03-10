package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(of = {"program", "profile"})
@Entity
@Table(name = "ELIGIBLE_PROGRAMS")
public class EligibleProgram extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "PROGRAM_ID")
    private Program program;

    @ManyToOne
    @JoinColumn(name = "PROFILE_ID")
    private ProgramProfile profile;

    @Override
    public String toString() {
        return "EligibleProgram: id + program";
    }

}
