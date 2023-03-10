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
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

@Getter
@Setter

@ToString(exclude = "profile")
@Entity
@Table(name = "PROGRAM_ENROLMENT_NOTIFICATIONS")
public class ProgramEnrollmentNotification extends IdentifiableEntity{

    @ManyToOne
    @JoinColumn(name = "PROGRAM_PROFILE_ID")
    private ProgramProfile profile;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_ENROLLERS_ID")
    private ProgramEnroller enroller;


    @Column(name = "APPLICATION_RECEIPT")
    private String receipt;

    @Column(name = "ENROLMENT_STATUS")
    private String status;

    @Column(name = "FINAL_DECISION")
    private String finalDecission;

    @Transient
    private String enrollerString;

    public void assignEnrollers(){
        for (ProgramEnroller programEnroller : profile.getEnrollers()) {
            if(programEnroller.getName().equals(enrollerString)){
                enroller = programEnroller;
                return;
            }
        }
        enroller = null;
    }

    @PostLoad
    public void onLoad(){
        if(enroller!=null){
            enrollerString = enroller.getName();
        }
    }
}