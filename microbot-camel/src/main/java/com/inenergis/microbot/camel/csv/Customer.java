package com.inenergis.microbot.camel.csv;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.util.Date;

@CsvRecord(separator = "¶")
public class Customer implements CustomerInterface {

	/**
	 * SA_STATUS
	 */
	@DataField(pos = 1)
	private String SA_STATUS;
	/**
	 * SA_ID
	 */
	@DataField(pos = 2)
	private String SA_ID;
	/**
	 * ACCOUNT_ID
	 */
	@DataField(pos = 3)
	private String ACCOUNT_ID;
	/**
	 * PREMISE_ID
	 */
	@DataField(pos = 4)
	private String PREMISE_ID;
	/**
	 * METER_ID
	 */
	@DataField(pos = 5)
	private String METER_ID;
	/**
	 * PERSON_ID
	 */
	@DataField(pos = 6)
	private String PERSON_ID;
	/**
	 * LAST_NAME
	 */
	@DataField(pos = 7)
	private String LAST_NAME;
	/**
	 * SERVICE_ADDRESS1
	 */
	@DataField(pos = 8)
	private String SERVICE_ADDRESS1;
	/**
	 * SERVICE_ADDRESS2
	 */
	@DataField(pos = 9)
	private String SERVICE_ADDRESS2;

	/**
	 * SERVICE_CITY_UPR
	 */
	@DataField(pos = 10)
	private String SERVICE_CITY_UPR;

	/**
	 * SERVICE_STATE
	 */
	@DataField(pos = 11)
	private String SERVICE_STATE;
	/**
	 * SERVICE_POSTAL
	 */
	@DataField(pos = 12)
	private String SERVICE_POSTAL;
	/**
	 * PHONE
	 */
	@DataField(pos = 13)
	private String PHONE;
	/**
	 * MAILING_ADDRESS1
	 */
	@DataField(pos = 14)
	private String MAILING_ADDRESS1;
	/**
	 * MAILING_ADDRESS2
	 */
	@DataField(pos = 15)
	private String MAILING_ADDRESS2;
	/**
	 * MAILING_CITY_UPR
	 */
	@DataField(pos = 16)
	private String MAILING_CITY_UPR;
	/**
	 * MAILING_STATE
	 */
	@DataField(pos = 17)
	private String MAILING_STATE;
	/**
	 * MAILING_ZIP
	 */
	@DataField(pos = 18)
	private String MAILING_ZIP;
	/**
	 * RS_CD
	 */
	@DataField(pos = 19)
	private String RS_CD;
	/**
	 * RES_YN_IND
	 */
	@DataField(pos = 20)
	private String RES_YN_IND;
	/**
	 * SUBSTATION
	 */
	@DataField(pos = 21)
	private String SUBSTATION;
	/**
	 * FEEDER
	 */
	@DataField(pos = 22)
	private String FEEDER;
	/**
	 * SP_ID
	 */
	@DataField(pos = 23)//Service Point ID
	private String SP_ID;
	/**
	 * MEDICAL_BASELINE_IND
	 */
	@DataField(pos = 24)
	private String MEDICAL_BASELINE_IND;
	
	private boolean medicalBaseline = false;
	
	/**
	 * LIFE_SUPPORT_IND
	 */
	@DataField(pos = 25)
	private String LIFE_SUPPORT_IND;
	/**
	 * SMART_RATE_IND
	 */
	@DataField(pos = 26)
	private String SMART_RATE_IND;
	/**
	 * SA_SP_START_DTTM
	 */
	@DataField(pos=27,pattern="MM/dd/yyyy HH:mm:ss" /*, pattern="yyyy-MM-dd HH:mm:ss"*/)
	private Date SA_SP_START_DTTM; //Agreement Map Table
	/**
	 * SA_SP_STOP_DTTM
	 */
	@DataField(pos=28,pattern="MM/dd/yyyy HH:mm:ss" /*, pattern="yyyy-MM-dd HH:mm:ss"*/)
	private Date SA_SP_STOP_DTTM; //Agreement Map Table
	/**
	 * PREMISE_TYPE
	 */
	@DataField(pos = 29)
	private String PREMISE_TYPE;
	/**
	 * SR_START_DATE
	 */
	@DataField(pos=30,pattern="MM/dd/yyyy" /*, pattern="yyyy-MM-dd HH:mm:ss"*/)
	private Date SR_START_DATE; //
	/**
	 * SR_END_DATE
	 */
	@DataField(pos=31,pattern="MM/dd/yyyy" /*, pattern="yyyy-MM-dd HH:mm:ss"*/)
	private Date SR_END_DATE;
	/**
	 * PDP_ENROLLED
	 */
	@DataField(pos = 32)
	private String PDP_ENROLLED;
	/**
	 * PDP_STATUS
	 */
	@DataField(pos = 33)
	private String PDP_STATUS;
	/**
	 * PDP_START_DATE
	 */
	@DataField(pos=34,pattern="MM/dd/yyyy" /*, pattern="yyyy-MM-dd HH:mm:ss"*/)
	private Date PDP_START_DATE;
	/**
	 * PDP_STOP_DATE
	 */
	@DataField(pos=35,pattern="MM/dd/yyyy" /*, pattern="yyyy-MM-dd HH:mm:ss"*/)
	private Date PDP_STOP_DATE;
	/**
	 * PDP_PLAN_OPTIONS
	 */
	@DataField(pos = 36)
	private String PDP_PLAN_OPTIONS;
	/**
	 * ELEC_USAGE_NONRES
	 */
	@DataField(pos = 37)
	private String ELEC_USAGE_NONRES;
	/**
	 * PDP_RESV_CAP_VAL
	 */
	@DataField(pos = 38)
	private String PDP_RESV_CAP_VAL;

