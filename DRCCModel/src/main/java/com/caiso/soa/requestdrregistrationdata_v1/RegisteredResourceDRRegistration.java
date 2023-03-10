
package com.caiso.soa.requestdrregistrationdata_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RegisteredResource_DRRegistration complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RegisteredResource_DRRegistration"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.caiso.com/soa/RequestDRRegistrationData_v1.xsd#}IdentifiedObject_REST_37"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="LoadAggregationPoint" type="{http://www.caiso.com/soa/RequestDRRegistrationData_v1.xsd#}LoadAggregationPoint_DRRegistration" minOccurs="0"/&gt;
 *         &lt;element name="LoadServingEntity" type="{http://www.caiso.com/soa/RequestDRRegistrationData_v1.xsd#}LoadServingEntity" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegisteredResource_DRRegistration", propOrder = {
    "loadAggregationPoint",
    "loadServingEntity"
})
@XmlSeeAlso({
    RegisteredGeneratorDRRegistration.class
})
public class RegisteredResourceDRRegistration
    extends IdentifiedObjectREST37
{

    @XmlElement(name = "LoadAggregationPoint")
    protected LoadAggregationPointDRRegistration loadAggregationPoint;
    @XmlElement(name = "LoadServingEntity")
    protected LoadServingEntity loadServingEntity;

    /**
     * Gets the value of the loadAggregationPoint property.
     * 
     * @return
     *     possible object is
     *     {@link LoadAggregationPointDRRegistration }
     *     
     */
    public LoadAggregationPointDRRegistration getLoadAggregationPoint() {
        return loadAggregationPoint;
    }

    /**
     * Sets the value of the loadAggregationPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link LoadAggregationPointDRRegistration }
     *     
     */
    public void setLoadAggregationPoint(LoadAggregationPointDRRegistration value) {
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
