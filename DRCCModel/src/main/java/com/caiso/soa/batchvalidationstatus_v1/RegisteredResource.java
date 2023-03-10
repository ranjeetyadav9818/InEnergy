
package com.caiso.soa.batchvalidationstatus_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * A resource that is registered through the RTO participant registration system. Examples include generating unit, customer meter, and a non-physical generator or load.
 * 
 * <p>Java class for RegisteredResource complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RegisteredResource"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.caiso.com/soa/BatchValidationStatus_v1.xsd#}PowerSystemResource"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ErrorLog" type="{http://www.caiso.com/soa/BatchValidationStatus_v1.xsd#}ErrorLog" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegisteredResource", propOrder = {
    "errorLog"
})
public class RegisteredResource
    extends PowerSystemResource
{

    @XmlElement(name = "ErrorLog", required = true)
    protected List<ErrorLog> errorLog;

    /**
     * Gets the value of the errorLog property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the errorLog property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getErrorLog().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ErrorLog }
     * 
     * 
     */
    public List<ErrorLog> getErrorLog() {
        if (errorLog == null) {
            errorLog = new ArrayList<ErrorLog>();
        }
        return this.errorLog;
    }

}