	/**
	 * HAS_3RD_PARTY_DRP
	 */
	@DataField(pos = 39)
	private String HAS_3RD_PARTY_DRP;

	@DataField(pos = 40)
	private String PHONE_EXTENSION;

	@DataField(pos = 41)
	private String SUPPLIER_IS_DRP;

	@DataField(pos = 42)
	private String UNIQ_SA_ID;

	@DataField(pos = 43,pattern="MM/dd/yyyy HH:mm:ss")
	private Date UNIQ_SA_ID_CREATE_DATE;

	@DataField(pos = 44)
	private String UNIQ_SA_ID_WARN_FLAG;

	@DataField(pos = 45)
	private String SA_UUID;

	@DataField(pos = 46)
	private String DO_BUS_AS_NM;

	@DataField(pos = 47,pattern="MM/dd/yyyy")
	private Date SA_START_DATE;

	@DataField(pos = 48,pattern="MM/dd/yyyy")
	private Date SA_END_DATE;

	@DataField(pos = 49)
	private String SA_NAICS;

	@DataField(pos = 50)
	private String BILL_CYCLE_CD;

	@DataField(pos = 51)
	private String CUST_CLASS_CD;

	@DataField(pos = 52)
	private String REVENUE_CLASS_DESC;

	@DataField(pos = 53)
	private String FERA_FLAG;

	@DataField(pos = 54)
	private String BILL_SYSTEM;

	@DataField(pos = 55)
	private String CUST_SIZE;

	@DataField(pos = 56)
	private String MARKET_SEGMENT;

	@DataField(pos = 57)
	private String CARE_FLAG;

	@DataField(pos = 58)
	private String OPERATION_AREA;

	@DataField(pos = 59)
	private String PREM_BASELINE_CHAR;

	@DataField(pos = 60)
	private String MTR_BADGENBR;

	@DataField(pos = 61)
	private String SM_STATUS;

	@DataField(pos = 62, pattern="MM/dd/yyyy")
	private Date MTR_INSTALL_DT;

	@DataField(pos = 63, pattern="MM/dd/yyyy HH:mm:ss")
	private Date MTR_UNINSTALL_DT;

	@DataField(pos = 64)
	private String SM_MODULE_MFR;

	@DataField(pos = 65)
	private String MTR_CONFIG_TYPE;

	@DataField(pos = 66)
	private String MTR_READ_FREQ;

	@DataField(pos = 67)
	private String MTR_MFG;

	@DataField(pos = 68)
	private String PDP_BILL_PROTECTION;

	@DataField(pos = 69)
	private String SR_BILL_PROTECTION;

	@DataField(pos = 70)
	private String SA_TYPE_CD;

	@DataField(pos = 71)
	private String CUSTOMER_MDMA_COMPANY_NAME;

	@DataField(pos = 72)
	private String CUSTOMER_MSP_COMPANY_NAME;

	@DataField(pos = 73)
	private String CUSTOMER_LSE_COMPANY_NAME;

	@DataField(pos = 74)
	private String CUST_METER_READ_CYCLE12;

	@DataField(pos = 75)
	private String CUST_SERVICE_VOLTAGE_CLASS;

	@DataField(pos = 76)
	private String SA_SP_ID;

	@DataField(pos = 77,pattern="MM/dd/yyyy")
	private Date RATE_CODE_EFFECTIVE_DATE;

	@DataField(pos = 78)
	private String SERVICE_TYPE;

	@DataField(pos = 79)
	private String BUSINESS_ACTIVITY_DESC;

	@DataField(pos = 80)
	private String BUS_OWNER;

	@DataField(pos = 81)
	private String DIVISION_CODE_19;

	@DataField(pos = 82)
	private String CIRCUIT_NUMBER;

	@DataField(pos = 83)
	private String SUB_STATION_NUMBER;

	@DataField(pos = 84)
	private String COUNTY;

	@DataField(pos = 85)
	private String CLIMATE_ZONE;

	@DataField(pos = 86)
	private String ESS_DIVISION_CODE;

