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
@XmlType(name = "wsse:Security")
public class ItronSecurityHeader {

    @XmlElement(name = "wsse:UsernameToken")
    private ItronUsernameToken itronUsernameToken;
}
