package com.caiso.soa.custom.security;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;


@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CAISOUsernameToken")
public class CAISOUsernameToken {

    @XmlElement(name = "Username")
    private String username;
    @XmlElement(name = "Nonce")
    private String nonce;
    @XmlElement(name = "Created")
    private Date created;
}
