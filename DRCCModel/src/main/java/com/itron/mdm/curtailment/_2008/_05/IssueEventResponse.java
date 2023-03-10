
package com.itron.mdm.curtailment._2008._05;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import com.itron.mdm.common._2008._04.ResponseBase;


/**
 * The Response definition for the CurtailmentEventSave.IssueEvent Operation
 * 
 * <p>Java class for IssueEvent_Response complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IssueEvent_Response"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.itron.com/mdm/common/2008/04}ResponseBase"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AlternateID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CommittedReduction" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="EnergyReduction" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="EventID" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ExpectedCompensation" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="ParticipatingServicePoints" type="{http://www.itron.com/mdm/curtailment/2008/05}ArrayOfServicePoint"/&gt;
 *         &lt;element name="ProgramID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="RejectedServicePoints" type="{http://www.itron.com/mdm/curtailment/2008/05}ArrayOfServicePoint"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IssueEvent_Response", propOrder = {
    "alternateID",
    "committedReduction",
    "energyReduction",
    "eventID",
    "expectedCompensation",
    "participatingServicePoints",
    "programID",
    "rejectedServicePoints"
})
public class IssueEventResponse
    extends ResponseBase
{

    @XmlElementRef(name = "AlternateID", namespace = "http://www.itron.com/mdm/curtailment/2008/05", type = JAXBElement.class, required = false)
    protected JAXBElement<String> alternateID;
    @XmlElement(name = "CommittedReduction")
    protected double committedReduction;
    @XmlElement(name = "EnergyReduction")
    protected double energyReduction;
    @XmlElement(name = "EventID")
    protected int eventID;
    @XmlElement(name = "ExpectedCompensation")
    protected double expectedCompensation;
    @XmlElement(name = "ParticipatingServicePoints", required = true, nillable = true)
    protected ArrayOfServicePoint participatingServicePoints;
    @XmlElement(name = "ProgramID", required = true, nillable = true)
    protected String programID;
    @XmlElement(name = "RejectedServicePoints", required = true, nillable = true)
    protected ArrayOfServicePoint rejectedServicePoints;

    /**
     * Gets the value of the alternateID property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAlternateID() {
        return alternateID;
    }

    /**
     * Sets the value of the alternateID property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAlternateID(JAXBElement<String> value) {
        this.alternateID = value;
    }

    /**
     * Gets the value of the committedReduction property.
     * 
     */
    public double getCommittedReduction() {
        return committedReduction;
    }

    /**
     * Sets the value of the committedReduction property.
     * 
     */
    public void setCommittedReduction(double value) {
        this.committedReduction = value;
    }

    /**
     * Gets the value of the energyReduction property.
     * 
     */
    public double getEnergyReduction() {
        return energyReduction;
    }

    /**
     * Sets the value of the energyReduction property.
     * 
     */
    public void setEnergyReduction(double value) {
        this.energyReduction = value;
    }

    /**
     * Gets the value of the eventID property.
     * 
     */
    public int getEventID() {
        return eventID;
    }

    /**
     * Sets the value of the eventID property.
     * 
     */
    public void setEventID(int value) {
        this.eventID = value;
    }

    /**
     * Gets the value of the expectedCompensation property.
     * 
     */
    public double getExpectedCompensation() {
        return expectedCompensation;
    }

    /**
     * Sets the value of the expectedCompensation property.
     * 
     */
    public void setExpectedCompensation(double value) {
        this.expectedCompensation = value;
    }

    /**
     * Gets the value of the participatingServicePoints property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfServicePoint }
     *     
     */
    public ArrayOfServicePoint getParticipatingServicePoints() {
        return participatingServicePoints;
    }

    /**
     * Sets the value of the participatingServicePoints property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfServicePoint }
     *     
     */
    public void setParticipatingServicePoints(ArrayOfServicePoint value) {
        this.participatingServicePoints = value;
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
     * Gets the value of the rejectedServicePoints property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfServicePoint }
     *     
     */
    public ArrayOfServicePoint getRejectedServicePoints() {
        return rejectedServicePoints;
    }

    /**
     * Sets the value of the rejectedServicePoints property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfServicePoint }
     *     
     */
    public void setRejectedServicePoints(ArrayOfServicePoint value) {
        this.rejectedServicePoints = value;
    }

}
