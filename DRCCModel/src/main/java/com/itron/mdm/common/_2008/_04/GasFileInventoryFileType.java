
package com.itron.mdm.common._2008._04;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FileInventoryFileType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FileInventoryFileType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="XML"/&gt;
 *     &lt;enumeration value="Workbin"/&gt;
 *     &lt;enumeration value="CleanReading"/&gt;
 *     &lt;enumeration value="Banked"/&gt;
 *     &lt;enumeration value="ARIBypass"/&gt;
 *     &lt;enumeration value="Undefined"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "FileInventoryFileType")
@XmlEnum
public enum GasFileInventoryFileType {

    XML("XML"),
    @XmlEnumValue("Workbin")
    WORKBIN("Workbin"),
    @XmlEnumValue("CleanReading")
    CLEAN_READING("CleanReading"),
    @XmlEnumValue("Banked")
    BANKED("Banked"),
    @XmlEnumValue("ARIBypass")
    ARI_BYPASS("ARIBypass"),
    @XmlEnumValue("Undefined")
    UNDEFINED("Undefined");
    private final String value;

    GasFileInventoryFileType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static GasFileInventoryFileType fromValue(String v) {
        for (GasFileInventoryFileType c: GasFileInventoryFileType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
