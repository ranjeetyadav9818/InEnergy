package com.caiso.soa.custom.responses;

import lombok.Getter;
import lombok.Setter;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;


@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DRRegistrations_attachmentResponse", namespace = "http://www.caiso.com/soa/DRRegistrationData_v1.xsd")
public class RetrieveRegistrationsInternalResponse {

    @XmlElement(name = "DRRegistrations_attachment", namespace = "http://www.caiso.com/soa/DRRegistrationData_v1.xsd")
    @XmlMimeType("application/octetstream")
    private DataHandler attachment;

}