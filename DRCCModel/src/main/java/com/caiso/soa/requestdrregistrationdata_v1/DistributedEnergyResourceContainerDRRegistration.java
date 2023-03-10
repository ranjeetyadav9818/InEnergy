
package com.caiso.soa.requestdrregistrationdata_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DistributedEnergyResourceContainer_DRRegistration complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DistributedEnergyResourceContainer_DRRegistration"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.caiso.com/soa/RequestDRRegistrationData_v1.xsd#}AssetContainer_DRRegistration"&gt;
 *       &lt;sequence&gt;
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
@XmlType(name = "DistributedEnergyResourceContainer_DRRegistration", propOrder = {
    "demandUtilityDistributionCompany"
})
@XmlSeeAlso({
    DemandResponseRegistrationFull.class
})
public class DistributedEnergyResourceContainerDRRegistration
    extends AssetContainerDRRegistration
{

    @XmlElement(name = "DemandUtilityDistributionCompany")
    protected DemandUtilityDistributionCompany demandUtilityDistributionCompany;

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
