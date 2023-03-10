
package com.itron.mdm.common._2008._04;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ErrorProcessingInstructions.MessageLevel.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ErrorProcessingInstructions.MessageLevel"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Information"/&gt;
 *     &lt;enumeration value="Warning"/&gt;
 *     &lt;enumeration value="Error"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ErrorProcessingInstructions.MessageLevel")
@XmlEnum
public enum GasErrorProcessingInstructionsMessageLevel {

    @XmlEnumValue("Information")
    INFORMATION("Information"),
    @XmlEnumValue("Warning")
    WARNING("Warning"),
    @XmlEnumValue("Error")
    ERROR("Error");
    private final String value;

    GasErrorProcessingInstructionsMessageLevel(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static GasErrorProcessingInstructionsMessageLevel fromValue(String v) {
        for (GasErrorProcessingInstructionsMessageLevel c: GasErrorProcessingInstructionsMessageLevel.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
