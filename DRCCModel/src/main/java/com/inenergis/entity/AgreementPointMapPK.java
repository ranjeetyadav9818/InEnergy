package com.inenergis.entity;

import lombok.EqualsAndHashCode;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The primary key class for the AGREEMENT_POINT_MAP database table.
 * 
 */
@Embeddable
@EqualsAndHashCode(of = {"servicePointId","serviceAgreementId"})
public class AgreementPointMapPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private String servicePointId;
	private String serviceAgreementId;

	public AgreementPointMapPK() {
	}

	public AgreementPointMapPK(String servicePointId, String serviceAgreementId) {
		this.servicePointId = servicePointId;
		this.serviceAgreementId = serviceAgreementId;
	}

	@Column(name="SERVICE_POINT_ID", insertable=false, updatable=false)
	public String getServicePointId() {
		return this.servicePointId;
	}
	public void setServicePointId(String servicePointId) {
		this.servicePointId = servicePointId;
	}

	@Column(name="SERVICE_AGREEMENT_ID", insertable=false, updatable=false)
	public String getServiceAgreementId() {
		return this.serviceAgreementId;
	}
	public void setServiceAgreementId(String serviceAgreementId) {
		this.serviceAgreementId = serviceAgreementId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!Hibernate.getClass(this).equals(Hibernate.getClass(other))) {
			return false;
		}
		AgreementPointMapPK castOther = (AgreementPointMapPK)other;
		return 
			this.getServicePointId().equals(castOther.getServicePointId())
			&& this.getServiceAgreementId().equals(castOther.getServiceAgreementId());
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.getServicePointId().hashCode();
		hash = hash * prime + this.getServiceAgreementId().hashCode();
		
		return hash;
	}

	@Override
	public String toString() {
		return "{" +
				"servicePointId=" + servicePointId +
				", serviceAgreementId=" + serviceAgreementId +
				'}';
	}
}