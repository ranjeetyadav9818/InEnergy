
package com.itron.mdm.common._2008._04;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * Instructions for how the service should report faults and error information
 * 
 * <p>Java class for ErrorProcessingInstructions complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ErrorProcessingInstructions"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ReturnFaultInResponse" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="ReturnLogMessages" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="ReturnLogMessageLevel" type="{http://www.itron.com/mdm/common/2008/04}ErrorProcessingInstructions.MessageLevel" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ErrorProcessingInstructions", propOrder = {
    "returnFaultInResponse",
    "returnLogMessages",
    "returnLogMessageLevel"
})
public class ErrorProcessingInstructions {

    @XmlElement(name = "ReturnFaultInResponse")
    protected Boolean returnFaultInResponse;
    @XmlElementRef(name = "ReturnLogMessages", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<Boolean> returnLogMessages;
    @XmlElementRef(name = "ReturnLogMessageLevel", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<ErrorProcessingInstructionsMessageLevel> returnLogMessageLevel;

    /**
     * Gets the value of the returnFaultInResponse property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isReturnFaultInResponse() {
        return returnFaultInResponse;
    }

    /**
     * Sets the value of the returnFaultInResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setReturnFaultInResponse(Boolean value) {
        this.returnFaultInResponse = value;
    }

    /**
     * Gets the value of the returnLogMessages property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getReturnLogMessages() {
        return returnLogMessages;
    }

    /**
     * Sets the value of the returnLogMessages property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setReturnLogMessages(JAXBElement<Boolean> value) {
        this.returnLogMessages = value;
    }

    /**
     * Gets the value of the returnLogMessageLevel property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ErrorProcessingInstructionsMessageLevel }{@code >}
     *     
     */
    public JAXBElement<ErrorProcessingInstructionsMessageLevel> getReturnLogMessageLevel() {
        return returnLogMessageLevel;
    }

    /**
     * Sets the value of the returnLogMessageLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ErrorProcessingInstructionsMessageLevel }{@code >}
     *     
     */
    public void setReturnLogMessageLevel(JAXBElement<ErrorProcessingInstructionsMessageLevel> value) {
        this.returnLogMessageLevel = value;
    }

}