	@DataField(pos = 87)
	private String SOURCE_SIDE_DEVICE_NUMBER;

	@DataField(pos = 88)
	private String LATITUDE;

	@DataField(pos = 89)
	private String LONGITUDE;

	@DataField(pos = 90)
	private String TRFMR_NUMBER;

	@DataField(pos = 91)
	private String TRFMR_BDG_NUMBER;

	@DataField(pos = 92)
	private String ROB_CODE;

	@DataField(pos = 93)
	private String CUSTOMER_MDMA_CODE;
	@DataField(pos = 94)
	private String CUSTOMER_MSP_CODE;
	@DataField(pos = 95)
	private String CUSTOMER_LSE_CODE;
	@DataField(pos = 96)
	private String ESSENTIAL_CUSTOMER_FLAG;

	private boolean pdpEnrolled;
	private boolean smartRate;
	private boolean lifeSupport;
	private boolean has3rdParty;
	private boolean supplierIsDRP;
	private boolean feraFlag;
	private boolean careFlag;
	private boolean pdpBillProtection;
	private boolean srBillProtection;
	private boolean resInd;

	public String getSA_STATUS() {
		return SA_STATUS;
	}

	public void setSA_STATUS(String sA_STATUS) {
		SA_STATUS = sA_STATUS;
	}

	public String getSA_ID() {
		return SA_ID;
	}

	public void setSA_ID(String sA_ID) {
		SA_ID = sA_ID;
	}

	public String getACCOUNT_ID() {
		return ACCOUNT_ID;
	}

	public void setACCOUNT_ID(String aCCOUNT_ID) {
		ACCOUNT_ID = aCCOUNT_ID;
	}

	public String getPREMISE_ID() {
		return PREMISE_ID;
	}

	public void setPREMISE_ID(String pREMISE_ID) {
		PREMISE_ID = pREMISE_ID;
	}

	public String getMETER_ID() {
		return METER_ID;
	}

	public void setMETER_ID(String mETER_ID) {
		METER_ID = mETER_ID;
	}

	public String getPERSON_ID() {
		return PERSON_ID;
	}

	public void setPERSON_ID(String pERSON_ID) {
		PERSON_ID = pERSON_ID;
	}

	public String getLAST_NAME() {
		return LAST_NAME;
	}

	public void setLAST_NAME(String lAST_NAME) {
		LAST_NAME = lAST_NAME;
	}

	public String getSERVICE_ADDRESS1() {
		return SERVICE_ADDRESS1;
	}

	public void setSERVICE_ADDRESS1(String sERVICE_ADDRESS1) {
		SERVICE_ADDRESS1 = sERVICE_ADDRESS1;
	}

	public String getSERVICE_ADDRESS2() {
		return SERVICE_ADDRESS2;
	}

	public void setSERVICE_ADDRESS2(String sERVICE_ADDRESS2) {
		SERVICE_ADDRESS2 = sERVICE_ADDRESS2;
	}



	public String getSERVICE_STATE() {
		return SERVICE_STATE;
	}

	public void setSERVICE_STATE(String sERVICE_STATE) {
		SERVICE_STATE = sERVICE_STATE;
	}

	public String getSERVICE_POSTAL() {
		return SERVICE_POSTAL;
	}

	public void setSERVICE_POSTAL(String sERVICE_POSTAL) {
		SERVICE_POSTAL = sERVICE_POSTAL;
	}

	public String getPHONE() {
		return PHONE;
	}

	public void setPHONE(String pHONE) {
		PHONE = pHONE;
	}

	public String getMAILING_ADDRESS1() {
		return MAILING_ADDRESS1;
	}

	public void setMAILING_ADDRESS1(String mAILING_ADDRESS1) {
		MAILING_ADDRESS1 = mAILING_ADDRESS1;
	}

	public String getMAILING_ADDRESS2() {
		return MAILING_ADDRESS2;
	}

	public void setMAILING_ADDRESS2(String mAILING_ADDRESS2) {
		MAILING_ADDRESS2 = mAILING_ADDRESS2;
	}

	public String getMAILING_CITY_UPR() {
		return MAILING_CITY_UPR;
	}

	public void setMAILING_CITY_UPR(String mAILING_CITY_UPR) {
		MAILING_CITY_UPR = mAILING_CITY_UPR;
	}

	public String getMAILING_STATE() {
		return MAILING_STATE;
	}

	public void setMAILING_STATE(String mAILING_STATE) {
		MAILING_STATE = mAILING_STATE;
	}

	public String getMAILING_ZIP() {
		return MAILING_ZIP;
	}

	public void setMAILING_ZIP(String mAILING_ZIP) {
		MAILING_ZIP = mAILING_ZIP;
	}

	public String getRS_CD() {
		return RS_CD;
	}

	public void setRS_CD(String rS_CD) {
		RS_CD = rS_CD;
	}

	public String getRES_YN_IND() {
		return RES_YN_IND;
	}

