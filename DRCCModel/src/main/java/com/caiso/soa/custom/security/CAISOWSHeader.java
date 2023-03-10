package com.caiso.soa.custom.security;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CAISOWSHeader")
public class CAISOWSHeader {

    @XmlElement(name = "CAISOUsernameToken")
    private CAISOUsernameToken caisoUsernameToken;
}
