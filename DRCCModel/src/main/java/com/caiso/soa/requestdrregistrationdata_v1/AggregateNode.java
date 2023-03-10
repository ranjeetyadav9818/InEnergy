
package com.caiso.soa.requestdrregistrationdata_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * An aggregated node can define a typed grouping further defined by the AnodeType enumeratuion. Types range from System Zone/Regions to Market Energy Regions to Aggregated Loads and Aggregated Generators.
 * 
 * <p>Java class for AggregateNode complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AggregateNode"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AggregatedPnode" type="{http://www.caiso.com/soa/RequestDRRegistrationData_v1.xsd#}AggregatedPnode"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AggregateNode", propOrder = {
    "aggregatedPnode"
})
public class AggregateNode {

    @XmlElement(name = "AggregatedPnode", required = true)
    protected AggregatedPnode aggregatedPnode;

    /**
     * Gets the value of the aggregatedPnode property.
     * 
     * @return
     *     possible object is
     *     {@link AggregatedPnode }
     *     
     */
    public AggregatedPnode getAggregatedPnode() {
        return aggregatedPnode;
    }

    /**
     * Sets the value of the aggregatedPnode property.
     * 
     * @param value
     *     allowed object is
     *     {@link AggregatedPnode }
     *     
     */
    public void setAggregatedPnode(AggregatedPnode value) {
        this.aggregatedPnode = value;
    }

}