	public void setRES_YN_IND(String rES_YN_IND) {
		RES_YN_IND = rES_YN_IND;
	}

	public String getSUBSTATION() {
		return SUBSTATION;
	}

	public void setSUBSTATION(String sUBSTATION) {
		SUBSTATION = sUBSTATION;
	}

	public String getFEEDER() {
		return FEEDER;
	}

	public void setFEEDER(String fEEDER) {
		FEEDER = fEEDER;
	}

	public String getSP_ID() {
		return SP_ID;
	}

	public void setSP_ID(String sP_ID) {
		SP_ID = sP_ID;
	}

	public String getMEDICAL_BASELINE_IND() {
		return MEDICAL_BASELINE_IND;
	}

	public void setMEDICAL_BASELINE_IND(String mEDICAL_BASELINE_IND) {
		MEDICAL_BASELINE_IND = mEDICAL_BASELINE_IND;
	}

	public String getLIFE_SUPPORT_IND() {
		return LIFE_SUPPORT_IND;
	}

	public void setLIFE_SUPPORT_IND(String lIFE_SUPPORT_IND) {
		LIFE_SUPPORT_IND = lIFE_SUPPORT_IND;
	}

	public boolean isPdpEnrolled() {
		return pdpEnrolled;
	}

	public void setPdpEnrolled(boolean pdpEnrolled) {
		this.pdpEnrolled = pdpEnrolled;
	}

	public boolean isSmartRate() {
		return smartRate;
	}

	public void setSmartRate(boolean smartRate) {
		this.smartRate = smartRate;
	}

	public boolean isLifeSupport() {
		return lifeSupport;
	}

	public void setLifeSupport(boolean lifeSupport) {
		this.lifeSupport = lifeSupport;
	}

	public String getSMART_RATE_IND() {
		return SMART_RATE_IND;
	}

	public void setSMART_RATE_IND(String sMART_RATE_IND) {
		SMART_RATE_IND = sMART_RATE_IND;
	}

	public Date getSA_SP_START_DTTM() {
		return SA_SP_START_DTTM;
	}

	public void setSA_SP_START_DTTM(Date sA_SP_START_DTTM) {
		SA_SP_START_DTTM = sA_SP_START_DTTM;
	}

	public Date getSA_SP_STOP_DTTM() {
		return SA_SP_STOP_DTTM;
	}

	public void setSA_SP_STOP_DTTM(Date sA_SP_STOP_DTTM) {
		SA_SP_STOP_DTTM = sA_SP_STOP_DTTM;
	}

	public String getPREMISE_TYPE() {
		return PREMISE_TYPE;
	}

	public void setPREMISE_TYPE(String pREMISE_TYPE) {
		PREMISE_TYPE = pREMISE_TYPE;
	}

	public Date getSR_START_DATE() {
		return SR_START_DATE;
	}

	public void setSR_START_DATE(Date sR_START_DATE) {
		SR_START_DATE = sR_START_DATE;
	}

	public Date getSR_END_DATE() {
		return SR_END_DATE;
	}

	public void setSR_END_DATE(Date sR_END_DATE) {
		SR_END_DATE = sR_END_DATE;
	}

	public String getPDP_ENROLLED() {
		return PDP_ENROLLED;
	}

	public void setPDP_ENROLLED(String pDP_ENROLLED) {
		PDP_ENROLLED = pDP_ENROLLED;
	}

	public String getPDP_STATUS() {
		return PDP_STATUS;
	}

	public void setPDP_STATUS(String pDP_STATUS) {
		PDP_STATUS = pDP_STATUS;
	}

	public Date getPDP_START_DATE() {
		return PDP_START_DATE;
	}

	public void setPDP_START_DATE(Date pDP_START_DATE) {
		PDP_START_DATE = pDP_START_DATE;
	}

	public Date getPDP_STOP_DATE() {
		return PDP_STOP_DATE;
	}

	public void setPDP_STOP_DATE(Date pDP_STOP_DATE) {
		PDP_STOP_DATE = pDP_STOP_DATE;
	}

	public String getPDP_PLAN_OPTIONS() {
		return PDP_PLAN_OPTIONS;
	}

	public void setPDP_PLAN_OPTIONS(String pDP_PLAN_OPTIONS) {
		PDP_PLAN_OPTIONS = pDP_PLAN_OPTIONS;
	}

	public String getELEC_USAGE_NONRES() {
		return ELEC_USAGE_NONRES;
	}

	public void setELEC_USAGE_NONRES(String eLEC_USAGE_NONRES) {
		ELEC_USAGE_NONRES = eLEC_USAGE_NONRES;
	}

	public String getPDP_RESV_CAP_VAL() {
		return PDP_RESV_CAP_VAL;
	}

	public void setPDP_RESV_CAP_VAL(String pDP_RESV_CAP_VAL) {
		PDP_RESV_CAP_VAL = pDP_RESV_CAP_VAL;
	}

