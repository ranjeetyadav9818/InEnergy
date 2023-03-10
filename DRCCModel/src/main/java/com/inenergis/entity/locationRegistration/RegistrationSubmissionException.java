package com.inenergis.entity.locationRegistration;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@DiscriminatorValue("Registration")
public class RegistrationSubmissionException extends IsoException {

    @ManyToOne
    @JoinColumn(name = "REGISTRATION_ID")
    RegistrationSubmissionStatus registrationSubmissionStatus;


}
