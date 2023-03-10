
package com.caiso.ads.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "instructionType", namespace = "http://ads.caiso.com", propOrder = {
    "batchUID",
    "resourceId",
    "startTime",
    "endTime",
    "dot",
    "oosEnergyCode",
    "asType",
    "instructionType",
    "preGoto",
    "bidDelay",
    "minAccept",
    "acceptDot",
    "acceptStatus",
    "responder",
    "reasonCode",
    "oprAcceptDot",
    "oprAcceptStatus",
    "oprResponder",
    "oprReasonCode",
    "validated",
    "validatedBy",
    "apiValidated",
    "apiValidatedBy",
    "passIndicator",
    "revisionNumber",
    "statusCode",
    "clearedMW",
    "awardMW",
    "selfSchedMW",
    "hourlyMw",
    "rmrTestRequestor",
    "configurationId",
    "transitionFromConfigId",
    "transitionToConfigId",
    "agcFlag",
    "rmrFlag",
    "hourlyMwThreshold",
    "baseSchedule",
    "registeredIntertieFlag",
    "detail"
})
@Getter
@Setter
public class InstructionType {

    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String batchUID;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String resourceId;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected XMLGregorianCalendar startTime;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected XMLGregorianCalendar endTime;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String dot;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String oosEnergyCode;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String asType;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String instructionType;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String preGoto;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String bidDelay;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String minAccept;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String acceptDot;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String acceptStatus;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String responder;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String reasonCode;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String oprAcceptDot;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String oprAcceptStatus;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String oprResponder;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String oprReasonCode;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String validated;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String validatedBy;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String apiValidated;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String apiValidatedBy;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String passIndicator;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String revisionNumber;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String statusCode;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String clearedMW;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String awardMW;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String selfSchedMW;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String hourlyMw;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String rmrTestRequestor;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String configurationId;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String transitionFromConfigId;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String transitionToConfigId;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String agcFlag;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String rmrFlag;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String hourlyMwThreshold;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String baseSchedule;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String registeredIntertieFlag;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected DetailType detail;
    @XmlAttribute(name = "instructionUID")
    protected String instructionUID;

}
