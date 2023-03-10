package com.inenergis.entity;

import com.inenergis.entity.PdpSrParticipant.SuccessfulNotificationType;

import javax.persistence.CascadeType;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;


/**
 * The persistent class for the PDP_SR_NOTIFICATIONS database table.
 * 
 */
@Entity
@Table(name="PDP_SR_NOTIFICATIONS")
@NamedQuery(name="PdpSrNotification.findAll", query="SELECT p FROM PdpSrNotification p")
public class PdpSrNotification implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long notificationId;
	private Date creationTimestamp;
	private String eventCancelStatus;
	private String eventDisplayDayname;
	private String eventDisplayEventDate;
	private String eventDisplayPremiseAddr;
	private String language;
	private String messageTemplate;
	private String notifyBy;
	private String notifyByValue;
	private String pdpReservationCapacity;
	private String personId;
	private String preferenceCategory;
	private String vendorStatus;
	private String vendorStatusDisplayMessage;
	private SuccessfulNotificationType successfulNotification; 
	private int vendorStatusCounter;
	private Date vendorStatusTimestamp;
	private PdpSrParticipant pdpSrParticipant;
	private PdpSrEvent pdpSrEvent;
	private PdpSrVendor pdpSrVendor;
	private PdpSrNotification duplicateOf;
	private String voiceRecordFileName;

	public PdpSrNotification() {
	}


	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name="NOTIFICATION_ID")
	public Long getNotificationId() {
		return this.notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATION_TIMESTAMP")
	public Date getCreationTimestamp() {
		return this.creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}


	@Column(name="EVENT_CANCEL_STATUS")
	public String getEventCancelStatus() {
		return this.eventCancelStatus;
	}

	public void setEventCancelStatus(String eventCancelStatus) {
		this.eventCancelStatus = eventCancelStatus;
	}


	@Column(name="EVENT_DISPLAY_DAYNAME")
	public String getEventDisplayDayname() {
		return this.eventDisplayDayname;
	}

	public void setEventDisplayDayname(String eventDisplayDayname) {
		this.eventDisplayDayname = eventDisplayDayname;
	}


	@Column(name="EVENT_DISPLAY_EVENT_DATE")
	public String getEventDisplayEventDate() {
		return this.eventDisplayEventDate;
	}

	public void setEventDisplayEventDate(String eventDisplayEventDate) {
		this.eventDisplayEventDate = eventDisplayEventDate;
	}


	@Column(name="EVENT_DISPLAY_PREMISE_ADDR")
	public String getEventDisplayPremiseAddr() {
		return this.eventDisplayPremiseAddr;
	}

	public void setEventDisplayPremiseAddr(String eventDisplayPremiseAddr) {
		this.eventDisplayPremiseAddr = eventDisplayPremiseAddr;
	}


	@Column(name="LANGUAGE")
	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}


	@Column(name="MESSAGE_TEMPLATE")
	public String getMessageTemplate() {
		return this.messageTemplate;
	}

	public void setMessageTemplate(String messageTemplate) {
		this.messageTemplate = messageTemplate;
	}


	@Column(name="NOTIFY_BY")
	public String getNotifyBy() {
		return this.notifyBy;
	}

	public void setNotifyBy(String notifyBy) {

		this.notifyBy = notifyBy;
	}


	@Column(name="NOTIFY_BY_VALUE")
	public String getNotifyByValue() {
		return this.notifyByValue;
	}

	public void setNotifyByValue(String notifyByValue) {
		this.notifyByValue = notifyByValue;
	}


	@Column(name="PDP_RESERVATION_CAPACITY")
	public String getPdpReservationCapacity() {
		return this.pdpReservationCapacity;
	}

	public void setPdpReservationCapacity(String pdpReservationCapacity) {
		this.pdpReservationCapacity = pdpReservationCapacity;
	}


	@Column(name="PERSON_ID")
	public String getPersonId() {
		return this.personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}


	@Column(name="PREFERENCE_CATEGORY")
	public String getPreferenceCategory() {
		return this.preferenceCategory;
	}

	public void setPreferenceCategory(String preferenceCategory) {
		this.preferenceCategory = preferenceCategory;
	}


	@Column(name="VENDOR_STATUS")
	public String getVendorStatus() {
		return this.vendorStatus;
	}

	public void setVendorStatus(String vendorStatus) {
		this.vendorStatus = vendorStatus;
	}


	@Column(name="VENDOR_STATUS_COUNTER")
	public int getVendorStatusCounter() {
		return this.vendorStatusCounter;
	}

	public void setVendorStatusCounter(int vendorStatusCounter) {
		this.vendorStatusCounter = vendorStatusCounter;
	}


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "VENDOR_STATUS_TIMESTAMP")
	public Date getVendorStatusTimestamp() {
		return this.vendorStatusTimestamp;
	}

	public void setVendorStatusTimestamp(Date vendorStatusTimestamp) {
		this.vendorStatusTimestamp = vendorStatusTimestamp;
	}


	//bi-directional many-to-one association to PdpSrParticipant
	@ManyToOne
	@JoinColumn(name="PDP_SR_PARTICIPANTS_PARTICIPANT_ID")
	public PdpSrParticipant getPdpSrParticipant() {
		return this.pdpSrParticipant;
	}

	public void setPdpSrParticipant(PdpSrParticipant pdpSrParticipant) {
		this.pdpSrParticipant = pdpSrParticipant;
	}


	//bi-directional many-to-one association to PdpSrEvent
	@ManyToOne
	@JoinColumn(name="PDP_SR_EVENT_EVENT_ID")
	public PdpSrEvent getPdpSrEvent() {
		return this.pdpSrEvent;
	}

	public void setPdpSrEvent(PdpSrEvent pdpSrEvent) {
		this.pdpSrEvent = pdpSrEvent;
	}


	//bi-directional many-to-one association to PdpSrVendor
	@ManyToOne
	@JoinColumn(name="PDP_SR_VENDOR_VENDOR_ID")
	public PdpSrVendor getPdpSrVendor() {
		return this.pdpSrVendor;
	}

	public void setPdpSrVendor(PdpSrVendor pdpSrVendor) {
		this.pdpSrVendor = pdpSrVendor;
	}


	@Column(name="DISPLAY_MESSAGE")
	public String getVendorStatusDisplayMessage() {
		return vendorStatusDisplayMessage;
	}


	public void setVendorStatusDisplayMessage(String vendorStatusDisplayMessage) {
		this.vendorStatusDisplayMessage = vendorStatusDisplayMessage;
	}

	
	@Column(name="SUCCESSFUL_NOTIFICATION")
	@Enumerated(EnumType.STRING)
	public SuccessfulNotificationType getSuccessfulNotification() {
		return successfulNotification;
	}


	public void setSuccessfulNotification(SuccessfulNotificationType successfulNotification) {
		this.successfulNotification = successfulNotification;
	}

	@Column(name = "VOICE_FILE_NAME")
	public String getVoiceRecordFileName() {
		return voiceRecordFileName;
	}

	public void setVoiceRecordFileName(String voiceRecordFileName) {
		this.voiceRecordFileName = voiceRecordFileName;
	}

	@Transient
	public boolean isDelivered(){
		if(SuccessfulNotificationType.DELIVERED.equals(this.getSuccessfulNotification())){
			return true;
		} else {
			return false;
		}
	}
	
	@Transient
	public boolean isAttempted(){
		if(SuccessfulNotificationType.ATTEMPTED.equals(this.getSuccessfulNotification())){
			return true;
		} else {
			return false;
		}
	}
	
	@Transient
	public boolean isUnsuccessful(){
		if(SuccessfulNotificationType.UNSUCCESS.equals(this.getSuccessfulNotification())){
			return true;
		} else {
			return false;
		}
	}


	@ManyToOne
	@JoinColumn(name="DUPLICATE_OF")
	public PdpSrNotification getDuplicateOf() {
		return duplicateOf;
	}

	public void setDuplicateOf(PdpSrNotification duplicateOf) {
		this.duplicateOf = duplicateOf;
	}

}