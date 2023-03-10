
package com.itron.mdm.common._2008._04;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * This contract is used whereever there is a need to fetch statistics. The Name ,value pair hold the statistics name and the value. An optional category is used to specify a group for the statistics. Ex: Number of failures by ValidationRule. In this case, the category is "Validation", the stat name is the validation rule and the count is the number of failures.
 * 
 * <p>Java class for StatisticsSummary complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StatisticsSummary"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Category" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="StatisticsName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="StatisticsValue" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StatisticsSummary", propOrder = {
    "category",
    "statisticsName",
    "statisticsValue"
})
public class StatisticsSummary {

    @XmlElementRef(name = "Category", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<String> category;
    @XmlElement(name = "StatisticsName", required = true, nillable = true)
    protected String statisticsName;
    @XmlElement(name = "StatisticsValue")
    protected int statisticsValue;

    /**
     * Gets the value of the category property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCategory() {
        return category;
    }

    /**
     * Sets the value of the category property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCategory(JAXBElement<String> value) {
        this.category = value;
    }

    /**
     * Gets the value of the statisticsName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatisticsName() {
        return statisticsName;
    }

    /**
     * Sets the value of the statisticsName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatisticsName(String value) {
        this.statisticsName = value;
    }

    /**
     * Gets the value of the statisticsValue property.
     * 
     */
    public int getStatisticsValue() {
        return statisticsValue;
    }

    /**
     * Sets the value of the statisticsValue property.
     * 
     */
    public void setStatisticsValue(int value) {
        this.statisticsValue = value;
    }

}
