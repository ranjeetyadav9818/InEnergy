
package com.caiso.soa.batchvalidationstatus_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BatchValidationStatus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BatchValidationStatus"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="MessageHeader" type="{http://www.caiso.com/soa/BatchValidationStatus_v1.xsd#}MessageHeader" minOccurs="0"/&gt;
 *         &lt;element name="MessagePayload" type="{http://www.caiso.com/soa/BatchValidationStatus_v1.xsd#}MessagePayload"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BatchValidationStatus", propOrder = {
    "messageHeader",
    "messagePayload"
})
public class BatchValidationStatus {

    @XmlElement(name = "MessageHeader")
    protected MessageHeader messageHeader;
    @XmlElement(name = "MessagePayload", required = true)
    protected MessagePayload messagePayload;

    /**
     * Gets the value of the messageHeader property.
     * 
     * @return
     *     possible object is
     *     {@link MessageHeader }
     *     
     */
    public MessageHeader getMessageHeader() {
        return messageHeader;
    }

    /**
     * Sets the value of the messageHeader property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageHeader }
     *     
     */
    public void setMessageHeader(MessageHeader value) {
        this.messageHeader = value;
    }

    /**
     * Gets the value of the messagePayload property.
     * 
     * @return
     *     possible object is
     *     {@link MessagePayload }
     *     
     */
    public MessagePayload getMessagePayload() {
        return messagePayload;
    }

    /**
     * Sets the value of the messagePayload property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessagePayload }
     *     
     */
    public void setMessagePayload(MessagePayload value) {
        this.messagePayload = value;
    }

}