	public String getHAS_3RD_PARTY_DRP() {
		return HAS_3RD_PARTY_DRP;
	}

	public void setHAS_3RD_PARTY_DRP(String hAS_3RD_PARTY_DRP) {
		HAS_3RD_PARTY_DRP = hAS_3RD_PARTY_DRP;
	}

	public String getSERVICE_CITY_UPR() {
		return SERVICE_CITY_UPR;
	}

	public void setSERVICE_CITY_UPR(String sERVICE_CITY_UPR) {
		SERVICE_CITY_UPR = sERVICE_CITY_UPR;
	}

	public boolean isMedicalBaseline() {
		return medicalBaseline;
	}

	public void setMedicalBaseline(boolean medicalBaseline) {
		this.medicalBaseline = medicalBaseline;
	}

	public boolean isHas3rdParty() {
		return has3rdParty;
	}

	public void setHas3rdParty(boolean has3rdParty) {
		this.has3rdParty = has3rdParty;
	}

	public boolean isResInd() {
		return resInd;
	}

	public void setResInd(boolean resInd) {
		this.resInd = resInd;
	}

	public String getPHONE_EXTENSION() {
		return PHONE_EXTENSION;
	}

	public void setPHONE_EXTENSION(String PHONE_EXTENSION) {
		this.PHONE_EXTENSION = PHONE_EXTENSION;
	}

	public String getSUPPLIER_IS_DRP() {
		return SUPPLIER_IS_DRP;
	}

	public void setSUPPLIER_IS_DRP(String SUPPLIER_IS_DRP) {
		this.SUPPLIER_IS_DRP = SUPPLIER_IS_DRP;
	}

	public boolean isSupplierIsDRP() {
		return supplierIsDRP;
	}

	public void setSupplierIsDRP(boolean supplierIsDRP) {
		this.supplierIsDRP = supplierIsDRP;
	}

	public String getUNIQ_SA_ID() {
		return UNIQ_SA_ID;
	}

	public void setUNIQ_SA_ID(String UNIQ_SA_ID) {
		this.UNIQ_SA_ID = UNIQ_SA_ID;
	}

	public Date getUNIQ_SA_ID_CREATE_DATE() {
		return UNIQ_SA_ID_CREATE_DATE;
	}

	public void setUNIQ_SA_ID_CREATE_DATE(Date UNIQ_SA_ID_CREATE_DATE) {
		this.UNIQ_SA_ID_CREATE_DATE = UNIQ_SA_ID_CREATE_DATE;
	}

	public String getUNIQ_SA_ID_WARN_FLAG() {
		return UNIQ_SA_ID_WARN_FLAG;
	}

	public void setUNIQ_SA_ID_WARN_FLAG(String UNIQ_SA_ID_WARN_FLAG) {
		this.UNIQ_SA_ID_WARN_FLAG = UNIQ_SA_ID_WARN_FLAG;
	}

	public String getSA_UUID() {
		return SA_UUID;
	}

	public void setSA_UUID(String SA_UUID) {
		this.SA_UUID = SA_UUID;
	}

	public String getDO_BUS_AS_NM() {
		return DO_BUS_AS_NM;
	}

	public void setDO_BUS_AS_NM(String DO_BUS_AS_NM) {
		this.DO_BUS_AS_NM = DO_BUS_AS_NM;
	}

	public Date getSA_START_DATE() {
		return SA_START_DATE;
	}

	public void setSA_START_DATE(Date SA_START_DATE) {
		this.SA_START_DATE = SA_START_DATE;
	}

	public Date getSA_END_DATE() {
		return SA_END_DATE;
	}

	public void setSA_END_DATE(Date SA_END_DATE) {
		this.SA_END_DATE = SA_END_DATE;
	}

	public String getSA_NAICS() {
		return SA_NAICS;
	}

	public void setSA_NAICS(String SA_NAICS) {
		this.SA_NAICS = SA_NAICS;
	}

	public String getBILL_CYCLE_CD() {
		return BILL_CYCLE_CD;
	}

	public void setBILL_CYCLE_CD(String BILL_CYCLE_CD) {
		this.BILL_CYCLE_CD = BILL_CYCLE_CD;
	}

	public String getCUST_CLASS_CD() {
		return CUST_CLASS_CD;
	}

	public void setCUST_CLASS_CD(String CUST_CLASS_CD) {
		this.CUST_CLASS_CD = CUST_CLASS_CD;
	}

	public String getREVENUE_CLASS_DESC() {
		return REVENUE_CLASS_DESC;
	}

	public void setREVENUE_CLASS_DESC(String REVENUE_CLASS_DESC) {
		this.REVENUE_CLASS_DESC = REVENUE_CLASS_DESC;
	}

	public String getFERA_FLAG() {
		return FERA_FLAG;
	}

	public void setFERA_FLAG(String FERA_FLAG) {
		this.FERA_FLAG = FERA_FLAG;
	}

