
package com.caiso.soa.standardoutput_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MessagePayload complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MessagePayload"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="EventLog" type="{http://www.caiso.com/soa/StandardOutput_v1.xsd#}EventLog"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessagePayload", propOrder = {
    "eventLog"
})
public class MessagePayload {

    @XmlElement(name = "EventLog", required = true)
    protected EventLog eventLog;

    /**
     * Gets the value of the eventLog property.
     * 
     * @return
     *     possible object is
     *     {@link EventLog }
     *     
     */
    public EventLog getEventLog() {
        return eventLog;
    }

    /**
     * Sets the value of the eventLog property.
     * 
     * @param value
     *     allowed object is
     *     {@link EventLog }
     *     
     */
    public void setEventLog(EventLog value) {
        this.eventLog = value;
    }

}
