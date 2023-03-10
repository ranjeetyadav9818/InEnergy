
package com.caiso.ads.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for trajectoryBatchType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="trajectoryBatchType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="batchReceived" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="bindingFlag" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="batchSent" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dopList" type="{http://ads.caiso.com}dopListType"/>
 *         &lt;element name="complianceList" type="{http://ads.caiso.com}complianceListType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="batchUID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "trajectoryBatchType", namespace = "http://ads.caiso.com", propOrder = {
    "batchReceived",
    "bindingFlag",
    "batchSent",
    "dopList",
    "complianceList"
})
@Getter
@Setter
public class TrajectoryBatchType {

    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected XMLGregorianCalendar batchReceived;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String bindingFlag;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected XMLGregorianCalendar batchSent;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected DopListType dopList;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected ComplianceListType complianceList;
    @XmlAttribute(name = "batchUID")
    protected String batchUID;



}