	public String getBILL_SYSTEM() {
		return BILL_SYSTEM;
	}

	public void setBILL_SYSTEM(String BILL_SYSTEM) {
		this.BILL_SYSTEM = BILL_SYSTEM;
	}

	public String getCUST_SIZE() {
		return CUST_SIZE;
	}

	public void setCUST_SIZE(String CUST_SIZE) {
		this.CUST_SIZE = CUST_SIZE;
	}

	public String getMARKET_SEGMENT() {
		return MARKET_SEGMENT;
	}

	public void setMARKET_SEGMENT(String MARKET_SEGMENT) {
		this.MARKET_SEGMENT = MARKET_SEGMENT;
	}

	public String getCARE_FLAG() {
		return CARE_FLAG;
	}

	public void setCARE_FLAG(String CARE_FLAG) {
		this.CARE_FLAG = CARE_FLAG;
	}

	public String getOPERATION_AREA() {
		return OPERATION_AREA;
	}

	public void setOPERATION_AREA(String OPERATION_AREA) {
		this.OPERATION_AREA = OPERATION_AREA;
	}

	public String getPREM_BASELINE_CHAR() {
		return PREM_BASELINE_CHAR;
	}

	public void setPREM_BASELINE_CHAR(String PREM_BASELINE_CHAR) {
		this.PREM_BASELINE_CHAR = PREM_BASELINE_CHAR;
	}

	public String getMTR_BADGENBR() {
		return MTR_BADGENBR;
	}

	public void setMTR_BADGENBR(String MTR_BADGENBR) {
		this.MTR_BADGENBR = MTR_BADGENBR;
	}

	public String getSM_STATUS() {
		return SM_STATUS;
	}

	public void setSM_STATUS(String SM_STATUS) {
		this.SM_STATUS = SM_STATUS;
	}

	public Date getMTR_INSTALL_DT() {
		return MTR_INSTALL_DT;
	}

	public void setMTR_INSTALL_DT(Date MTR_INSTALL_DT) {
		this.MTR_INSTALL_DT = MTR_INSTALL_DT;
	}

	public boolean isFeraFlag() {
		return feraFlag;
	}

	public void setFeraFlag(boolean feraFlag) {
		this.feraFlag = feraFlag;
	}

	public boolean isCareFlag() {
		return careFlag;
	}

	public void setCareFlag(boolean careFlag) {
		this.careFlag = careFlag;
	}

	public Date getMTR_UNINSTALL_DT() {
		return MTR_UNINSTALL_DT;
	}

	public void setMTR_UNINSTALL_DT(Date MTR_UNINSTALL_DT) {
		this.MTR_UNINSTALL_DT = MTR_UNINSTALL_DT;
	}

	public String getSM_MODULE_MFR() {
		return SM_MODULE_MFR;
	}

	public void setSM_MODULE_MFR(String SM_MODULE_MFR) {
		this.SM_MODULE_MFR = SM_MODULE_MFR;
	}

	public String getMTR_CONFIG_TYPE() {
		return MTR_CONFIG_TYPE;
	}

	public void setMTR_CONFIG_TYPE(String MTR_CONFIG_TYPE) {
		this.MTR_CONFIG_TYPE = MTR_CONFIG_TYPE;
	}

	public String getMTR_READ_FREQ() {
		return MTR_READ_FREQ;
	}

	public void setMTR_READ_FREQ(String MTR_READ_FREQ) {
		this.MTR_READ_FREQ = MTR_READ_FREQ;
	}

	public String getMTR_MFG() {
		return MTR_MFG;
	}

	public void setMTR_MFG(String MTR_MFG) {
		this.MTR_MFG = MTR_MFG;
	}

	public String getPDP_BILL_PROTECTION() {
		return PDP_BILL_PROTECTION;
	}

	public void setPDP_BILL_PROTECTION(String PDP_BILL_PROTECTION) {
		this.PDP_BILL_PROTECTION = PDP_BILL_PROTECTION;
	}

	public String getSR_BILL_PROTECTION() {
		return SR_BILL_PROTECTION;
	}

	public void setSR_BILL_PROTECTION(String SR_BILL_PROTECTION) {
		this.SR_BILL_PROTECTION = SR_BILL_PROTECTION;
	}

	public String getSA_TYPE_CD() {
		return SA_TYPE_CD;
	}

	public void setSA_TYPE_CD(String SA_TYPE_CD) {
		this.SA_TYPE_CD = SA_TYPE_CD;
	}

	public String getCUSTOMER_MDMA_COMPANY_NAME() {
		return CUSTOMER_MDMA_COMPANY_NAME;
	}

	public void setCUSTOMER_MDMA_COMPANY_NAME(String CUSTOMER_MDMA_COMPANY_NAME) {
		this.CUSTOMER_MDMA_COMPANY_NAME = CUSTOMER_MDMA_COMPANY_NAME;
	}

