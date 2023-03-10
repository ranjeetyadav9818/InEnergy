
package com.caiso.ads.api.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for trajectoryComplianceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="trajectoryComplianceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="resourceId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="startTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mwh" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="complFlag" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="configurationId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attribute name="complianceUID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "trajectoryComplianceType", namespace = "http://ads.caiso.com", propOrder = {
    "resourceId",
    "startTime",
    "mwh",
    "complFlag",
    "configurationId"
})
public class TrajectoryComplianceType {

    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String resourceId;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String startTime;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String mwh;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String complFlag;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String configurationId;
    @XmlAttribute(name = "complianceUID")
    protected String complianceUID;

    /**
     * Gets the value of the resourceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     * Sets the value of the resourceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResourceId(String value) {
        this.resourceId = value;
    }

    /**
     * Gets the value of the startTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Sets the value of the startTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartTime(String value) {
        this.startTime = value;
    }

    /**
     * Gets the value of the mwh property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMwh() {
        return mwh;
    }

    /**
     * Sets the value of the mwh property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMwh(String value) {
        this.mwh = value;
    }

    /**
     * Gets the value of the complFlag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComplFlag() {
        return complFlag;
    }

    /**
     * Sets the value of the complFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComplFlag(String value) {
        this.complFlag = value;
    }

    /**
     * Gets the value of the configurationId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConfigurationId() {
        return configurationId;
    }

    /**
     * Sets the value of the configurationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConfigurationId(String value) {
        this.configurationId = value;
    }

    /**
     * Gets the value of the complianceUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComplianceUID() {
        return complianceUID;
    }

    /**
     * Sets the value of the complianceUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComplianceUID(String value) {
        this.complianceUID = value;
    }

}
