
package com.itron.mdm.common._2008._04;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfStatisticsSummary complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfStatisticsSummary"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="StatisticsSummary" type="{http://www.itron.com/mdm/common/2008/04}StatisticsSummary" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfStatisticsSummary", propOrder = {
    "statisticsSummary"
})
public class GasArrayOfStatisticsSummary {

    @XmlElement(name = "StatisticsSummary", nillable = true)
    protected List<GasStatisticsSummary> gasStatisticsSummary;

    /**
     * Gets the value of the statisticsSummary property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the statisticsSummary property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStatisticsSummary().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GasStatisticsSummary }
     * 
     * 
     */
    public List<GasStatisticsSummary> getStatisticsSummary() {
        if (gasStatisticsSummary == null) {
            gasStatisticsSummary = new ArrayList<GasStatisticsSummary>();
        }
        return this.gasStatisticsSummary;
    }

}
