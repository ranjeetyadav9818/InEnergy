package com.inenergis.microbot.camel.csv;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.util.Date;

@CsvRecord(separator = ",")
public class RecordingManifest {

	@DataField(pos = 1)
	String SuperEnterprise;

	@DataField(pos = 2)
	String SuperEnterpriseID;

	@DataField(pos = 3)
	String Enterprise;

	@DataField(pos = 4)
	String EnterpriseID;

	@DataField(pos = 5)
	String Account;

	@DataField(pos = 6)
	String AccountID;

	@DataField(pos = 7)
	String Campaign;

	@DataField(pos = 8)
	String SubCampaign;

	@DataField(pos = 9)
	String PassID;

	@DataField(pos = 10)
	String PassType;

	@DataField(pos = 11)
	String SkillGroup;

	@DataField(pos = 12, pattern = "MM/dd/yyyy HH:mm:ss zzz")
	Date SessionStartTime;

	@DataField(pos = 13)
	String SessionSeconds;

	@DataField(pos = 14, pattern = "MM/dd/yyyy HH:mm:ss zzz")
	Date RecordingStartTime;

	@DataField(pos = 15)
	String RecordingSeconds;

	@DataField(pos = 16)
	String RecordingType;

	@DataField(pos = 17)
	String AgentID;

	@DataField(pos = 18)
	String AgentDevice;

	@DataField(pos = 19)
	String DCAttemptNumber;

	@DataField(pos = 20)
	String ClientID;

	@DataField(pos = 21)
	String FirstName;

	@DataField(pos = 22)
	String LastName;

	@DataField(pos = 23)
	String Device;

	@DataField(pos = 24)
	String CompletionStatus;

	@DataField(pos = 25)
	String DispositionCategory;

	@DataField(pos = 26)
	String DispositionCode;

	@DataField(pos = 27)
	String DispositionValue;

	@DataField(pos = 28)
	String RecordingID;

	@DataField(pos = 29)
	String RecordingState;

	@DataField(pos = 30)
	String MediaURI;

	@DataField(pos = 31)
	String FileName;

	@DataField(pos = 32)
	String uselessAfterComma;

	public String getSuperEnterprise() {
		return SuperEnterprise;
	}

	public void setSuperEnterprise(String superEnterprise) {
		SuperEnterprise = superEnterprise;
	}

	public String getSuperEnterpriseID() {
		return SuperEnterpriseID;
	}

	public void setSuperEnterpriseID(String superEnterpriseID) {
		SuperEnterpriseID = superEnterpriseID;
	}

	public String getEnterprise() {
		return Enterprise;
	}

	public void setEnterprise(String enterprise) {
		Enterprise = enterprise;
	}

	public String getEnterpriseID() {
		return EnterpriseID;
	}

	public void setEnterpriseID(String enterpriseID) {
		EnterpriseID = enterpriseID;
	}

	public String getAccount() {
		return Account;
	}

	public void setAccount(String account) {
		Account = account;
	}

	public String getAccountID() {
		return AccountID;
	}

	public void setAccountID(String accountID) {
		AccountID = accountID;
	}

	public String getCampaign() {
		return Campaign;
	}

	public void setCampaign(String campaign) {
		Campaign = campaign;
	}

	public String getSubCampaign() {
		return SubCampaign;
	}

	public void setSubCampaign(String subCampaign) {
		SubCampaign = subCampaign;
	}

	public String getPassID() {
		return PassID;
	}

	public void setPassID(String passID) {
		PassID = passID;
	}

	public String getPassType() {
		return PassType;
	}

	public void setPassType(String passType) {
		PassType = passType;
	}

	public String getSkillGroup() {
		return SkillGroup;
	}

	public void setSkillGroup(String skillGroup) {
		SkillGroup = skillGroup;
	}

	public Date getSessionStartTime() {
		return SessionStartTime;
	}

	public void setSessionStartTime(Date sessionStartTime) {
		SessionStartTime = sessionStartTime;
	}

	public String getSessionSeconds() {
		return SessionSeconds;
	}

	public void setSessionSeconds(String sessionSeconds) {
		SessionSeconds = sessionSeconds;
	}

	public Date getRecordingStartTime() {
		return RecordingStartTime;
	}

	public void setRecordingStartTime(Date recordingStartTime) {
		RecordingStartTime = recordingStartTime;
	}

	public String getRecordingSeconds() {
		return RecordingSeconds;
	}

	public void setRecordingSeconds(String recordingSeconds) {
		RecordingSeconds = recordingSeconds;
	}

	public String getRecordingType() {
		return RecordingType;
	}

	public void setRecordingType(String recordingType) {
		RecordingType = recordingType;
	}

	public String getAgentID() {
		return AgentID;
	}

	public void setAgentID(String agentID) {
		AgentID = agentID;
	}

	public String getAgentDevice() {
		return AgentDevice;
	}

	public void setAgentDevice(String agentDevice) {
		AgentDevice = agentDevice;
	}

	public String getDCAttemptNumber() {
		return DCAttemptNumber;
	}

	public void setDCAttemptNumber(String DCAttemptNumber) {
		this.DCAttemptNumber = DCAttemptNumber;
	}

	public String getClientID() {
		return ClientID;
	}

	public void setClientID(String clientID) {
		ClientID = clientID;
	}

	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public String getDevice() {
		return Device;
	}

	public void setDevice(String device) {
		Device = device;
	}

	public String getCompletionStatus() {
		return CompletionStatus;
	}

	public void setCompletionStatus(String completionStatus) {
		CompletionStatus = completionStatus;
	}

	public String getDispositionCategory() {
		return DispositionCategory;
	}

	public void setDispositionCategory(String dispositionCategory) {
		DispositionCategory = dispositionCategory;
	}

	public String getDispositionCode() {
		return DispositionCode;
	}

	public void setDispositionCode(String dispositionCode) {
		DispositionCode = dispositionCode;
	}

	public String getDispositionValue() {
		return DispositionValue;
	}

	public void setDispositionValue(String dispositionValue) {
		DispositionValue = dispositionValue;
	}

	public String getRecordingID() {
		return RecordingID;
	}

	public void setRecordingID(String recordingID) {
		RecordingID = recordingID;
	}

	public String getRecordingState() {
		return RecordingState;
	}

	public void setRecordingState(String recordingState) {
		RecordingState = recordingState;
	}

	public String getMediaURI() {
		return MediaURI;
	}

	public void setMediaURI(String mediaURI) {
		MediaURI = mediaURI;
	}

	public String getFileName() {
		return FileName;
	}

	public void setFileName(String fileName) {
		FileName = fileName;
	}

	@Override
	public String toString() {
		return "RecordingManifest{" +
				"SubCampaign='" + SubCampaign + '\'' +
				", ClientID='" + ClientID + '\'' +
				'}';
	}
}
