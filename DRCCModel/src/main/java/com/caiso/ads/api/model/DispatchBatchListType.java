
package com.caiso.ads.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;


/**
 * <p>Java class for dispatchBatchListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dispatchBatchListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DispatchBatch" type="{http://ads.caiso.com}DispatchBatchType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dispatchBatchListType", namespace = "http://ads.caiso.com", propOrder = {
    "dispatchBatch"
})
@Getter
@Setter
public class DispatchBatchListType {

    @XmlElement(name = "DispatchBatch", namespace = "http://ads.caiso.com", required = true)
    protected List<DispatchBatchType> dispatchBatch;


}
