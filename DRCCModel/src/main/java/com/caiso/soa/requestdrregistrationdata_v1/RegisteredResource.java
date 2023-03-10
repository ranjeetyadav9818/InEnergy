
package com.caiso.soa.requestdrregistrationdata_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * A resource that is registered through the RTO participant registration system. Examples include generating unit, customer meter, and a non-physical generator or load.
 * 
 * <p>Java class for RegisteredResource complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RegisteredResource"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="LoadAggregationPoint" type="{http://www.caiso.com/soa/RequestDRRegistrationData_v1.xsd#}AggregateNode" minOccurs="0"/&gt;
 *         &lt;element name="LoadServingEntity" type="{http://www.caiso.com/soa/RequestDRRegistrationData_v1.xsd#}LoadServingEntity" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegisteredResource", propOrder = {
    "loadAggregationPoint",
    "loadServingEntity"
})
@XmlSeeAlso({
    RegisteredGenerator.class
})
public class RegisteredResource {

    @XmlElement(name = "LoadAggregationPoint")
    protected AggregateNode loadAggregationPoint;
    @XmlElement(name = "LoadServingEntity")
    protected LoadServingEntity loadServingEntity;

    /**
     * Gets the value of the loadAggregationPoint property.
     * 
     * @return
     *     possible object is
     *     {@link AggregateNode }
     *     
     */
    public AggregateNode getLoadAggregationPoint() {
        return loadAggregationPoint;
    }

    /**
     * Sets the value of the loadAggregationPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link AggregateNode }
     *     
     */
    public void setLoadAggregationPoint(AggregateNode value) {
        this.loadAggregationPoint = value;
    }

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