	public String getCUSTOMER_MSP_COMPANY_NAME() {
		return CUSTOMER_MSP_COMPANY_NAME;
	}

	public void setCUSTOMER_MSP_COMPANY_NAME(String CUSTOMER_MSP_COMPANY_NAME) {
		this.CUSTOMER_MSP_COMPANY_NAME = CUSTOMER_MSP_COMPANY_NAME;
	}

	public String getCUSTOMER_LSE_COMPANY_NAME() {
		return CUSTOMER_LSE_COMPANY_NAME;
	}

	public void setCUSTOMER_LSE_COMPANY_NAME(String CUSTOMER_LSE_COMPANY_NAME) {
		this.CUSTOMER_LSE_COMPANY_NAME = CUSTOMER_LSE_COMPANY_NAME;
	}

	public String getCUST_METER_READ_CYCLE12() {
		return CUST_METER_READ_CYCLE12;
	}

	public void setCUST_METER_READ_CYCLE12(String CUST_METER_READ_CYCLE12) {
		this.CUST_METER_READ_CYCLE12 = CUST_METER_READ_CYCLE12;
	}

	public String getCUST_SERVICE_VOLTAGE_CLASS() {
		return CUST_SERVICE_VOLTAGE_CLASS;
	}

	public void setCUST_SERVICE_VOLTAGE_CLASS(String CUST_SERVICE_VOLTAGE_CLASS) {
		this.CUST_SERVICE_VOLTAGE_CLASS = CUST_SERVICE_VOLTAGE_CLASS;
	}

	public String getSA_SP_ID() {
		return SA_SP_ID;
	}

	public void setSA_SP_ID(String SA_SP_ID) {
		this.SA_SP_ID = SA_SP_ID;
	}

	public Date getRATE_CODE_EFFECTIVE_DATE() {
		return RATE_CODE_EFFECTIVE_DATE;
	}

	public void setRATE_CODE_EFFECTIVE_DATE(Date RATE_CODE_EFFECTIVE_DATE) {
		this.RATE_CODE_EFFECTIVE_DATE = RATE_CODE_EFFECTIVE_DATE;
	}

	public String getSERVICE_TYPE() {
		return SERVICE_TYPE;
	}

	public void setSERVICE_TYPE(String SERVICE_TYPE) {
		this.SERVICE_TYPE = SERVICE_TYPE;
	}

	public String getBUSINESS_ACTIVITY_DESC() {
		return BUSINESS_ACTIVITY_DESC;
	}

	public void setBUSINESS_ACTIVITY_DESC(String BUSINESS_ACTIVITY_DESC) {
		this.BUSINESS_ACTIVITY_DESC = BUSINESS_ACTIVITY_DESC;
	}

	public String getBUS_OWNER() {
		return BUS_OWNER;
	}

	public void setBUS_OWNER(String BUS_OWNER) {
		this.BUS_OWNER = BUS_OWNER;
	}

	public String getDIVISION_CODE_19() {
		return DIVISION_CODE_19;
	}

	public void setDIVISION_CODE_19(String DIVISION_CODE_19) {
		this.DIVISION_CODE_19 = DIVISION_CODE_19;
	}

	public String getCIRCUIT_NUMBER() {
		return CIRCUIT_NUMBER;
	}

	public void setCIRCUIT_NUMBER(String CIRCUIT_NUMBER) {
		this.CIRCUIT_NUMBER = CIRCUIT_NUMBER;
	}

	public String getSUB_STATION_NUMBER() {
		return SUB_STATION_NUMBER;
	}

	public void setSUB_STATION_NUMBER(String SUB_STATION_NUMBER) {
		this.SUB_STATION_NUMBER = SUB_STATION_NUMBER;
	}

	public boolean isPdpBillProtection() {
		return pdpBillProtection;
	}

	public void setPdpBillProtection(boolean pdpBillProtection) {
		this.pdpBillProtection = pdpBillProtection;
	}

	public boolean isSrBillProtection() {
		return srBillProtection;
	}

	public void setSrBillProtection(boolean srBillProtection) {
		this.srBillProtection = srBillProtection;
	}

	public String getCOUNTY() {
		return COUNTY;
	}

	public void setCOUNTY(String COUNTY) {
		this.COUNTY = COUNTY;
	}

	public String getCLIMATE_ZONE() {
		return CLIMATE_ZONE;
	}

	public void setCLIMATE_ZONE(String CLIMATE_ZONE) {
		this.CLIMATE_ZONE = CLIMATE_ZONE;
	}

	public String getESS_DIVISION_CODE() {
		return ESS_DIVISION_CODE;
	}

	public void setESS_DIVISION_CODE(String ESS_DIVISION_CODE) {
		this.ESS_DIVISION_CODE = ESS_DIVISION_CODE;
	}

	public String getSOURCE_SIDE_DEVICE_NUMBER() {
		return SOURCE_SIDE_DEVICE_NUMBER;
	}

