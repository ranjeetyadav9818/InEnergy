
package com.caiso.ads.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;


/**
 * <p>Java class for DispatchBatchType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DispatchBatchType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="marketID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="batchStatus" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="batchReceived" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="batchSent" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="batchExpires" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="batchType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="startTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dispatchMode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="bindingFlag" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="revisionNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="contingencyType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="pathExclusion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="instructions" type="{http://ads.caiso.com}instructionsType"/>
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
@XmlType(name = "DispatchBatchType", namespace = "http://ads.caiso.com", propOrder = {
    "marketID",
    "batchStatus",
    "batchReceived",
    "batchSent",
    "batchExpires",
    "batchType",
    "startTime",
    "dispatchMode",
    "bindingFlag",
    "revisionNo",
    "contingencyType",
    "pathExclusion",
    "instructions"
})
@Getter
@Setter
public class DispatchBatchType {

    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String marketID;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String batchStatus;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected XMLGregorianCalendar batchReceived;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected XMLGregorianCalendar batchSent;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String batchExpires;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String batchType;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String startTime;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String dispatchMode;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String bindingFlag;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String revisionNo;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String contingencyType;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String pathExclusion;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected InstructionsType instructions;
    @XmlAttribute(name = "batchUID")
    protected String batchUID;
}
