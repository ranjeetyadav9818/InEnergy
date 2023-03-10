
package com.itron.mdm.curtailment._2008._05;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IDType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="IDType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ServicePointID"/&gt;
 *     &lt;enumeration value="AccountNumber"/&gt;
 *     &lt;enumeration value="AccountID"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "IDType")
@XmlEnum
public enum IDType {

    @XmlEnumValue("ServicePointID")
    SERVICE_POINT_ID("ServicePointID"),
    @XmlEnumValue("AccountNumber")
    ACCOUNT_NUMBER("AccountNumber"),
    @XmlEnumValue("AccountID")
    ACCOUNT_ID("AccountID");
    private final String value;

    IDType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static IDType fromValue(String v) {
        for (IDType c: IDType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
