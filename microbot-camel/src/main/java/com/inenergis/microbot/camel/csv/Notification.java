package com.inenergis.microbot.camel.csv;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.util.Date;

@CsvRecord(separator=",")
public class Notification {
	/**
	ALL_NOTIFICATION_ID        | bigint(20)   | NO   | PRI | NULL    | auto_increment |
	| CREATION_TIMESTAMP         | varchar(255) | YES  |     | NULL    |                |
	| DRUID                      | varchar(255) | YES  |     | NULL    |                |
	| EVENT_CANCEL_STATUS        | varchar(255) | YES  |     | NULL    |                |
	| EVENT_DISPLAY_DAYNAME      | varchar(255) | YES  |     | NULL    |                |
	| EVENT_DISPLAY_EVENT_DATE   | varchar(255) | YES  |     | NULL    |                |
	| EVENT_DISPLAY_PREMISE_ADDR | varchar(255) | YES  |     | NULL    |                |
	| LANGUAGE                   | varchar(255) | YES  |     | NULL    |                |
	| MESSAGE_TEMPLATE           | varchar(255) | YES  |     | NULL    |                |
	| NOTIFY_BY                  | varchar(255) | YES  |     | NULL    |                |
	| NOTIFY_BY_VALUE            | varchar(255) | YES  |     | NULL    |                |
	| PDP_RESERVATION_CAPACITY   | varchar(255) | YES  |     | NULL    |                |
	| PERSON_ID                  | varchar(255) | YES  |     | NULL    |                |
	| PREFERENCE_CATEGORY        | varchar(255) | YES  |     | NULL    |                |
	| PREMISE_ID                 | varchar(255) | YES  |     | NULL    |                |
	| SERVICE_POINT_ID           | varchar(255) | YES  |     | NULL    |                |
	| PDPSR_EVENT_EVENT_ID       | bigint(20)   | YES  | MUL | NULL
	*/
	
	@DataField(pos=1)
	private Long recId;
	
	@DataField(pos=2)
	private String prefCategory;
	
	@DataField(pos=3)
	private String messageTemplate;
	
	@DataField(pos=4)
	private String servicePoint;
	
	@DataField(pos=5)
	private String premiseId;
	
	@DataField(pos=6)
	private String serviceAgreementId;
	
	@DataField(pos=7)
	private String accountId;
	
	@DataField(pos=8)
	private String preferenceId;
	
	@DataField(pos=9)
	private String personId;
	
	@DataField(pos=10)
	private String druid;
	
	@DataField(pos=11)
	private String numServiceAddress;
	
	@DataField(pos=12)
	private String notifyBy;
	
	@DataField(pos=13)
	private String notifyByValue;
	
	@DataField(pos=14)
	private String notifyWindow;
	
	@DataField(pos=15)
	private String language;
	
	@DataField(pos=16)
	private String defaultRecordInd;
	
	@DataField(pos=17)
	private String priority;
	
	@DataField(pos=18,pattern="MM/dd/yyyy HH:mm:ss" /*, pattern="yyyy-MM-dd HH:mm:ss"*/)
	private Date creationTimestamp;
	
	@DataField(pos=19,pattern="yyyyMMdd-HHmm"/*, pattern="yyyy-MM-dd HH:mm:ss"*/)
	private Date expirationTimestamp;
	
	
	
	
	/**
	 * Not in the CSV file, the Event ID of the participant
	 * @return
	 */
	private Long eventId;
	
	
	
	/**
	 * Not in the CSV file, the DB ID of the participant
	 * @return
	 */
	private Long participantId;
	
	private Long vendorId;
	
	private String vendor;
	
	private String secondDisplayAddress;
	
	private int additionalAddressCount;
	
	private String vendorStatus = "SENT TO VENDOR";
	
