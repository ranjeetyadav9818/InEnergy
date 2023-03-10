
package com.caiso.soa.requestdrregistrationdata_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RegistrationRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RegistrationRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="requestType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DemandResponseRegistration" type="{http://www.caiso.com/soa/RequestDRRegistrationData_v1.xsd#}DemandResponseRegistration_Full" minOccurs="0"/&gt;
 *         &lt;element name="rangePeriod" type="{http://www.caiso.com/soa/RequestDRRegistrationData_v1.xsd#}DateTimeInterval" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegistrationRequest", propOrder = {
    "requestType",
    "demandResponseRegistration",
    "rangePeriod"
})
public class RegistrationRequest {

    protected String requestType;
    @XmlElement(name = "DemandResponseRegistration")
    protected DemandResponseRegistrationFull demandResponseRegistration;
    protected DateTimeInterval rangePeriod;

    /**
     * Gets the value of the requestType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestType() {
        return requestType;
    }

    /**
     * Sets the value of the requestType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestType(String value) {
        this.requestType = value;
    }

    /**
     * Gets the value of the demandResponseRegistration property.
     * 
     * @return
     *     possible object is
     *     {@link DemandResponseRegistrationFull }
     *     
     */
    public DemandResponseRegistrationFull getDemandResponseRegistration() {
        return demandResponseRegistration;
    }

    /**
     * Sets the value of the demandResponseRegistration property.
     * 
     * @param value
     *     allowed object is
     *     {@link DemandResponseRegistrationFull }
     *     
     */
    public void setDemandResponseRegistration(DemandResponseRegistrationFull value) {
        this.demandResponseRegistration = value;
    }

    /**
     * Gets the value of the rangePeriod property.
     * 
     * @return
     *     possible object is
     *     {@link DateTimeInterval }
     *     
     */
    public DateTimeInterval getRangePeriod() {
        return rangePeriod;
    }

    /**
     * Sets the value of the rangePeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateTimeInterval }
     *     
     */
    public void setRangePeriod(DateTimeInterval value) {
        this.rangePeriod = value;
    }

}
