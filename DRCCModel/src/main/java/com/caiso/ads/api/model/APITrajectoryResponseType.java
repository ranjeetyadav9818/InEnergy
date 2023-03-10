
package com.caiso.ads.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for APITrajectoryResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="APITrajectoryResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="trajectoryBatchList" type="{http://ads.caiso.com}trajectoryBatchListType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "APITrajectoryResponseType", namespace = "http://ads.caiso.com", propOrder = {
    "trajectoryBatchList"
})
@Getter
@Setter
public class APITrajectoryResponseType {

    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected TrajectoryBatchListType trajectoryBatchList;

}
