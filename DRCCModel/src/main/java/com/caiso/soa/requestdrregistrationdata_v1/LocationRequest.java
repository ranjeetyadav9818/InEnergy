
package com.caiso.soa.requestdrregistrationdata_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LocationRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LocationRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="DistributedEnergyResourceContainer" type="{http://www.caiso.com/soa/RequestDRRegistrationData_v1.xsd#}DistributedEnergyResourceContainer" minOccurs="0"/&gt;
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
@XmlType(name = "LocationRequest", propOrder = {
    "distributedEnergyResourceContainer",
    "rangePeriod"
})
public class LocationRequest {

    @XmlElement(name = "DistributedEnergyResourceContainer")
    protected DistributedEnergyResourceContainer distributedEnergyResourceContainer;
    protected DateTimeInterval rangePeriod;

    /**
     * Gets the value of the distributedEnergyResourceContainer property.
     * 
     * @return
     *     possible object is
     *     {@link DistributedEnergyResourceContainer }
     *     
     */
    public DistributedEnergyResourceContainer getDistributedEnergyResourceContainer() {
        return distributedEnergyResourceContainer;
    }

    /**
     * Sets the value of the distributedEnergyResourceContainer property.
     * 
     * @param value
     *     allowed object is
     *     {@link DistributedEnergyResourceContainer }
     *     
     */
    public void setDistributedEnergyResourceContainer(DistributedEnergyResourceContainer value) {
        this.distributedEnergyResourceContainer = value;
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
