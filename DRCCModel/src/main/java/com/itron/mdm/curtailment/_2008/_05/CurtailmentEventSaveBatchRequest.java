
package com.itron.mdm.curtailment._2008._05;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import com.itron.mdm.common._2008._04.RequestBase;


/**
 * The BatchRequest definition for the CurtailmentEventSave Service
 * 
 * <p>Java class for CurtailmentEventSave_BatchRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CurtailmentEventSave_BatchRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.itron.com/mdm/common/2008/04}RequestBase"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="IssueEvent_Requests" type="{http://www.itron.com/mdm/curtailment/2008/05}ArrayOfIssueEvent_Request" minOccurs="0"/&gt;
 *         &lt;element name="ManageEvent_Requests" type="{http://www.itron.com/mdm/curtailment/2008/05}ArrayOfManageEvent_Request" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CurtailmentEventSave_BatchRequest", propOrder = {
    "issueEventRequests",
    "manageEventRequests"
})
public class CurtailmentEventSaveBatchRequest
    extends RequestBase
{

    @XmlElementRef(name = "IssueEvent_Requests", namespace = "http://www.itron.com/mdm/curtailment/2008/05", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfIssueEventRequest> issueEventRequests;
    @XmlElementRef(name = "ManageEvent_Requests", namespace = "http://www.itron.com/mdm/curtailment/2008/05", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfManageEventRequest> manageEventRequests;

    /**
     * Gets the value of the issueEventRequests property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfIssueEventRequest }{@code >}
     *     
     */
    public JAXBElement<ArrayOfIssueEventRequest> getIssueEventRequests() {
        return issueEventRequests;
    }

    /**
     * Sets the value of the issueEventRequests property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfIssueEventRequest }{@code >}
     *     
     */
    public void setIssueEventRequests(JAXBElement<ArrayOfIssueEventRequest> value) {
        this.issueEventRequests = value;
    }

    /**
     * Gets the value of the manageEventRequests property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfManageEventRequest }{@code >}
     *     
     */
    public JAXBElement<ArrayOfManageEventRequest> getManageEventRequests() {
        return manageEventRequests;
    }

    /**
     * Sets the value of the manageEventRequests property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfManageEventRequest }{@code >}
     *     
     */
    public void setManageEventRequests(JAXBElement<ArrayOfManageEventRequest> value) {
        this.manageEventRequests = value;
    }

}
