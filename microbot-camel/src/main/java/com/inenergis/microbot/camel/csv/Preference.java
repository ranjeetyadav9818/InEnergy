package com.inenergis.microbot.camel.csv;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.util.Date;

@CsvRecord(separator = "¶")
public class Preference {
	
	@DataField(pos = 1)
	String NOTIFICATION_OPTION_ID;
	
	@DataField(pos = 2)
	String SA_ID;
	
	@DataField(pos = 3)
	String PERSON_ID;
	
	@DataField(pos = 4)
	String NOTIFICATION_PROGRAM;
	
	@DataField(pos = 5)
	String NOTIFICATION_TYPE;
	
	@DataField(pos = 6)
	String NOTIFICATION_VALUE;
	
	@DataField(pos = 7)
	String NOTIFICATION_LANGUAGE;
	
	@DataField(pos = 8)
	String NOTIFICATION_DNC_FLAG;

	@DataField(pos = 9,pattern="MM/dd/yyyy")
	Date NOTIFICATION_EFF_STARTDT;

	@DataField(pos = 10,pattern="MM/dd/yyyy")
	Date NOTIFICATION_EFF_ENDDT;

	@DataField(pos = 11)
	String NOTIFICATION_VALUE_EXTENSION;
	
	boolean dncFlag;

	public String getNOTIFICATION_OPTION_ID() {
		return NOTIFICATION_OPTION_ID;
	}

	public void setNOTIFICATION_OPTION_ID(String nOTIFICATION_OPTION_ID) {
		NOTIFICATION_OPTION_ID = nOTIFICATION_OPTION_ID;
	}

	public String getSA_ID() {
		return SA_ID;
	}

	public void setSA_ID(String sA_ID) {
		SA_ID = sA_ID;
	}

	public String getPERSON_ID() {
		return PERSON_ID;
	}

	public void setPERSON_ID(String pERSON_ID) {
		PERSON_ID = pERSON_ID;
	}

	public String getNOTIFICATION_PROGRAM() {
		return NOTIFICATION_PROGRAM;
	}

	public void setNOTIFICATION_PROGRAM(String nOTIFICATION_PROGRAM) {
		NOTIFICATION_PROGRAM = nOTIFICATION_PROGRAM;
	}

	public String getNOTIFICATION_TYPE() {
		return NOTIFICATION_TYPE;
	}

	public void setNOTIFICATION_TYPE(String nOTIFICATION_TYPE) {
		NOTIFICATION_TYPE = nOTIFICATION_TYPE;
	}

	public String getNOTIFICATION_VALUE() {
		return NOTIFICATION_VALUE;
	}

	public void setNOTIFICATION_VALUE(String nOTIFICATION_VALUE) {
		NOTIFICATION_VALUE = nOTIFICATION_VALUE;
	}

	public String getNOTIFICATION_LANGUAGE() {
		return NOTIFICATION_LANGUAGE;
	}

	public void setNOTIFICATION_LANGUAGE(String nOTIFICATION_LANGUAGE) {
		NOTIFICATION_LANGUAGE = nOTIFICATION_LANGUAGE;
	}

	public String getNOTIFICATION_DNC_FLAG() {
		return NOTIFICATION_DNC_FLAG;
	}

	public void setNOTIFICATION_DNC_FLAG(String nOTIFICATION_DNC_FLAG) {
		NOTIFICATION_DNC_FLAG = nOTIFICATION_DNC_FLAG;
		if("Y".equalsIgnoreCase(NOTIFICATION_DNC_FLAG)){
			this.dncFlag = true;
		}
	}

	public boolean isDncFlag() {
		return dncFlag;
	}

	public void setDncFlag(boolean dncFlag) {
		this.dncFlag = dncFlag;
	}

	public Date getNOTIFICATION_EFF_STARTDT() {
		return NOTIFICATION_EFF_STARTDT;
	}

	public void setNOTIFICATION_EFF_STARTDT(Date NOTIFICATION_EFF_STARTDT) {
		this.NOTIFICATION_EFF_STARTDT = NOTIFICATION_EFF_STARTDT;
	}

	public Date getNOTIFICATION_EFF_ENDDT() {
		return NOTIFICATION_EFF_ENDDT;
	}

	public void setNOTIFICATION_EFF_ENDDT(Date NOTIFICATION_EFF_ENDDT) {
		this.NOTIFICATION_EFF_ENDDT = NOTIFICATION_EFF_ENDDT;
	}

	public String getNOTIFICATION_VALUE_EXTENSION() {
		return NOTIFICATION_VALUE_EXTENSION;
	}

	public void setNOTIFICATION_VALUE_EXTENSION(String NOTIFICATION_VALUE_EXTENSION) {
		this.NOTIFICATION_VALUE_EXTENSION = NOTIFICATION_VALUE_EXTENSION;
	}
}
