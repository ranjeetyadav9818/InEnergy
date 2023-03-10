package com.inenergis.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;


/**
 * The persistent class for the PREMISE database table.
 * 
 */
@Entity
@Table(name="PREMISE")
@NamedQuery(name="Premise.findAll", query="SELECT p FROM Premise p")
public class Premise implements Serializable, VModelEntity {
	private static final long serialVersionUID = 1L;
	private String premiseId;
	private String serviceAddress1;
	private String serviceAddress2;
	private String serviceCityUpr;
	private String servicePostal;
	private String serviceState;
	private String baseLineChar;
	private String county;
	private String premiseType;
	private List<BaseServicePoint> servicePoints;

	public Premise() {
	}


	@Id
	@Column(name="PREMISE_ID")
	public String getPremiseId() {
		return this.premiseId;
	}

	public void setPremiseId(String premiseId) {
		this.premiseId = premiseId;
	}


	@Column(name="SERVICE_ADDRESS1")
	public String getServiceAddress1() {
		return this.serviceAddress1;
	}

	public void setServiceAddress1(String serviceAddress1) {
		this.serviceAddress1 = serviceAddress1;
	}


	@Column(name="SERVICE_ADDRESS2")
	public String getServiceAddress2() {
		return this.serviceAddress2;
	}

	public void setServiceAddress2(String serviceAddress2) {
		this.serviceAddress2 = serviceAddress2;
	}


	@Column(name="SERVICE_CITY_UPR")
	public String getServiceCityUpr() {
		return this.serviceCityUpr;
	}

	public void setServiceCityUpr(String serviceCityUpr) {
		this.serviceCityUpr = serviceCityUpr;
	}


	@Column(name="SERVICE_POSTAL")
	public String getServicePostal() {
		return this.servicePostal;
	}

	public void setServicePostal(String servicePostal) {
		this.servicePostal = servicePostal;
	}


	@Column(name="SERVICE_STATE")
	public String getServiceState() {
		return this.serviceState;
	}

	public void setServiceState(String serviceState) {
		this.serviceState = serviceState;
	}

	@Column(name="PREM_BASELINE_CHAR")
	public String getBaseLineChar() {
		return baseLineChar;
	}


	public void setBaseLineChar(String baseLineChar) {
		this.baseLineChar = baseLineChar;
	}

	@Column(name="COUNTY")
	public String getCounty() {
		return county;
	}


	public void setCounty(String county) {
		this.county = county;
	}

	@Column(name="PREMISE_TYPE")
	public String getPremiseType() {
		return premiseType;
	}

	public void setPremiseType(String premiseType) {
		this.premiseType = premiseType;
	}

	//bi-directional many-to-one association to ServicePoint
	@OneToMany(mappedBy="premise")
	@JsonBackReference
	public List<BaseServicePoint> getServicePoints() {
		return this.servicePoints;
	}

	public void setServicePoints(List<BaseServicePoint> servicePoints) {
		this.servicePoints = servicePoints;
	}

	public ServicePoint addServicePoint(ServicePoint servicePoint) {
		getServicePoints().add(servicePoint);
		servicePoint.setPremise(this);

		return servicePoint;
	}

	public ServicePoint removeServicePoint(ServicePoint servicePoint) {
		getServicePoints().remove(servicePoint);
		servicePoint.setPremise(null);

		return servicePoint;
	}
	
	@Transient
	public String getSPIds(){
		StringBuilder s = new StringBuilder();
		if(getServicePoints()!=null){
			for(BaseServicePoint sp : this.getServicePoints()){
				if(s.length()!=0){
					s.append(", ");
				} else {
					s.append(sp.getServicePointId());
				}
			}
		}
		return s.toString();
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Premise premise = (Premise) o;

		if (premiseId != null ? !premiseId.equals(premise.premiseId) : premise.premiseId != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return premiseId != null ? premiseId.hashCode() : 0;
	}

	public String idFieldName() {
		return "premiseId";
	}

	public List<String> relationshipFieldNames() {
		return null;
	}

	public List<String> excludedFieldsToCompare() {
		return Arrays.asList("servicePoints","SPIds");
	}
}