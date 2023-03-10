package com.inenergis.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;


/**
 * The persistent class for the VENDOR_CONFIGURATION database table.
 * 
 */
@Entity
@Table(name="VENDOR_CONFIGURATION")
public class VendorConfig implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long vendorConfigId;
	private String notifyBy;
	private PdpSrVendor pdpSrVendor;
	private String program;
	private String sftpAddress;
	private String sftpDirectory;
	private String sftpPassword;
	private String sftpUsername;


	@Id
	@Column(name="VENDOR_CONFIG_ID")
	@GeneratedValue(strategy = IDENTITY)
	public Long getVendorConfigId() {
		return this.vendorConfigId;
	}

	public void setVendorConfigId(Long vendorConfigId) {
		this.vendorConfigId = vendorConfigId;
	}


	@Column(name="NOTIFY_BY")
	public String getNotifyBy() {
		return this.notifyBy;
	}

	public void setNotifyBy(String notifyBy) {
		this.notifyBy = notifyBy;
	}


	@Column(name="PROGRAM")
	public String getProgram() {
		return this.program;
	}

	public void setProgram(String program) {
		this.program = program;
	}


	@Column(name="SFTP_ADDRESS")
	public String getSftpAddress() {
		return this.sftpAddress;
	}

	public void setSftpAddress(String sftpAddress) {
		this.sftpAddress = sftpAddress;
	}


	@Column(name="SFTP_DIRECTORY")
	public String getSftpDirectory() {
		return this.sftpDirectory;
	}

	public void setSftpDirectory(String sftpDirectory) {
		this.sftpDirectory = sftpDirectory;
	}


	@Column(name="SFTP_PASSWORD")
	public String getSftpPassword() {
		return this.sftpPassword;
	}

	public void setSftpPassword(String sftpPassword) {
		this.sftpPassword = sftpPassword;
	}


	@Column(name="SFTP_USERNAME")
	public String getSftpUsername() {
		return this.sftpUsername;
	}

	public void setSftpUsername(String sftpUsername) {
		this.sftpUsername = sftpUsername;
	}

	//bi-directional many-to-one association to PdpSrVendor
	@ManyToOne
	@JoinColumn(name="PDP_SR_VENDOR_VENDOR_ID")
	public PdpSrVendor getPdpSrVendor() {
		return pdpSrVendor;
	}

	public void setPdpSrVendor(PdpSrVendor pdpSrVendor) {
		this.pdpSrVendor = pdpSrVendor;
	}

}