package com.inenergis.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;


/**
 * The persistent class for the CUSTOMER_NOTIFICATION_PREFERENCE database table.
 * 
 */
@Entity
@Table(name="GAS_CUSTOMER_NOTIFICATION_PREFERENCE")
public class GasCustomerNotificationPreference implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long customerNotificationPreferenceId;
	private boolean notificationDncFlag;
	private String notificationLanguage;
	private String notificationProgram;
	private String notificationTypeId;
	private String notificationValue;
	private String personId;
	private Person gasPerson;
	private String saId;
	private GasServiceAgreement serviceAgreement;
	private String uniqSaId;
	private Date startDate;
	private Date endDate;
	private String phoneExtension;

	public GasCustomerNotificationPreference() {
	}

	@Transient
	public String getRowKey(){
		return this.getCustomerNotificationPreferenceId().toString();
	}

	@Id
	@Column(name="GAS_CUSTOMER_NOTIFICATION_PREFERENCE_ID")
	@GeneratedValue(strategy = IDENTITY)
	public Long getCustomerNotificationPreferenceId() {
		return this.customerNotificationPreferenceId;
	}

	public void setCustomerNotificationPreferenceId(Long customerNotificationPreferenceId) {
		this.customerNotificationPreferenceId = customerNotificationPreferenceId;
	}


	@Column(name="NOTIFICATION_DNC_FLAG")
	public boolean getNotificationDncFlag() {
		return this.notificationDncFlag;
	}

	public void setNotificationDncFlag(boolean notificationDncFlag) {
		this.notificationDncFlag = notificationDncFlag;
	}


	@Column(name="NOTIFICATION_LANGUAGE")
	public String getNotificationLanguage() {
		return this.notificationLanguage;
	}

	public void setNotificationLanguage(String notificationLanguage) {
		this.notificationLanguage = notificationLanguage;
	}


	@Column(name="NOTIFICATION_PROGRAM")
	public String getNotificationProgram() {
		return this.notificationProgram;
	}

	public void setNotificationProgram(String notificationProgram) {
		this.notificationProgram = notificationProgram;
	}

	@Column(name="NOTIFICATION_TYPE_ID")
	public String getNotificationTypeId() {
		return notificationTypeId;
	}


	public void setNotificationTypeId(String notificationTypeId) {
		this.notificationTypeId = notificationTypeId;
	}


	@Column(name="NOTIFICATION_VALUE")
	public String getNotificationValue() {
		return this.notificationValue;
	}

	public void setNotificationValue(String notificationValue) {
		this.notificationValue = notificationValue;
	}


	@Column(name="PERSON_ID", insertable=false, updatable=false)
	public String getPersonId() {
		return this.personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}


	//bi-directional many-to-one association to Service Agreement
	@ManyToOne
	@JoinColumn(name="PERSON_ID")
	@NotFound(action=NotFoundAction.IGNORE)
	public Person getPerson() {
		return gasPerson;
	}


	public void setPerson(Person gasPerson) {
		this.gasPerson = gasPerson;
	}


	@Column(name="SA_ID", insertable=false, updatable=false)
	public String getSaId() {
		return this.saId;
	}

	public void setSaId(String saId) {
		this.saId = saId;
	}


	//bi-directional many-to-one association to Service Agreement
	@ManyToOne
	@JoinColumn(name="SA_ID")
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonManagedReference
	public GasServiceAgreement getServiceAgreement() {
		return serviceAgreement;
	}


	public void setServiceAgreement(GasServiceAgreement serviceAgreement) {
		this.serviceAgreement = serviceAgreement;
	}


	@Column(name="UNIQ_SA_ID")
	public String getUniqSaId() {
		return this.uniqSaId;
	}

	public void setUniqSaId(String uniqSaId) {
		this.uniqSaId = uniqSaId;
	}

	@Column(name="START_DATE")
	public Date getStartDate() {
		return startDate;
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name="END_DATE")
	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name="PHONE_EXTENSION")
	public String getPhoneExtension() {
		return phoneExtension;
	}


	public void setPhoneExtension(String phoneExtension) {
		this.phoneExtension = phoneExtension;
	}

}