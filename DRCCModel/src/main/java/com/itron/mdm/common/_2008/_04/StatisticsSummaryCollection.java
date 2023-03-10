
package com.itron.mdm.common._2008._04;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * List element to insure an ArrayOfStatisticsSummary will be included in the Common xsd
 * 
 * <p>Java class for StatisticsSummaryCollection complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StatisticsSummaryCollection"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="StatisticsSummarys" type="{http://www.itron.com/mdm/common/2008/04}ArrayOfStatisticsSummary" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StatisticsSummaryCollection", propOrder = {
    "statisticsSummarys"
})
public class StatisticsSummaryCollection {

    @XmlElementRef(name = "StatisticsSummarys", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfStatisticsSummary> statisticsSummarys;

    /**
     * Gets the value of the statisticsSummarys property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfStatisticsSummary }{@code >}
     *     
     */
    public JAXBElement<ArrayOfStatisticsSummary> getStatisticsSummarys() {
        return statisticsSummarys;
    }

    /**
     * Sets the value of the statisticsSummarys property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfStatisticsSummary }{@code >}
     *     
     */
    public void setStatisticsSummarys(JAXBElement<ArrayOfStatisticsSummary> value) {
        this.statisticsSummarys = value;
    }

}
