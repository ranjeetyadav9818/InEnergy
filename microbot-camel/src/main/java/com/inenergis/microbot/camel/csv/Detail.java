package com.inenergis.microbot.camel.csv;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.io.Serializable;
import java.util.Date;

@CsvRecord(separator=",")
public class Detail implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@DataField(pos=1)
	private String uniqueId;

	@DataField(pos=2)
	private String eventProgram;
	
	@DataField(pos=3)
	private String eventName;
	
	@DataField(pos=4, pattern="yyyy-MM-dd HH:mm:ss")
	private Date eventStartDateTime;
	
	@DataField(pos=5, pattern="yyyy-MM-dd HH:mm:ss")
	private Date eventEndDateTime;
	
	@DataField(pos=6)
	private String eventState;
	
	@DataField(pos=7)
	private String eventOptions;
	
	@DataField(pos=8)
	private String eventType;
	
	
	
	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getEventProgram() {
		return eventProgram;
	}

	public void setEventProgram(String eventProgram) {
		this.eventProgram = eventProgram;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public Date getEventStartDateTime() {
		return eventStartDateTime;
	}

	public void setEventStartDateTime(Date eventStartDateTime) {
		this.eventStartDateTime = eventStartDateTime;
	}

	public Date getEventEndDateTime() {
		return eventEndDateTime;
	}

	public void setEventEndDateTime(Date eventEndDateTime) {
		this.eventEndDateTime = eventEndDateTime;
	}

	public String getEventState() {
		return eventState;
	}

	public void setEventState(String eventState) {
		this.eventState = eventState;
	}

	public String getEventOptions() {
		return eventOptions;
	}

	public void setEventOptions(String eventOptions) {
		this.eventOptions = eventOptions;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	@Override
	public String toString() {
		return "Detail [uniqueId=" + uniqueId + ", eventProgram=" + eventProgram + ", eventName=" + eventName
				+ ", eventStartDateTime=" + eventStartDateTime + ", eventEndDateTime=" + eventEndDateTime
				+ ", eventState=" + eventState + ", eventOptions=" + eventOptions + ", eventType=" + eventType + "]";
	}
	
	
	
}
