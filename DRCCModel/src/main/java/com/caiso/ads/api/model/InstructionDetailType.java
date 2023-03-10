
package com.caiso.ads.api.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for instructionDetailType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="instructionDetailType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="serviceType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mw" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="price" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attribute name="segNo" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "instructionDetailType", namespace = "http://ads.caiso.com", propOrder = {
    "serviceType",
    "mw",
    "price"
})
public class InstructionDetailType {

    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String serviceType;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String mw;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String price;
    @XmlAttribute(name = "segNo")
    protected String segNo;

    /**
     * Gets the value of the serviceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * Sets the value of the serviceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceType(String value) {
        this.serviceType = value;
    }

    /**
     * Gets the value of the mw property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMw() {
        return mw;
    }

    /**
     * Sets the value of the mw property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMw(String value) {
        this.mw = value;
    }

    /**
     * Gets the value of the price property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrice() {
        return price;
    }

    /**
     * Sets the value of the price property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrice(String value) {
        this.price = value;
    }

    /**
     * Gets the value of the segNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSegNo() {
        return segNo;
    }

    /**
     * Sets the value of the segNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSegNo(String value) {
        this.segNo = value;
    }

}
