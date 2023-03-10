
package com.itron.mdm.common._2008._04;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Specifies element name and direction for sorting the results
 * 
 * <p>Java class for SortElement complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SortElement"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ElementName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SortOrder" type="{http://www.itron.com/mdm/common/2008/04}SortOrder" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SortElement", propOrder = {
    "elementName",
    "sortOrder"
})
public class GasSortElement {

    @XmlElementRef(name = "ElementName", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<String> elementName;
    @XmlElement(name = "SortOrder")
    @XmlSchemaType(name = "string")
    protected GasSortOrder gasSortOrder;

    /**
     * Gets the value of the elementName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getElementName() {
        return elementName;
    }

    /**
     * Sets the value of the elementName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setElementName(JAXBElement<String> value) {
        this.elementName = value;
    }

    /**
     * Gets the value of the sortOrder property.
     * 
     * @return
     *     possible object is
     *     {@link GasSortOrder }
     *     
     */
    public GasSortOrder getSortOrder() {
        return gasSortOrder;
    }

    /**
     * Sets the value of the sortOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link GasSortOrder }
     *     
     */
    public void setSortOrder(GasSortOrder value) {
        this.gasSortOrder = value;
    }

}
