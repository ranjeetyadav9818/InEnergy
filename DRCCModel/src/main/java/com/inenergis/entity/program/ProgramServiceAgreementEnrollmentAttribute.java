package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.ServiceAgreement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@Table(name = "PROGRAM_SA_ENROLLMENT_ATTRIBUTE")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgramServiceAgreementEnrollmentAttribute extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "SA_ENROLLMENT_ID")
    private ProgramServiceAgreementEnrollment enrollment;
    @Column(name = "_KEY")
    private String key;
    @Column(name = "_VALUE")
    private String value;

}