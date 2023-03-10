
package com.itron.mdm.common._2008._04;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * Base class for all transfer contracts
 * Remarks: a 'Transfer' is a contract that is expected to be 'passed' back and forth between IEE systems
 * 
 * <p>Java class for TransferBase complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TransferBase"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.itron.com/mdm/common/2008/04}RequestBase"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Fault" type="{http://www.itron.com/mdm/common/2008/04}MdmServiceFault" minOccurs="0"/&gt;
 *         &lt;element name="LogMessages" type="{http://www.itron.com/mdm/common/2008/04}ArrayOfServiceLogMessage" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransferBase", propOrder = {
    "fault",
    "logMessages"
})
@XmlSeeAlso({
    BatchTransferBase.class
})
public class TransferBase
    extends RequestBase
{

    @XmlElementRef(name = "Fault", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<MdmServiceFault> fault;
    @XmlElementRef(name = "LogMessages", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfServiceLogMessage> logMessages;

    /**
     * Gets the value of the fault property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link MdmServiceFault }{@code >}
     *     
     */
    public JAXBElement<MdmServiceFault> getFault() {
        return fault;
    }

    /**
     * Sets the value of the fault property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link MdmServiceFault }{@code >}
     *     
     */
    public void setFault(JAXBElement<MdmServiceFault> value) {
        this.fault = value;
    }

    /**
     * Gets the value of the logMessages property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfServiceLogMessage }{@code >}
     *     
     */
    public JAXBElement<ArrayOfServiceLogMessage> getLogMessages() {
        return logMessages;
    }

    /**
     * Sets the value of the logMessages property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfServiceLogMessage }{@code >}
     *     
     */
    public void setLogMessages(JAXBElement<ArrayOfServiceLogMessage> value) {
        this.logMessages = value;
    }

}
