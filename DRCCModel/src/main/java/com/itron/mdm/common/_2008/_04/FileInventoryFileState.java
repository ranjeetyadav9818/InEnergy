
package com.itron.mdm.common._2008._04;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FileInventoryFileState.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FileInventoryFileState"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="NotYetProcessed"/&gt;
 *     &lt;enumeration value="InProcess"/&gt;
 *     &lt;enumeration value="Error"/&gt;
 *     &lt;enumeration value="Processed"/&gt;
 *     &lt;enumeration value="Resubmit"/&gt;
 *     &lt;enumeration value="FTPFailure"/&gt;
 *     &lt;enumeration value="Undefined"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "FileInventoryFileState")
@XmlEnum
public enum FileInventoryFileState {

    @XmlEnumValue("NotYetProcessed")
    NOT_YET_PROCESSED("NotYetProcessed"),
    @XmlEnumValue("InProcess")
    IN_PROCESS("InProcess"),
    @XmlEnumValue("Error")
    ERROR("Error"),
    @XmlEnumValue("Processed")
    PROCESSED("Processed"),
    @XmlEnumValue("Resubmit")
    RESUBMIT("Resubmit"),
    @XmlEnumValue("FTPFailure")
    FTP_FAILURE("FTPFailure"),
    @XmlEnumValue("Undefined")
    UNDEFINED("Undefined");
    private final String value;

    FileInventoryFileState(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FileInventoryFileState fromValue(String v) {
        for (FileInventoryFileState c: FileInventoryFileState.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
