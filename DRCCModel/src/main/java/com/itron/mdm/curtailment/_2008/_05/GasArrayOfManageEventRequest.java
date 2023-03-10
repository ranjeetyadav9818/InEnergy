
package com.itron.mdm.curtailment._2008._05;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfManageEvent_Request complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfManageEvent_Request"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ManageEvent_Request" type="{http://www.itron.com/mdm/curtailment/2008/05}ManageEvent_Request" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfManageEvent_Request", propOrder = {
    "manageEventRequest"
})
public class GasArrayOfManageEventRequest {

    @XmlElement(name = "ManageEvent_Request", nillable = true)
    protected List<GasManageEventRequest> manageEventRequest;

    /**
     * Gets the value of the manageEventRequest property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the manageEventRequest property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getManageEventRequest().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GasManageEventRequest }
     * 
     * 
     */
    public List<GasManageEventRequest> getManageEventRequest() {
        if (manageEventRequest == null) {
            manageEventRequest = new ArrayList<GasManageEventRequest>();
        }
        return this.manageEventRequest;
    }

}
