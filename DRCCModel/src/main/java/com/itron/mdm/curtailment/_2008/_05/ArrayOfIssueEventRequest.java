
package com.itron.mdm.curtailment._2008._05;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfIssueEvent_Request complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfIssueEvent_Request"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="IssueEvent_Request" type="{http://www.itron.com/mdm/curtailment/2008/05}IssueEvent_Request" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfIssueEvent_Request", propOrder = {
    "issueEventRequest"
})
public class ArrayOfIssueEventRequest {

    @XmlElement(name = "IssueEvent_Request", nillable = true)
    protected List<IssueEventRequest> issueEventRequest;

    /**
     * Gets the value of the issueEventRequest property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the issueEventRequest property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIssueEventRequest().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IssueEventRequest }
     * 
     * 
     */
    public List<IssueEventRequest> getIssueEventRequest() {
        if (issueEventRequest == null) {
            issueEventRequest = new ArrayList<IssueEventRequest>();
        }
        return this.issueEventRequest;
    }

}
