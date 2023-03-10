
package com.caiso.soa.requestdrregistrationdata_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DistributedEnergyResourceContainer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DistributedEnergyResourceContainer"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.caiso.com/soa/RequestDRRegistrationData_v1.xsd#}AssetContainer"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="DemandResponseRegistration" type="{http://www.caiso.com/soa/RequestDRRegistrationData_v1.xsd#}DemandResponseRegistration" minOccurs="0"/&gt;
 *         &lt;element name="DemandUtilityDistributionCompany" type="{http://www.caiso.com/soa/RequestDRRegistrationData_v1.xsd#}DemandUtilityDistributionCompany" minOccurs="0"/&gt;
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
    "demandResponseRegistration",
    "demandUtilityDistributionCompany"
})
public class DistributedEnergyResourceContainer
    extends AssetContainer
{

    @XmlElement(name = "DemandResponseRegistration")
    protected DemandResponseRegistration demandResponseRegistration;
    @XmlElement(name = "DemandUtilityDistributionCompany")
    protected DemandUtilityDistributionCompany demandUtilityDistributionCompany;

    /**
     * Gets the value of the demandResponseRegistration property.
     * 
     * @return
     *     possible object is
     *     {@link DemandResponseRegistration }
     *     
     */
    public DemandResponseRegistration getDemandResponseRegistration() {
        return demandResponseRegistration;
    }

    /**
     * Sets the value of the demandResponseRegistration property.
     * 
     * @param value
     *     allowed object is
     *     {@link DemandResponseRegistration }
     *     
     */
    public void setDemandResponseRegistration(DemandResponseRegistration value) {
        this.demandResponseRegistration = value;
    }

    /**
     * Gets the value of the demandUtilityDistributionCompany property.
     * 
     * @return
     *     possible object is
     *     {@link DemandUtilityDistributionCompany }
     *     
     */
    public DemandUtilityDistributionCompany getDemandUtilityDistributionCompany() {
        return demandUtilityDistributionCompany;
    }

    /**
     * Sets the value of the demandUtilityDistributionCompany property.
     * 
     * @param value
     *     allowed object is
     *     {@link DemandUtilityDistributionCompany }
     *     
     */
    public void setDemandUtilityDistributionCompany(DemandUtilityDistributionCompany value) {
        this.demandUtilityDistributionCompany = value;
    }

}
