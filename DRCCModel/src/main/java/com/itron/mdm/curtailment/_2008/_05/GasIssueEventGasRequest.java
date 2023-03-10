
package com.itron.mdm.curtailment._2008._05;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import com.itron.mdm.common._2008._04.GasRequestBase;


/**
 * The Request definition for the CurtailmentEventSave.IssueEvent Operation
 * 
 * <p>Java class for IssueEvent_Request complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IssueEvent_Request"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.itron.com/mdm/common/2008/04}RequestBase"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ContinueOnServicePointValidationFailure" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="DelayNoticeUntillDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="DemandCapValue" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="EventAlternateID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="EventEndDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="EventSettlementPeriod" type="{http://www.itron.com/mdm/curtailment/2008/05}SettlementPeriod"/&gt;
 *         &lt;element name="EventStartDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="IncludeServicePointsInOverlappingEvents" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="IsTestEvent" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="IssueEventBlocks" type="{http://www.itron.com/mdm/curtailment/2008/05}ArrayOfIssueEventBlock"/&gt;
 *         &lt;element name="IssueEventServicePoints" type="{http://www.itron.com/mdm/curtailment/2008/05}ArrayOfIssueEventServicePoint"/&gt;
 *         &lt;element name="Message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ProgramID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="RespondByDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="StartDialingMetersDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IssueEvent_Request", propOrder = {
    "continueOnServicePointValidationFailure",
    "delayNoticeUntillDateTime",
    "demandCapValue",
    "eventAlternateID",
    "eventEndDateTime",
    "eventSettlementPeriod",
    "eventStartDateTime",
    "includeServicePointsInOverlappingEvents",
    "isTestEvent",
    "issueEventBlocks",
    "issueEventServicePoints",
    "message",
    "programID",
    "respondByDateTime",
    "startDialingMetersDateTime"
})
public class GasIssueEventGasRequest
    extends GasRequestBase
{

    @XmlElementRef(name = "ContinueOnServicePointValidationFailure", namespace = "http://www.itron.com/mdm/curtailment/2008/05", type = JAXBElement.class, required = false)
    protected JAXBElement<Boolean> continueOnServicePointValidationFailure;
    @XmlElementRef(name = "DelayNoticeUntillDateTime", namespace = "http://www.itron.com/mdm/curtailment/2008/05", type = JAXBElement.class, required = false)
    protected JAXBElement<XMLGregorianCalendar> delayNoticeUntillDateTime;
    @XmlElementRef(name = "DemandCapValue", namespace = "http://www.itron.com/mdm/curtailment/2008/05", type = JAXBElement.class, required = false)
    protected JAXBElement<Double> demandCapValue;
    @XmlElementRef(name = "EventAlternateID", namespace = "http://www.itron.com/mdm/curtailment/2008/05", type = JAXBElement.class, required = false)
    protected JAXBElement<String> eventAlternateID;
    @XmlElement(name = "EventEndDateTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar eventEndDateTime;
    @XmlElement(name = "EventSettlementPeriod", required = true)
    @XmlSchemaType(name = "string")
    protected GasSettlementPeriod eventGasSettlementPeriod;
    @XmlElement(name = "EventStartDateTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar eventStartDateTime;
    @XmlElementRef(name = "IncludeServicePointsInOverlappingEvents", namespace = "http://www.itron.com/mdm/curtailment/2008/05", type = JAXBElement.class, required = false)
    protected JAXBElement<Boolean> includeServicePointsInOverlappingEvents;
    @XmlElementRef(name = "IsTestEvent", namespace = "http://www.itron.com/mdm/curtailment/2008/05", type = JAXBElement.class, required = false)
    protected JAXBElement<Boolean> isTestEvent;
    @XmlElement(name = "IssueEventBlocks", required = false, nillable = true,namespace = "http://www.itron.com/mdm/common/2008/04")
    protected GasArrayOfIssueEventBlock issueEventBlocks;
    @XmlElement(name = "IssueEventServicePoints", required = true, nillable = true)
    protected GasArrayOfIssueEventServicePoint issueEventServicePoints;
    @XmlElementRef(name = "Message", namespace = "http://www.itron.com/mdm/curtailment/2008/05", type = JAXBElement.class, required = false)
    protected JAXBElement<String> message;
    @XmlElement(name = "ProgramID", required = true, nillable = true)
    protected String programID;
    @XmlElementRef(name = "RespondByDateTime", namespace = "http://www.itron.com/mdm/curtailment/2008/05", type = JAXBElement.class, required = false)
    protected JAXBElement<XMLGregorianCalendar> respondByDateTime;
    @XmlElementRef(name = "StartDialingMetersDateTime", namespace = "http://www.itron.com/mdm/curtailment/2008/05", type = JAXBElement.class, required = false)
    protected JAXBElement<XMLGregorianCalendar> startDialingMetersDateTime;

    /**
     * Gets the value of the continueOnServicePointValidationFailure property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getContinueOnServicePointValidationFailure() {
        return continueOnServicePointValidationFailure;
    }

    /**
     * Sets the value of the continueOnServicePointValidationFailure property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setContinueOnServicePointValidationFailure(JAXBElement<Boolean> value) {
        this.continueOnServicePointValidationFailure = value;
    }

    /**
     * Gets the value of the delayNoticeUntillDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getDelayNoticeUntillDateTime() {
        return delayNoticeUntillDateTime;
    }

    /**
     * Sets the value of the delayNoticeUntillDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setDelayNoticeUntillDateTime(JAXBElement<XMLGregorianCalendar> value) {
        this.delayNoticeUntillDateTime = value;
    }

    /**
     * Gets the value of the demandCapValue property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Double }{@code >}
     *     
     */
    public JAXBElement<Double> getDemandCapValue() {
        return demandCapValue;
    }

    /**
     * Sets the value of the demandCapValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Double }{@code >}
     *     
     */
    public void setDemandCapValue(JAXBElement<Double> value) {
        this.demandCapValue = value;
    }

    /**
     * Gets the value of the eventAlternateID property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getEventAlternateID() {
        return eventAlternateID;
    }

    /**
     * Sets the value of the eventAlternateID property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setEventAlternateID(JAXBElement<String> value) {
        this.eventAlternateID = value;
    }

    /**
     * Gets the value of the eventEndDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEventEndDateTime() {
        return eventEndDateTime;
    }

    /**
     * Sets the value of the eventEndDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEventEndDateTime(XMLGregorianCalendar value) {
        this.eventEndDateTime = value;
    }

    /**
     * Gets the value of the eventSettlementPeriod property.
     * 
     * @return
     *     possible object is
     *     {@link GasSettlementPeriod }
     *     
     */
    public GasSettlementPeriod getEventSettlementPeriod() {
        return eventGasSettlementPeriod;
    }

    /**
     * Sets the value of the eventSettlementPeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link GasSettlementPeriod }
     *     
     */
    public void setEventSettlementPeriod(GasSettlementPeriod value) {
        this.eventGasSettlementPeriod = value;
    }

    /**
     * Gets the value of the eventStartDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEventStartDateTime() {
        return eventStartDateTime;
    }

    /**
     * Sets the value of the eventStartDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEventStartDateTime(XMLGregorianCalendar value) {
        this.eventStartDateTime = value;
    }

    /**
     * Gets the value of the includeServicePointsInOverlappingEvents property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getIncludeServicePointsInOverlappingEvents() {
        return includeServicePointsInOverlappingEvents;
    }

    /**
     * Sets the value of the includeServicePointsInOverlappingEvents property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setIncludeServicePointsInOverlappingEvents(JAXBElement<Boolean> value) {
        this.includeServicePointsInOverlappingEvents = value;
    }

    /**
     * Gets the value of the isTestEvent property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getIsTestEvent() {
        return isTestEvent;
    }

    /**
     * Sets the value of the isTestEvent property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setIsTestEvent(JAXBElement<Boolean> value) {
        this.isTestEvent = value;
    }

    /**
     * Gets the value of the issueEventBlocks property.
     *
     * @return
     *     possible object is
     *     {@link GasArrayOfIssueEventBlock }
     *
     */
    public GasArrayOfIssueEventBlock getIssueEventBlocks() {
        return issueEventBlocks;
    }

    /**
     * Sets the value of the issueEventBlocks property.
     *
     * @param value
     *     allowed object is
     *     {@link GasArrayOfIssueEventBlock }
     *
     */
    public void setIssueEventBlocks(GasArrayOfIssueEventBlock value) {
        this.issueEventBlocks = value;
    }

    /**
     * Gets the value of the issueEventServicePoints property.
     * 
     * @return
     *     possible object is
     *     {@link GasArrayOfIssueEventServicePoint }
     *     
     */
    public GasArrayOfIssueEventServicePoint getIssueEventServicePoints() {
        return issueEventServicePoints;
    }

    /**
     * Sets the value of the issueEventServicePoints property.
     * 
     * @param value
     *     allowed object is
     *     {@link GasArrayOfIssueEventServicePoint }
     *     
     */
    public void setIssueEventServicePoints(GasArrayOfIssueEventServicePoint value) {
        this.issueEventServicePoints = value;
    }

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMessage(JAXBElement<String> value) {
        this.message = value;
    }

    /**
     * Gets the value of the programID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProgramID() {
        return programID;
    }

    /**
     * Sets the value of the programID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProgramID(String value) {
        this.programID = value;
    }

    /**
     * Gets the value of the respondByDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getRespondByDateTime() {
        return respondByDateTime;
    }

    /**
     * Sets the value of the respondByDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setRespondByDateTime(JAXBElement<XMLGregorianCalendar> value) {
        this.respondByDateTime = value;
    }

    /**
     * Gets the value of the startDialingMetersDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getStartDialingMetersDateTime() {
        return startDialingMetersDateTime;
    }

    /**
     * Sets the value of the startDialingMetersDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setStartDialingMetersDateTime(JAXBElement<XMLGregorianCalendar> value) {
        this.startDialingMetersDateTime = value;
    }

}
