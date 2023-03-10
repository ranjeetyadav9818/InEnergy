
package com.itron.mdm.curtailment._2008._05;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import com.itron.mdm.common._2008._04.ResponseBase;


/**
 * The BatchResponse definition for the CurtailmentEventSave Service
 * 
 * <p>Java class for CurtailmentEventSave_BatchResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CurtailmentEventSave_BatchResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.itron.com/mdm/common/2008/04}ResponseBase"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="IssueEvent_Responses" type="{http://www.itron.com/mdm/curtailment/2008/05}ArrayOfIssueEvent_Response" minOccurs="0"/&gt;
 *         &lt;element name="ManageEvent_Responses" type="{http://www.itron.com/mdm/curtailment/2008/05}ArrayOfManageEvent_Response" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CurtailmentEventSave_BatchResponse", propOrder = {
    "issueEventResponses",
    "manageEventResponses"
})
public class CurtailmentEventSaveBatchResponse
    extends ResponseBase
{

    @XmlElementRef(name = "IssueEvent_Responses", namespace = "http://www.itron.com/mdm/curtailment/2008/05", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfIssueEventResponse> issueEventResponses;
    @XmlElementRef(name = "ManageEvent_Responses", namespace = "http://www.itron.com/mdm/curtailment/2008/05", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfManageEventResponse> manageEventResponses;

    /**
     * Gets the value of the issueEventResponses property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfIssueEventResponse }{@code >}
     *     
     */
    public JAXBElement<ArrayOfIssueEventResponse> getIssueEventResponses() {
        return issueEventResponses;
    }

    /**
     * Sets the value of the issueEventResponses property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfIssueEventResponse }{@code >}
     *     
     */
    public void setIssueEventResponses(JAXBElement<ArrayOfIssueEventResponse> value) {
        this.issueEventResponses = value;
    }

    /**
     * Gets the value of the manageEventResponses property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfManageEventResponse }{@code >}
     *     
     */
    public JAXBElement<ArrayOfManageEventResponse> getManageEventResponses() {
        return manageEventResponses;
    }

    /**
     * Sets the value of the manageEventResponses property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfManageEventResponse }{@code >}
     *     
     */
    public void setManageEventResponses(JAXBElement<ArrayOfManageEventResponse> value) {
        this.manageEventResponses = value;
    }

}
