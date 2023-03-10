
package com.caiso.soa.batchvalidationstatus_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * Distributed energy resource container.
 * 
 * <p>Java class for DistributedEnergyResourceContainer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DistributedEnergyResourceContainer"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.caiso.com/soa/BatchValidationStatus_v1.xsd#}IdentifiedObject_mrid_name"&gt;
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
@XmlType(name = "DistributedEnergyResourceContainer", propOrder = {
    "errorLog"
})
public class DistributedEnergyResourceContainer
    extends IdentifiedObjectMridName
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
