
package com.itron.mdm.curtailment._2008._05;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfIssueEventBlock complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfIssueEventBlock"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="IssueEventBlock" type="{http://www.itron.com/mdm/curtailment/2008/05}IssueEventBlock" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfIssueEventBlock", propOrder = {
    "issueEventBlock"
})
public class ArrayOfIssueEventBlock {

    @XmlElement(name = "IssueEventBlock", nillable = true)
    protected List<IssueEventBlock> issueEventBlock;

    /**
     * Gets the value of the issueEventBlock property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the issueEventBlock property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIssueEventBlock().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IssueEventBlock }
     * 
     * 
     */
    public List<IssueEventBlock> getIssueEventBlock() {
        if (issueEventBlock == null) {
            issueEventBlock = new ArrayList<IssueEventBlock>();
        }
        return this.issueEventBlock;
    }

}
