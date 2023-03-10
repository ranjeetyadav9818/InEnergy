package com.inenergis.entity;

import com.inenergis.entity.PdpSrParticipant.SuccessfulNotificationType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;


/**
 * The persistent class for the VENDOR_STATUS_MAPPING database table.
 * 
 */
@Entity
@Table(name="VENDOR_STATUS_MAPPING")
@NamedQuery(name="VendorStatusMapping.findAll", query="SELECT v FROM VendorStatusMapping v")
public class VendorStatusMapping implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long vendorStatusMappingId;
	private String displayMessage;
	private String vendorMessage;
	private String statusCode;
	private SuccessfulNotificationType successfulNotification;
	private PdpSrVendor vendor;

	public VendorStatusMapping() {
	}


	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name="VENDOR_STATUS_MAPPING_ID")
	public Long getVendorStatusMappingId() {
		return this.vendorStatusMappingId;
	}

	public void setVendorStatusMappingId(Long vendorStatusMappingId) {
		this.vendorStatusMappingId = vendorStatusMappingId;
	}


	@Column(name="DISPLAY_MESSAGE")
	public String getDisplayMessage() {
		return this.displayMessage;
	}

	public void setDisplayMessage(String displayMessage) {
		this.displayMessage = displayMessage;
	}


	@Column(name="STATUS_CODE")
	public String getStatusCode() {
		return this.statusCode;
	}

	public void setStatusCode(String statusCode) {
//		System.out.println("calling setStatCode: " + statusCode);
		this.statusCode = statusCode;
	}


	@Column(name="SUCCESSFUL_NOTIFICATION")
	@Enumerated(EnumType.STRING)
	public SuccessfulNotificationType getSuccessfulNotification() {
		return this.successfulNotification;
	}

	public void setSuccessfulNotification(SuccessfulNotificationType successfulNotification) {
//		System.out.println("calling setNot: " + successfulNotification);
		this.successfulNotification = successfulNotification;
	}


	@ManyToOne
	@JoinColumn(name="VENDOR_ID")
//	@Transient
	public PdpSrVendor getVendor() {
		return this.vendor;
	}

	public void setVendor(PdpSrVendor vendor) {
//		System.out.println("calling setvendor: " + vendor);
		this.vendor = vendor;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("VendorStatusMapping [vendorStatusMappingId=");
		builder.append(vendorStatusMappingId);
		builder.append(", displayMessage=");
		builder.append(displayMessage);
		builder.append(", statusCode=");
		builder.append(statusCode);
		builder.append(", successfulNotification=");
		builder.append(successfulNotification);
		builder.append(", vendor=");
		builder.append(vendor);
		builder.append("]");
		return builder.toString();
	}


	@Column(name="VENDOR_DESCRIPTION")
	public String getVendorMessage() {
		return vendorMessage;
	}


	public void setVendorMessage(String vendorMessage) {
		this.vendorMessage = vendorMessage;
	}
	
	

}