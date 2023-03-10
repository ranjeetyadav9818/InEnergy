package com.inenergis.util.soap;

import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationAction {
    private RegistrationSubmissionStatus registration;
    private String action;
}