	public String getCleanedNotifyByValue(){
		String value = getNotifyByValue().replaceAll("['\"]", "");
		if("EMAIL".equalsIgnoreCase(this.getNotifyBy())){
			return value.toLowerCase();
		}else{
			value = value.replaceAll("[^+0-9]", "");
		}
		return value;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eventId == null) ? 0 : eventId.hashCode());
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((notifyBy == null) ? 0 : notifyBy.hashCode());
		result = prime * result + ((notifyByValue == null) ? 0 : notifyByValue.hashCode());
		result = prime * result + ((participantId == null) ? 0 : participantId.hashCode());
		result = prime * result + ((vendorId == null) ? 0 : vendorId.hashCode());
		return result;
	}

	/**
	 * Returns a key which is used to identify duplicates. All notifications that return the same key are duplicates to each other (one of them is original though)
	 * @return
	 */
	public String getDuplicateKey(){
		return String.format("%d_%d_%d", this.eventId,this.participantId,this.vendorId,this.getNotifyBy(),this.getNotifyByValue(),this.getLanguage());
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Notification other = (Notification) obj;
		if (eventId == null) {
			if (other.eventId != null)
				return false;
		} else if (!eventId.equals(other.eventId))
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (notifyBy == null) {
			if (other.notifyBy != null)
				return false;
		} else if (!notifyBy.equals(other.notifyBy))
			return false;
		if (notifyByValue == null) {
			if (other.notifyByValue != null)
				return false;
		} else if (!notifyByValue.equals(other.notifyByValue))
			return false;
		if (participantId == null) {
			if (other.participantId != null)
				return false;
		} else if (!participantId.equals(other.participantId))
			return false;
		if (vendorId == null) {
			if (other.vendorId != null)
				return false;
		} else if (!vendorId.equals(other.vendorId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Notification [recId=" + recId + ", prefCategory=" + prefCategory + ", druid=" + druid + ", notifyBy="
				+ notifyBy + ", notifyByValue=" + notifyByValue + ", language=" + language + ", eventId=" + eventId
				+ ", participantId=" + participantId + ", vendorId=" + vendorId + ", vendor=" + vendor + "]";
	}

	public Long getRecId() {
		return recId;
	}

	public void setRecId(Long recId) {
		this.recId = recId;
	}

	public String getPrefCategory() {
		return prefCategory;
	}

	public void setPrefCategory(String prefCategory) {
		this.prefCategory = prefCategory;
	}

	public String getMessageTemplate() {
		return messageTemplate;
	}

	public void setMessageTemplate(String messageTemplate) {
		this.messageTemplate = messageTemplate;
	}

	public String getServicePoint() {
		return servicePoint;
	}

	public void setServicePoint(String servicePoint) {
		this.servicePoint = servicePoint;
	}

	public String getPremiseId() {
		return premiseId;
	}

	public void setPremiseId(String premiseId) {
		this.premiseId = premiseId;
	}

	public String getServiceAgreementId() {
		return serviceAgreementId;
	}

	public void setServiceAgreementId(String serviceAgreementId) {
		this.serviceAgreementId = serviceAgreementId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getPreferenceId() {
		return preferenceId;
	}

	public void setPreferenceId(String preferenceId) {
		this.preferenceId = preferenceId;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getDruid() {
		return druid;
	}

	public void setDruid(String druid) {
		this.druid = druid;
	}

	public String getNumServiceAddress() {
		return numServiceAddress;
	}

	public void setNumServiceAddress(String numServiceAddress) {
		this.numServiceAddress = numServiceAddress;
	}

	public String getNotifyBy() {
		return notifyBy;
	}

	public void setNotifyBy(String notifyBy) {
		this.notifyBy = notifyBy;
	}

	public String getNotifyByValue() {
		return notifyByValue;
	}

	public void setNotifyByValue(String notifyByValue) {
		this.notifyByValue = notifyByValue;
	}

	public String getNotifyWindow() {
		return notifyWindow;
	}

	public void setNotifyWindow(String notifyWindow) {
		this.notifyWindow = notifyWindow;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getDefaultRecordInd() {
		return defaultRecordInd;
	}

	public void setDefaultRecordInd(String defaultRecordInd) {
		this.defaultRecordInd = defaultRecordInd;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public Date getExpirationTimestamp() {
		return expirationTimestamp;
	}

	public void setExpirationTimestamp(Date expirationTimestamp) {
		this.expirationTimestamp = expirationTimestamp;
	}

	public String getDedup() {
		return dedup;
	}

	public void setDedup(String dedup) {
		this.dedup = dedup;
	}

	public String getEventDisplayName() {
		return eventDisplayName;
	}

	public void setEventDisplayName(String eventDisplayName) {
		this.eventDisplayName = eventDisplayName;
	}

	public String getEventDisplayDate() {
		return eventDisplayDate;
	}

	public void setEventDisplayDate(String eventDisplayDate) {
		this.eventDisplayDate = eventDisplayDate;
	}

	public String getEventCancelStatus() {
		return eventCancelStatus;
	}

	public void setEventCancelStatus(String eventCancelStatus) {
		this.eventCancelStatus = eventCancelStatus;
	}

	public String getEventDisplayPremiseAddress() {
		return eventDisplayPremiseAddress;
	}

	public void setEventDisplayPremiseAddress(String eventDisplayPremiseAddress) {
		this.eventDisplayPremiseAddress = eventDisplayPremiseAddress;
	}

	public String getPdpReservationCapacity() {
		return pdpReservationCapacity;
	}

	public void setPdpReservationCapacity(String pdpReservationCapacity) {
		this.pdpReservationCapacity = pdpReservationCapacity;
	}

	public Long getParticipantId() {
		return participantId;
	}

	public void setParticipantId(Long participantId) {
		this.participantId = participantId;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public String getSecondDisplayAddress() {
		return secondDisplayAddress;
	}

	public void setSecondDisplayAddress(String secondDisplayAddress) {
		this.secondDisplayAddress = secondDisplayAddress;
	}

	public int getAdditionalAddressCount() {
		return additionalAddressCount;
	}

	public void setAdditionalAddressCount(int additionalAddressCount) {
		this.additionalAddressCount = additionalAddressCount;
	}

	public String getVendorStatus() {
		return vendorStatus;
	}

	public void setVendorStatus(String vendorStatus) {
		this.vendorStatus = vendorStatus;
	}

	@DataField(pos=20)
	private String dedup;
	
	@DataField(pos=21)
	private String eventDisplayName;
	
	@DataField(pos=22)
	private String eventDisplayDate;
	
	@DataField(pos=23)
	private String eventCancelStatus;
	
	@DataField(pos=24)
	private String eventDisplayPremiseAddress;
	
	@DataField(pos=25)
	private String pdpReservationCapacity;
}