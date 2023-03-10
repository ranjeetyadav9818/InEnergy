
package com.caiso.soa.requestdrregistrationdata_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Asset_DRRegistration complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Asset_DRRegistration"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.caiso.com/soa/RequestDRRegistrationData_v1.xsd#}IdentifiedObject_DRRegistration"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="DemandResponseProvider" type="{http://www.caiso.com/soa/RequestDRRegistrationData_v1.xsd#}DemandResponseProvider" minOccurs="0"/&gt;
 *         &lt;element name="RegisteredGenerator" type="{http://www.caiso.com/soa/RequestDRRegistrationData_v1.xsd#}RegisteredResource_DRRegistration" minOccurs="0"/&gt;
 *         &lt;element name="status" type="{http://www.caiso.com/soa/RequestDRRegistrationData_v1.xsd#}Status" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Asset_DRRegistration", propOrder = {
    "demandResponseProvider",
    "registeredGenerator",
    "status"
})
@XmlSeeAlso({
    AssetContainerDRRegistration.class
})
public class AssetDRRegistration
    extends IdentifiedObjectDRRegistration
{

    @XmlElement(name = "DemandResponseProvider")
    protected DemandResponseProvider demandResponseProvider;
    @XmlElement(name = "RegisteredGenerator")
    protected RegisteredResourceDRRegistration registeredGenerator;
    protected Status status;

    /**
     * Gets the value of the demandResponseProvider property.
     * 
     * @return
     *     possible object is
     *     {@link DemandResponseProvider }
     *     
     */
    public DemandResponseProvider getDemandResponseProvider() {
        return demandResponseProvider;
    }

    /**
     * Sets the value of the demandResponseProvider property.
     * 
     * @param value
     *     allowed object is
     *     {@link DemandResponseProvider }
     *     
     */
    public void setDemandResponseProvider(DemandResponseProvider value) {
        this.demandResponseProvider = value;
    }

    /**
     * Gets the value of the registeredGenerator property.
     * 
     * @return
     *     possible object is
     *     {@link RegisteredResourceDRRegistration }
     *     
     */
    public RegisteredResourceDRRegistration getRegisteredGenerator() {
        return registeredGenerator;
    }

    /**
     * Sets the value of the registeredGenerator property.
     * 
     * @param value
     *     allowed object is
     *     {@link RegisteredResourceDRRegistration }
     *     
     */
    public void setRegisteredGenerator(RegisteredResourceDRRegistration value) {
        this.registeredGenerator = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link Status }
     *     
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link Status }
     *     
     */
    public void setStatus(Status value) {
        this.status = value;
    }

}
