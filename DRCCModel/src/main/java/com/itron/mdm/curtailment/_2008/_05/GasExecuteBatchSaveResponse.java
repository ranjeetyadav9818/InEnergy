
package com.itron.mdm.curtailment._2008._05;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ExecuteBatchSaveResult" type="{http://www.itron.com/mdm/curtailment/2008/05}CurtailmentEventSave_BatchResponse" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "executeBatchSaveResult"
})
@XmlRootElement(name = "ExecuteBatchSaveResponse")
public class GasExecuteBatchSaveResponse {

    @XmlElementRef(name = "ExecuteBatchSaveResult", namespace = "http://www.itron.com/mdm/curtailment/2008/05", type = JAXBElement.class, required = false)
    protected JAXBElement<GasCurtailmentEventSaveBatchGasResponse> executeBatchSaveResult;

    /**
     * Gets the value of the executeBatchSaveResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link GasCurtailmentEventSaveBatchGasResponse }{@code >}
     *     
     */
    public JAXBElement<GasCurtailmentEventSaveBatchGasResponse> getExecuteBatchSaveResult() {
        return executeBatchSaveResult;
    }

    /**
     * Sets the value of the executeBatchSaveResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link GasCurtailmentEventSaveBatchGasResponse }{@code >}
     *     
     */
    public void setExecuteBatchSaveResult(JAXBElement<GasCurtailmentEventSaveBatchGasResponse> value) {
        this.executeBatchSaveResult = value;
    }

}
