
package com.caiso.soa.requestdrregistrationdata_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MessagePayload complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MessagePayload"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="LocationRequest" type="{http://www.caiso.com/soa/RequestDRRegistrationData_v1.xsd#}LocationRequest" minOccurs="0"/&gt;
 *         &lt;element name="RegistrationRequest" type="{http://www.caiso.com/soa/RequestDRRegistrationData_v1.xsd#}RegistrationRequest" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessagePayload", propOrder = {
    "locationRequest",
    "registrationRequest"
})
public class MessagePayload {

    @XmlElement(name = "LocationRequest")
    protected LocationRequest locationRequest;
    @XmlElement(name = "RegistrationRequest")
    protected RegistrationRequest registrationRequest;

    /**
     * Gets the value of the locationRequest property.
     * 
     * @return
     *     possible object is
     *     {@link LocationRequest }
     *     
     */
    public LocationRequest getLocationRequest() {
        return locationRequest;
    }

    /**
     * Sets the value of the locationRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link LocationRequest }
     *     
     */
    public void setLocationRequest(LocationRequest value) {
        this.locationRequest = value;
    }

    /**
     * Gets the value of the registrationRequest property.
     * 
     * @return
     *     possible object is
     *     {@link RegistrationRequest }
     *     
     */
    public RegistrationRequest getRegistrationRequest() {
        return registrationRequest;
    }

    /**
     * Sets the value of the registrationRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link RegistrationRequest }
     *     
     */
    public void setRegistrationRequest(RegistrationRequest value) {
        this.registrationRequest = value;
    }

}
