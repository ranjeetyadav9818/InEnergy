package com.inenergis.microbot.camel.csv;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.util.Date;

@CsvRecord(separator=",")
public class Postback {

	@DataField(pos=1)
	private String drccNotifRec;//the notification id
	
	@DataField(pos=2)
	private String contactVendor;
	@DataField(pos=3, pattern="yyyy-MM-dd HH:mm:ss")
	private Date contactTimestamp;
	@DataField(pos=4)
	private String notifyBy; //called contactChannel but in all other places notify by is used
	@DataField(pos=5)
	private String notifyByValue;//called contact value but in all other places it is notify by value
	@DataField(pos=6)
	private String contactStatus;
	
	
	public String getDrccNotifRec() {
		return drccNotifRec;
	}
	public void setDrccNotifRec(String drccNotifRec) {
		this.drccNotifRec = drccNotifRec;
	}
	public String getContactVendor() {
		return contactVendor;
	}
	public void setContactVendor(String contactVendor) {
		this.contactVendor = contactVendor;
	}
	public Date getContactTimestamp() {
		return contactTimestamp;
	}
	public void setContactTimestamp(Date contactTimestamp) {
		this.contactTimestamp = contactTimestamp;
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
	public String getContactStatus() {
		return contactStatus;
	}
	public void setContactStatus(String contactStatus) {
		this.contactStatus = contactStatus;
	}
	
}
