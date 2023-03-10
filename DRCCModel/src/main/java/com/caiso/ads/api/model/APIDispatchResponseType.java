
package com.caiso.ads.api.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for APIDispatchResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="APIDispatchResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dispatchBatchList" type="{http://ads.caiso.com}dispatchBatchListType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "APIDispatchResponseType", namespace = "http://ads.caiso.com", propOrder = {
    "dispatchBatchList"
})
public class APIDispatchResponseType {

    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected DispatchBatchListType dispatchBatchList;

    /**
     * Gets the value of the dispatchBatchList property.
     * 
     * @return
     *     possible object is
     *     {@link DispatchBatchListType }
     *     
     */
    public DispatchBatchListType getDispatchBatchList() {
        return dispatchBatchList;
    }

    /**
     * Sets the value of the dispatchBatchList property.
     * 
     * @param value
     *     allowed object is
     *     {@link DispatchBatchListType }
     *     
     */
    public void setDispatchBatchList(DispatchBatchListType value) {
        this.dispatchBatchList = value;
    }

}
