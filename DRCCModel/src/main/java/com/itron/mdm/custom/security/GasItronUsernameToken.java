package com.itron.mdm.custom.security;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ItronUsernameToken")
public class GasItronUsernameToken {

    @XmlElement(name = "wsse:Username")
    private String username;
    @XmlElement(name = "wsse:Password")
    private String password;
}
