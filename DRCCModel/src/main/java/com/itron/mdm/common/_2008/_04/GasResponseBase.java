
package com.itron.mdm.common._2008._04;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import com.itron.mdm.curtailment._2008._05.GasCurtailmentEventSaveBatchGasResponse;
import com.itron.mdm.curtailment._2008._05.GasIssueEventGasResponse;
import com.itron.mdm.curtailment._2008._05.GasManageEventResponse;


/**
 * Base class for all response contracts
 * 
 * <p>Java class for ResponseBase complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResponseBase"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CorrelationID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Fault" type="{http://www.itron.com/mdm/common/2008/04}MdmServiceFault" minOccurs="0"/&gt;
 *         &lt;element name="LogMessages" type="{http://www.itron.com/mdm/common/2008/04}ArrayOfServiceLogMessage" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResponseBase", propOrder = {
    "correlationID",
    "fault",
    "logMessages"
})
@XmlSeeAlso({
    GasBatchGasResponseBase.class,
    GasCurtailmentEventSaveBatchGasResponse.class,
    GasIssueEventGasResponse.class,
    GasManageEventResponse.class
})
public class GasResponseBase {

    @XmlElementRef(name = "CorrelationID", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<String> correlationID;
    @XmlElementRef(name = "Fault", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<GasMdmServiceFault> fault;
    @XmlElementRef(name = "LogMessages", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<GasArrayOfServiceLogMessage> logMessages;

    /**
     * Gets the value of the correlationID property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCorrelationID() {
        return correlationID;
    }

    /**
     * Sets the value of the correlationID property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCorrelationID(JAXBElement<String> value) {
        this.correlationID = value;
    }

    /**
     * Gets the value of the fault property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link GasMdmServiceFault }{@code >}
     *     
     */
    public JAXBElement<GasMdmServiceFault> getFault() {
        return fault;
    }

    /**
     * Sets the value of the fault property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link GasMdmServiceFault }{@code >}
     *     
     */
    public void setFault(JAXBElement<GasMdmServiceFault> value) {
        this.fault = value;
    }

    /**
     * Gets the value of the logMessages property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link GasArrayOfServiceLogMessage }{@code >}
     *     
     */
    public JAXBElement<GasArrayOfServiceLogMessage> getLogMessages() {
        return logMessages;
    }

    /**
     * Sets the value of the logMessages property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link GasArrayOfServiceLogMessage }{@code >}
     *     
     */
    public void setLogMessages(JAXBElement<GasArrayOfServiceLogMessage> value) {
        this.logMessages = value;
    }

}
