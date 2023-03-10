
package com.itron.mdm.common._2008._04;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MessageSeverity.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MessageSeverity"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="CriticalError"/&gt;
 *     &lt;enumeration value="Error"/&gt;
 *     &lt;enumeration value="Warning"/&gt;
 *     &lt;enumeration value="Information"/&gt;
 *     &lt;enumeration value="Debug"/&gt;
 *     &lt;enumeration value="Trace"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "MessageSeverity")
@XmlEnum
public enum MessageSeverity {

    @XmlEnumValue("CriticalError")
    CRITICAL_ERROR("CriticalError"),
    @XmlEnumValue("Error")
    ERROR("Error"),
    @XmlEnumValue("Warning")
    WARNING("Warning"),
    @XmlEnumValue("Information")
    INFORMATION("Information"),
    @XmlEnumValue("Debug")
    DEBUG("Debug"),
    @XmlEnumValue("Trace")
    TRACE("Trace");
    private final String value;

    MessageSeverity(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MessageSeverity fromValue(String v) {
        for (MessageSeverity c: MessageSeverity.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
