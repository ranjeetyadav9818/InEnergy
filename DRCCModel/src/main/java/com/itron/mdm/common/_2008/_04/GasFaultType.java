
package com.itron.mdm.common._2008._04;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FaultType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FaultType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="AccessViolation"/&gt;
 *     &lt;enumeration value="ApplicationException"/&gt;
 *     &lt;enumeration value="InvalidParameter"/&gt;
 *     &lt;enumeration value="SystemException"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "FaultType")
@XmlEnum
public enum GasFaultType {

    @XmlEnumValue("AccessViolation")
    ACCESS_VIOLATION("AccessViolation"),
    @XmlEnumValue("ApplicationException")
    APPLICATION_EXCEPTION("ApplicationException"),
    @XmlEnumValue("InvalidParameter")
    INVALID_PARAMETER("InvalidParameter"),
    @XmlEnumValue("SystemException")
    SYSTEM_EXCEPTION("SystemException");
    private final String value;

    GasFaultType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static GasFaultType fromValue(String v) {
        for (GasFaultType c: GasFaultType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
