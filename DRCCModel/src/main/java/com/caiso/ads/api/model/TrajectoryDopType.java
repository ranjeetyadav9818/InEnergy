
package com.caiso.ads.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "trajectoryDopType", namespace = "http://ads.caiso.com", propOrder = {
    "resourceId",
    "dop",
    "targetTime",
    "sequenceNumber",
    "configurationId"
})
@Getter
@Setter
public class TrajectoryDopType {

    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String resourceId;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected Double dop;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected XMLGregorianCalendar targetTime;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String sequenceNumber;
    @XmlElement(namespace = "http://ads.caiso.com", required = true)
    protected String configurationId;
    @XmlAttribute(name = "dopUID")
    protected String dopUID;

}
