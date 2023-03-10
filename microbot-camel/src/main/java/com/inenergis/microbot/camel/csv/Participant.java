package com.inenergis.microbot.camel.csv;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.util.Date;

@CsvRecord(separator=",")
public class Participant {

	@DataField(pos=1)
	String preferenceCategory;
	
	@DataField(pos=2)
	String messageTemplate;
	
	@DataField(pos=3)
	String servicePointId;
	
	@DataField(pos=4)
	String preferenceOverrides;
	
	@DataField(pos=5)
	String premiseID;
	
	@DataField(pos=6)
	String saId;
	
	@DataField(pos=7)
	String acctId;
	
	@DataField(pos=8)
	String myEnergyId; //DRUID
	
	@DataField(pos=9)
	String serviceAddress;
	
	@DataField(pos=10)
	String defaultNotifyBy;
	
	@DataField(pos=11)
	String defaultNotifyByValue;
	
	@DataField(pos=12)
	String defaultNotifyAfterHrs;
	
	@DataField(pos=13)
	String priorty;
	
	@DataField(pos=14, pattern="MM/dd/yyyy HH:mm:ss")
	Date createtionTimestamp;
	
	@DataField(pos=15, pattern="yyyyMMdd-HHmm")
	Date expirationTimestamp;
	
	@DataField(pos=16)
	String depup;
	
	@DataField(pos=17)
	String eventDateOfWeek;
	
	@DataField(pos=18/*, pattern="yyyy-MM-dd HH:mm:ss"*/)
	String eventDisplayDate;
	
	@DataField(pos=19)
	String eventCancelStatus;
	
	@DataField(pos=20)
	String premiseDisplayAddress;
	
	/**
	 * This is not in the original CSV. This is the DB event ID
	 */
	private Long eventId;
	
	public String getPreferenceCategory() {
		return preferenceCategory;
	}

	public void setPreferenceCategory(String preferenceCategory) {
		this.preferenceCategory = preferenceCategory;
	}

	public String getMessageTemplate() {
		return messageTemplate;
	}

	public void setMessageTemplate(String messageTemplate) {
		this.messageTemplate = messageTemplate;
	}

	public String getServicePointId() {
		return servicePointId;
	}

	public void setServicePointId(String servicePointId) {
		this.servicePointId = servicePointId;
	}

	public String getPreferenceOverrides() {
		return preferenceOverrides;
	}

	public void setPreferenceOverrides(String preferenceOverrides) {
		this.preferenceOverrides = preferenceOverrides;
	}

	public String getPremiseID() {
		return premiseID;
	}

	public void setPremiseID(String premiseID) {
		this.premiseID = premiseID;
	}

	public String getSaId() {
		return saId;
	}

	public void setSaId(String saId) {
		this.saId = saId;
	}

	public String getAcctId() {
		return acctId;
	}

	public void setAcctId(String acctId) {
		this.acctId = acctId;
	}

	public String getMyEnergyId() {
		return myEnergyId;
	}

	public void setMyEnergyId(String myEnergyId) {
		this.myEnergyId = myEnergyId;
	}

	public String getServiceAddress() {
		return serviceAddress;
	}

	public void setServiceAddress(String serviceAddress) {
		this.serviceAddress = serviceAddress;
	}

	public String getDefaultNotifyBy() {
		return defaultNotifyBy;
	}

	public void setDefaultNotifyBy(String defaultNotifyBy) {
		this.defaultNotifyBy = defaultNotifyBy;
	}

	public String getDefaultNotifyByValue() {
		return defaultNotifyByValue;
	}

	public void setDefaultNotifyByValue(String defaultNotifyByValue) {
		this.defaultNotifyByValue = defaultNotifyByValue;
	}

	public String getDefaultNotifyAfterHrs() {
		return defaultNotifyAfterHrs;
	}

	public void setDefaultNotifyAfterHrs(String defaultNotifyAfterHrs) {
		this.defaultNotifyAfterHrs = defaultNotifyAfterHrs;
	}

	public String getPriorty() {
		return priorty;
	}

	public void setPriorty(String priorty) {
		this.priorty = priorty;
	}

	public Date getCreatetionTimestamp() {
		return createtionTimestamp;
	}

	public void setCreatetionTimestamp(Date createtionTimestamp) {
		this.createtionTimestamp = createtionTimestamp;
	}

	public Date getExpirationTimestamp() {
		return expirationTimestamp;
	}

	public void setExpirationTimestamp(Date expirationTimestamp) {
		this.expirationTimestamp = expirationTimestamp;
	}

	public String getDepup() {
		return depup;
	}

	public void setDepup(String depup) {
		this.depup = depup;
	}

	public String getEventDateOfWeek() {
		return eventDateOfWeek;
	}

	public void setEventDateOfWeek(String eventDateOfWeek) {
		this.eventDateOfWeek = eventDateOfWeek;
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

	public String getPremiseDisplayAddress() {
		return premiseDisplayAddress;
	}

	public void setPremiseDisplayAddress(String premiseDisplayAddress) {
		this.premiseDisplayAddress = premiseDisplayAddress;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	
	
}
