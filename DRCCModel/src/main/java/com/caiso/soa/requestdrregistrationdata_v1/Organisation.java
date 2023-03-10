
package com.caiso.soa.requestdrregistrationdata_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * Organisation that might have roles as utility, contractor, supplier, manufacturer, customer, etc.
 * 
 * <p>Java class for Organisation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Organisation"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="LoadServingEntity" type="{http://www.caiso.com/soa/RequestDRRegistrationData_v1.xsd#}LoadServingEntity"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Organisation", propOrder = {
    "loadServingEntity"
})
@XmlSeeAlso({
    MktOrganisationDER.class
})
public class Organisation {

    @XmlElement(name = "LoadServingEntity", required = true)
    protected LoadServingEntity loadServingEntity;

    /**
     * Gets the value of the loadServingEntity property.
     * 
     * @return
     *     possible object is
     *     {@link LoadServingEntity }
     *     
     */
    public LoadServingEntity getLoadServingEntity() {
        return loadServingEntity;
    }

    /**
     * Sets the value of the loadServingEntity property.
     * 
     * @param value
     *     allowed object is
     *     {@link LoadServingEntity }
     *     
     */
    public void setLoadServingEntity(LoadServingEntity value) {
        this.loadServingEntity = value;
    }

}
