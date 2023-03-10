
package com.caiso.soa.batchvalidationstatus_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * A power system resource can be an item of equipment such as a switch, an equipment container containing many individual items of equipment such as a substation, or an organisational entity such as sub-control area. Power system resources can have measurements associated.
 * 
 * <p>Java class for PowerSystemResource complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PowerSystemResource"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Flowgate" type="{http://www.caiso.com/soa/BatchValidationStatus_v1.xsd#}Flowgate" minOccurs="0"/&gt;
 *         &lt;element name="Measurements" type="{http://www.caiso.com/soa/BatchValidationStatus_v1.xsd#}Measurement" minOccurs="0"/&gt;
 *         &lt;element name="RegisteredGenerator" type="{http://www.caiso.com/soa/BatchValidationStatus_v1.xsd#}RegisteredGenerator" minOccurs="0"/&gt;
 *         &lt;element name="RegisteredInterTie" type="{http://www.caiso.com/soa/BatchValidationStatus_v1.xsd#}RegisteredInterTie" minOccurs="0"/&gt;
 *         &lt;element name="RegisteredLoad" type="{http://www.caiso.com/soa/BatchValidationStatus_v1.xsd#}RegisteredLoad" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PowerSystemResource", propOrder = {
    "flowgate",
    "measurements",
    "registeredGenerator",
    "registeredInterTie",
    "registeredLoad"
})
@XmlSeeAlso({
    RegisteredResource.class
})
public class PowerSystemResource {

    @XmlElement(name = "Flowgate")
    protected Flowgate flowgate;
    @XmlElement(name = "Measurements")
    protected Measurement measurements;
    @XmlElement(name = "RegisteredGenerator")
    protected RegisteredGenerator registeredGenerator;
    @XmlElement(name = "RegisteredInterTie")
    protected RegisteredInterTie registeredInterTie;
    @XmlElement(name = "RegisteredLoad")
    protected RegisteredLoad registeredLoad;

    /**
     * Gets the value of the flowgate property.
     * 
     * @return
     *     possible object is
     *     {@link Flowgate }
     *     
     */
    public Flowgate getFlowgate() {
        return flowgate;
    }

    /**
     * Sets the value of the flowgate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flowgate }
     *     
     */
    public void setFlowgate(Flowgate value) {
        this.flowgate = value;
    }

    /**
     * Gets the value of the measurements property.
     * 
     * @return
     *     possible object is
     *     {@link Measurement }
     *     
     */
    public Measurement getMeasurements() {
        return measurements;
    }

    /**
     * Sets the value of the measurements property.
     * 
     * @param value
     *     allowed object is
     *     {@link Measurement }
     *     
     */
    public void setMeasurements(Measurement value) {
        this.measurements = value;
    }

    /**
     * Gets the value of the registeredGenerator property.
     * 
     * @return
     *     possible object is
     *     {@link RegisteredGenerator }
     *     
     */
    public RegisteredGenerator getRegisteredGenerator() {
        return registeredGenerator;
    }

    /**
     * Sets the value of the registeredGenerator property.
     * 
     * @param value
     *     allowed object is
     *     {@link RegisteredGenerator }
     *     
     */
    public void setRegisteredGenerator(RegisteredGenerator value) {
        this.registeredGenerator = value;
    }

    /**
     * Gets the value of the registeredInterTie property.
     * 
     * @return
     *     possible object is
     *     {@link RegisteredInterTie }
     *     
     */
    public RegisteredInterTie getRegisteredInterTie() {
        return registeredInterTie;
    }

    /**
     * Sets the value of the registeredInterTie property.
     * 
     * @param value
     *     allowed object is
     *     {@link RegisteredInterTie }
     *     
     */
    public void setRegisteredInterTie(RegisteredInterTie value) {
        this.registeredInterTie = value;
    }

    /**
     * Gets the value of the registeredLoad property.
     * 
     * @return
     *     possible object is
     *     {@link RegisteredLoad }
     *     
     */
    public RegisteredLoad getRegisteredLoad() {
        return registeredLoad;
    }

    /**
     * Sets the value of the registeredLoad property.
     * 
     * @param value
     *     allowed object is
     *     {@link RegisteredLoad }
     *     
     */
    public void setRegisteredLoad(RegisteredLoad value) {
        this.registeredLoad = value;
    }

}
