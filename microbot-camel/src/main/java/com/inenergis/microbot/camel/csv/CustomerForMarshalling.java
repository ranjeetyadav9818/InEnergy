package com.inenergis.microbot.camel.csv;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.util.Date;

@CsvRecord(separator = "\t")
@Getter
@Setter
public class CustomerForMarshalling {
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
}
