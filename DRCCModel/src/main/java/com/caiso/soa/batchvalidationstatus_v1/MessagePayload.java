
package com.caiso.soa.batchvalidationstatus_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


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
 *         &lt;element name="BatchStatus" type="{http://www.caiso.com/soa/BatchValidationStatus_v1.xsd#}BatchStatus"/&gt;
 *         &lt;element name="DemandResponseRegistration" type="{http://www.caiso.com/soa/BatchValidationStatus_v1.xsd#}DemandResponseRegistration" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="DistributedEnergyResourceContainer" type="{http://www.caiso.com/soa/BatchValidationStatus_v1.xsd#}DistributedEnergyResourceContainer" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="ErrorLog" type="{http://www.caiso.com/soa/BatchValidationStatus_v1.xsd#}ErrorLog" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="RegisteredResource" type="{http://www.caiso.com/soa/BatchValidationStatus_v1.xsd#}RegisteredResource" maxOccurs="unbounded" minOccurs="0"/&gt;
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
    "batchStatus",
    "demandResponseRegistration",
    "distributedEnergyResourceContainer",
    "errorLog",
    "registeredResource"
})
public class MessagePayload {

    @XmlElement(name = "BatchStatus", required = true)
    protected BatchStatus batchStatus;
    @XmlElement(name = "DemandResponseRegistration")
    protected List<DemandResponseRegistration> demandResponseRegistration;
    @XmlElement(name = "DistributedEnergyResourceContainer")
    protected List<DistributedEnergyResourceContainer> distributedEnergyResourceContainer;
    @XmlElement(name = "ErrorLog")
    protected List<ErrorLog> errorLog;
    @XmlElement(name = "RegisteredResource")
    protected List<RegisteredResource> registeredResource;

    /**
     * Gets the value of the batchStatus property.
     * 
     * @return
     *     possible object is
     *     {@link BatchStatus }
     *     
     */
    public BatchStatus getBatchStatus() {
        return batchStatus;
    }

    /**
     * Sets the value of the batchStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link BatchStatus }
     *     
     */
    public void setBatchStatus(BatchStatus value) {
        this.batchStatus = value;
    }

    /**
     * Gets the value of the demandResponseRegistration property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the demandResponseRegistration property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDemandResponseRegistration().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DemandResponseRegistration }
     * 
     * 
     */
    public List<DemandResponseRegistration> getDemandResponseRegistration() {
        if (demandResponseRegistration == null) {
            demandResponseRegistration = new ArrayList<DemandResponseRegistration>();
        }
        return this.demandResponseRegistration;
    }

    /**
     * Gets the value of the distributedEnergyResourceContainer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the distributedEnergyResourceContainer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDistributedEnergyResourceContainer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DistributedEnergyResourceContainer }
     * 
     * 
     */
    public List<DistributedEnergyResourceContainer> getDistributedEnergyResourceContainer() {
        if (distributedEnergyResourceContainer == null) {
            distributedEnergyResourceContainer = new ArrayList<DistributedEnergyResourceContainer>();
        }
        return this.distributedEnergyResourceContainer;
    }

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

    /**
     * Gets the value of the registeredResource property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the registeredResource property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRegisteredResource().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RegisteredResource }
     * 
     * 
     */
    public List<RegisteredResource> getRegisteredResource() {
        if (registeredResource == null) {
            registeredResource = new ArrayList<RegisteredResource>();
        }
        return this.registeredResource;
    }

}
