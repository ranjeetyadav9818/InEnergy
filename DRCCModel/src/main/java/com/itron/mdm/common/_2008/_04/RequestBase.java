
package com.itron.mdm.common._2008._04;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import com.itron.mdm.curtailment._2008._05.CurtailmentEventSaveBatchRequest;
import com.itron.mdm.curtailment._2008._05.IssueEventRequest;
import com.itron.mdm.curtailment._2008._05.ManageEventRequest;


/**
 * Base class for all request contracts
 * 
 * <p>Java class for RequestBase complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RequestBase"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CorrelationID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ErrorProcessingInstructions" type="{http://www.itron.com/mdm/common/2008/04}ErrorProcessingInstructions" minOccurs="0"/&gt;
 *         &lt;element name="Locale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="RequestingUserName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RequestBase", propOrder = {
    "correlationID",
    "errorProcessingInstructions",
    "locale",
    "requestingUserName"
})
@XmlSeeAlso({
    BatchRequestBase.class,
    TransferBase.class,
    CurtailmentEventSaveBatchRequest.class,
    IssueEventRequest.class,
    ManageEventRequest.class
})
public class RequestBase {

    @XmlElementRef(name = "CorrelationID", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<String> correlationID;
    @XmlElementRef(name = "ErrorProcessingInstructions", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<ErrorProcessingInstructions> errorProcessingInstructions;
    @XmlElementRef(name = "Locale", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<String> locale;
    @XmlElementRef(name = "RequestingUserName", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<String> requestingUserName;

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
     * Gets the value of the errorProcessingInstructions property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ErrorProcessingInstructions }{@code >}
     *     
     */
    public JAXBElement<ErrorProcessingInstructions> getErrorProcessingInstructions() {
        return errorProcessingInstructions;
    }

    /**
     * Sets the value of the errorProcessingInstructions property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ErrorProcessingInstructions }{@code >}
     *     
     */
    public void setErrorProcessingInstructions(JAXBElement<ErrorProcessingInstructions> value) {
        this.errorProcessingInstructions = value;
    }

    /**
     * Gets the value of the locale property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLocale() {
        return locale;
    }

    /**
     * Sets the value of the locale property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLocale(JAXBElement<String> value) {
        this.locale = value;
    }

    /**
     * Gets the value of the requestingUserName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getRequestingUserName() {
        return requestingUserName;
    }

    /**
     * Sets the value of the requestingUserName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setRequestingUserName(JAXBElement<String> value) {
        this.requestingUserName = value;
    }

}
