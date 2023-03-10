
package com.itron.mdm.curtailment._2008._05;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfIssueEvent_Response complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfIssueEvent_Response"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="IssueEvent_Response" type="{http://www.itron.com/mdm/curtailment/2008/05}IssueEvent_Response" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfIssueEvent_Response", propOrder = {
    "issueEventResponse"
})
public class GasArrayOfIssueEventResponse {

    @XmlElement(name = "IssueEvent_Response", nillable = true)
    protected List<GasIssueEventGasResponse> issueEventResponse;

    /**
     * Gets the value of the issueEventResponse property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the issueEventResponse property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIssueEventResponse().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GasIssueEventGasResponse }
     * 
     * 
     */
    public List<GasIssueEventGasResponse> getIssueEventResponse() {
        if (issueEventResponse == null) {
            issueEventResponse = new ArrayList<GasIssueEventGasResponse>();
        }
        return this.issueEventResponse;
    }

}
