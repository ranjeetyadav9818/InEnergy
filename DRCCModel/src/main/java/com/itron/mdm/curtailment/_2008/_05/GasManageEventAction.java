
package com.itron.mdm.curtailment._2008._05;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ManageEventAction.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ManageEventAction"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Cancel"/&gt;
 *     &lt;enumeration value="Close"/&gt;
 *     &lt;enumeration value="Terminate"/&gt;
 *     &lt;enumeration value="ReSchedule"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ManageEventAction")
@XmlEnum
public enum GasManageEventAction {

    @XmlEnumValue("Cancel")
    CANCEL("Cancel"),
    @XmlEnumValue("Close")
    CLOSE("Close"),
    @XmlEnumValue("Terminate")
    TERMINATE("Terminate"),
    @XmlEnumValue("ReSchedule")
    RE_SCHEDULE("ReSchedule");
    private final String value;

    GasManageEventAction(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static GasManageEventAction fromValue(String v) {
        for (GasManageEventAction c: GasManageEventAction.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
