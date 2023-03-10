
package com.caiso.soa.requestdrregistrationdata_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for LoadAggregationPoint complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LoadAggregationPoint"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AggregatedPnode" type="{http://www.caiso.com/soa/RequestDRRegistrationData_v1.xsd#}AggregatedPnode" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LoadAggregationPoint", propOrder = {
    "aggregatedPnode"
})
public class LoadAggregationPoint {

    @XmlElement(name = "AggregatedPnode", required = true)
    protected List<AggregatedPnode> aggregatedPnode;

    /**
     * Gets the value of the aggregatedPnode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the aggregatedPnode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAggregatedPnode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AggregatedPnode }
     * 
     * 
     */
    public List<AggregatedPnode> getAggregatedPnode() {
        if (aggregatedPnode == null) {
            aggregatedPnode = new ArrayList<AggregatedPnode>();
        }
        return this.aggregatedPnode;
    }

}
