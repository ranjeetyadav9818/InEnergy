package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@EqualsAndHashCode(of = "name")
@Entity
@Table(name = "PROGRAM_ENROLMENT_ATTRIBUTES")
public class ProgramEnrollmentAttribute extends IdentifiableEntity{

    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_PROFILE_ID")
    private ProgramProfile profile;

    @Override
    public String toString() {
        return "Enroll. attribute: " + name;
    }
}