	public void setSOURCE_SIDE_DEVICE_NUMBER(String SOURCE_SIDE_DEVICE_NUMBER) {
		this.SOURCE_SIDE_DEVICE_NUMBER = SOURCE_SIDE_DEVICE_NUMBER;
	}

	public String getLATITUDE() {
		return LATITUDE;
	}

	public void setLATITUDE(String LATITUDE) {
		this.LATITUDE = LATITUDE;
	}

	public String getLONGITUDE() {
		return LONGITUDE;
	}

	public void setLONGITUDE(String LONGITUDE) {
		this.LONGITUDE = LONGITUDE;
	}

	public String getTRFMR_NUMBER() {
		return TRFMR_NUMBER;
	}

	public void setTRFMR_NUMBER(String TRFMR_NUMBER) {
		this.TRFMR_NUMBER = TRFMR_NUMBER;
	}

	public String getTRFMR_BDG_NUMBER() {
		return TRFMR_BDG_NUMBER;
	}

	public void setTRFMR_BDG_NUMBER(String TRFMR_BDG_NUMBER) {
		this.TRFMR_BDG_NUMBER = TRFMR_BDG_NUMBER;
	}

	public String getROB_CODE() {
		return ROB_CODE;
	}

	public void setROB_CODE(String ROB_CODE) {
		this.ROB_CODE = ROB_CODE;
	}


	public String getCUSTOMER_MDMA_CODE() {
		return CUSTOMER_MDMA_CODE;
	}

	public void setCUSTOMER_MDMA_CODE(String CUSTOMER_MDMA_CODE) {
		this.CUSTOMER_MDMA_CODE = CUSTOMER_MDMA_CODE;
	}

	public String getCUSTOMER_MSP_CODE() {
		return CUSTOMER_MSP_CODE;
	}

	public void setCUSTOMER_MSP_CODE(String CUSTOMER_MSP_CODE) {
		this.CUSTOMER_MSP_CODE = CUSTOMER_MSP_CODE;
	}

	public String getCUSTOMER_LSE_CODE() {
		return CUSTOMER_LSE_CODE;
	}

	public void setCUSTOMER_LSE_CODE(String CUSTOMER_LSE_CODE) {
		this.CUSTOMER_LSE_CODE = CUSTOMER_LSE_CODE;
	}

    public String getESSENTIAL_CUSTOMER_FLAG() {
        return ESSENTIAL_CUSTOMER_FLAG;
    }

    public void setESSENTIAL_CUSTOMER_FLAG(String ESSENTIAL_CUSTOMER_FLAG) {
        this.ESSENTIAL_CUSTOMER_FLAG = ESSENTIAL_CUSTOMER_FLAG;
    }

	@Override
	public String toString() {
		return "Customer{" +
				"SA_ID='" + SA_ID + '\'' +
				", ACCOUNT_ID='" + ACCOUNT_ID + '\'' +
				", PREMISE_ID='" + PREMISE_ID + '\'' +
				", METER_ID='" + METER_ID + '\'' +
				", PERSON_ID='" + PERSON_ID + '\'' +
				", LAST_NAME='" + LAST_NAME + '\'' +
				", SERVICE_ADDRESS1='" + SERVICE_ADDRESS1 + '\'' +
				", MAILING_ADDRESS1='" + MAILING_ADDRESS1 + '\'' +
				", BUS_OWNER='" + BUS_OWNER + '\'' +
				", businessName='" + DO_BUS_AS_NM + '\'' +
				'}';
	}

	public void solveBooleanValues() {
        if("Y".equalsIgnoreCase(CARE_FLAG)){
            this.setCareFlag(true);
        }
        if("Y".equalsIgnoreCase(FERA_FLAG)){
            this.setFeraFlag(true);
        }
        if("Y".equalsIgnoreCase(SUPPLIER_IS_DRP)){
            this.setSupplierIsDRP(true);
        }
        if("Y".equalsIgnoreCase(HAS_3RD_PARTY_DRP)){
            this.setHas3rdParty(true);
        }
        if("Y".equalsIgnoreCase(PDP_ENROLLED)){
            this.pdpEnrolled = true;
        }
        if("Y".equalsIgnoreCase(RES_YN_IND)){
            this.resInd = true;
        }
        if("Y".equalsIgnoreCase(MEDICAL_BASELINE_IND)){
            this.medicalBaseline = true;
        }
        if("Y".equalsIgnoreCase(LIFE_SUPPORT_IND)){
            this.lifeSupport = true;
        }
        if("Y".equalsIgnoreCase(SMART_RATE_IND)){
            this.smartRate = true;
        }
        if("Y".equalsIgnoreCase(PDP_BILL_PROTECTION)){
            this.setPdpBillProtection(true);
        }
        if("Y".equalsIgnoreCase(SR_BILL_PROTECTION)){
            this.setSrBillProtection(true);
        }
	}
}
