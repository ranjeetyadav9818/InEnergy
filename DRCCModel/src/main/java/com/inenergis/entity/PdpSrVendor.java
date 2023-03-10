package com.inenergis.entity;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;


/**
 * The persistent class for the PDP_SR_VENDOR database table.
 * 
 */
@Entity
@Table(name="PDP_SR_VENDOR")
@NamedQuery(name="PdpSrVendor.findAll", query="SELECT p FROM PdpSrVendor p")
public class PdpSrVendor implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long vendorId;
	private String vendor;
	private List<PdpSrNotification> pdpSrNotifications = new ArrayList<PdpSrNotification>();
	private List<VendorConfig> vendorConfigs = new ArrayList<VendorConfig>();

	public PdpSrVendor() {
	}


	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name="VENDOR_ID")
	public Long getVendorId() {
		return this.vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	@Column(name="VENDOR")
	public String getVendor() {
		return this.vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	//bi-directional many-to-one association to PdpSrNotification
	@OneToMany(mappedBy="pdpSrVendor")
	public List<PdpSrNotification> getPdpSrNotifications() {
		return this.pdpSrNotifications;
	}

	public void setPdpSrNotifications(List<PdpSrNotification> pdpSrNotifications) {
		this.pdpSrNotifications = pdpSrNotifications;
	}

	public PdpSrNotification addPdpSrNotification(PdpSrNotification pdpSrNotification) {
		getPdpSrNotifications().add(pdpSrNotification);
		pdpSrNotification.setPdpSrVendor(this);

		return pdpSrNotification;
	}

	public PdpSrNotification removePdpSrNotification(PdpSrNotification pdpSrNotification) {
		getPdpSrNotifications().remove(pdpSrNotification);
		pdpSrNotification.setPdpSrVendor(null);

		return pdpSrNotification;
	}


	//bi-directional many-to-one association to PdpSrNotification
	@OneToMany(mappedBy="pdpSrVendor")
	public List<VendorConfig> getVendorConfigs() {
		return vendorConfigs;
	}


	public void setVendorConfigs(List<VendorConfig> vendorConfigs) {
		this.vendorConfigs = vendorConfigs;
	}

	public VendorConfig addVendorConfig(VendorConfig vendorConfig) {
		getVendorConfigs().add(vendorConfig);
		vendorConfig.setPdpSrVendor(this);

		return vendorConfig;
	}

	public VendorConfig removeVendorConfig(VendorConfig vendorConfig) {
		getVendorConfigs().remove(vendorConfig);
		vendorConfig.setPdpSrVendor(null);

		return vendorConfig;
	}
	
	@Override
	 public String toString() {
	     return String.format("%s[id=%d]", getClass().getSimpleName(), this.getVendorId());
	 }


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getPdpSrNotifications() == null) ? 0 : getPdpSrNotifications().hashCode());
		result = prime * result + ((getVendor() == null) ? 0 : getVendor().hashCode());
		result = prime * result + ((getVendorConfigs() == null) ? 0 : getVendorConfigs().hashCode());
		result = prime * result + ((getVendorId() == null) ? 0 : getVendorId().hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (Hibernate.getClass(this) != Hibernate.getClass(obj))
			return false;
		PdpSrVendor other = (PdpSrVendor) obj;
		if (getPdpSrNotifications() == null) {
			if (other.getPdpSrNotifications() != null)
				return false;
		} else if (!getPdpSrNotifications().equals(other.getPdpSrNotifications()))
			return false;
		if (getVendor() == null) {
			if (other.getVendor() != null)
				return false;
		} else if (!getVendor().equals(other.getVendor()))
			return false;
		if (getVendorConfigs() == null) {
			if (other.getVendorConfigs() != null)
				return false;
		} else if (!getVendorConfigs().equals(other.getVendorConfigs()))
			return false;
		if (getVendorId() == null) {
			if (other.getVendorId() != null)
				return false;
		} else if (!getVendorId().equals(other.getVendorId()))
			return false;
		return true;
	}

}