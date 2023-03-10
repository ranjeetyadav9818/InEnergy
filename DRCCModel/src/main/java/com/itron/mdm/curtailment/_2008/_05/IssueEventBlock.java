
package com.itron.mdm.curtailment._2008._05;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * An IssueEventBlock element that allows event issuer to define eevnt parameters such as block price for each block. If SettlementMethod is 'EntireEvent', there must be exaclty one block defined for the event. Otherwise there must be one block for each hour or partial hour in the event.
 * 
 * <p>Java class for IssueEventBlock complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IssueEventBlock"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AdditionalReductionPrice" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="BlockNumber" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="BlockPrice" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="PenaltyPrice" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IssueEventBlock", propOrder = {
    "additionalReductionPrice",
    "blockNumber",
    "blockPrice",
    "penaltyPrice"
})
public class IssueEventBlock {

    @XmlElementRef(name = "AdditionalReductionPrice", namespace = "http://www.itron.com/mdm/curtailment/2008/05", type = JAXBElement.class, required = false)
    protected JAXBElement<Double> additionalReductionPrice;
    @XmlElement(name = "BlockNumber")
    protected Integer blockNumber;
    @XmlElementRef(name = "BlockPrice", namespace = "http://www.itron.com/mdm/curtailment/2008/05", type = JAXBElement.class, required = false)
    protected JAXBElement<Double> blockPrice;
    @XmlElementRef(name = "PenaltyPrice", namespace = "http://www.itron.com/mdm/curtailment/2008/05", type = JAXBElement.class, required = false)
    protected JAXBElement<Double> penaltyPrice;

    /**
     * Gets the value of the additionalReductionPrice property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Double }{@code >}
     *     
     */
    public JAXBElement<Double> getAdditionalReductionPrice() {
        return additionalReductionPrice;
    }

    /**
     * Sets the value of the additionalReductionPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Double }{@code >}
     *     
     */
    public void setAdditionalReductionPrice(JAXBElement<Double> value) {
        this.additionalReductionPrice = value;
    }

    /**
     * Gets the value of the blockNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBlockNumber() {
        return blockNumber;
    }

    /**
     * Sets the value of the blockNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBlockNumber(Integer value) {
        this.blockNumber = value;
    }

    /**
     * Gets the value of the blockPrice property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Double }{@code >}
     *     
     */
    public JAXBElement<Double> getBlockPrice() {
        return blockPrice;
    }

    /**
     * Sets the value of the blockPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Double }{@code >}
     *     
     */
    public void setBlockPrice(JAXBElement<Double> value) {
        this.blockPrice = value;
    }

    /**
     * Gets the value of the penaltyPrice property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Double }{@code >}
     *     
     */
    public JAXBElement<Double> getPenaltyPrice() {
        return penaltyPrice;
    }

    /**
     * Sets the value of the penaltyPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Double }{@code >}
     *     
     */
    public void setPenaltyPrice(JAXBElement<Double> value) {
        this.penaltyPrice = value;
    }

}
