package com.inenergis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTokenState {
    private String access_token;
    private Long expires_in;
    private String email;
    private String name;
    private String phone;
    private String service_agreement;
}