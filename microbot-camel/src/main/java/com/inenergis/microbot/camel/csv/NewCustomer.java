package com.inenergis.microbot.camel.csv;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.util.Date;

@CsvRecord(separator = "¶")
@ToString
@Getter
@Setter
public class NewCustomer implements CustomerInterface {

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

//    @DataField(pos = 26 )
//    private String ignoreMe1;


    /**
     * SA_SP_START_DTTM
     */
    @DataField(pos=26,pattern="MM/dd/yyyy HH:mm:ss" /*, pattern="yyyy-MM-dd HH:mm:ss"*/)
    private Date SA_SP_START_DTTM; //Agreement Map Table
    /**
     * SA_SP_STOP_DTTM
     */
    @DataField(pos=27,pattern="MM/dd/yyyy HH:mm:ss" /*, pattern="yyyy-MM-dd HH:mm:ss"*/)
    private Date SA_SP_STOP_DTTM; //Agreement Map Table
    /**
     * PREMISE_TYPE
     */
    @DataField(pos = 28)
    private String PREMISE_TYPE;
    /**
     * ELEC_USAGE_NONRES
     */



    @DataField(pos = 29)
    private String ELEC_USAGE_NONRES;
    /**
     * HAS_3RD_PARTY_DRP
     */
    @DataField(pos = 30)
    private String HAS_3RD_PARTY_DRP;

    @DataField(pos = 31)
    private String PHONE_EXTENSION;

    @DataField(pos = 32)
    private String SUPPLIER_IS_DRP;

    @DataField(pos = 33)
    private String UNIQ_SA_ID;

    @DataField(pos = 34,pattern="MM/dd/yyyy HH:mm:ss")
    private Date UNIQ_SA_ID_CREATE_DATE;

    @DataField(pos = 35)
    private String UNIQ_SA_ID_WARN_FLAG;

    @DataField(pos = 36)
    private String SA_UUID;

    @DataField(pos = 37)
    private String DO_BUS_AS_NM;

    @DataField(pos = 38,pattern="MM/dd/yyyy")
    private Date SA_START_DATE;

    @DataField(pos = 39,pattern="MM/dd/yyyy")
    private Date SA_END_DATE;

    @DataField(pos = 40)
    private String SA_NAICS;

    @DataField(pos = 41)
    private String BILL_CYCLE_CD;

    @DataField(pos = 42)
    private String CUST_CLASS_CD;

    @DataField(pos = 43)
    private String REVENUE_CLASS_DESC;

    @DataField(pos = 44)
    private String FERA_FLAG;

    @DataField(pos = 45)
    private String BILL_SYSTEM;

    @DataField(pos = 46)
    private String CUST_SIZE;

    @DataField(pos = 47)
    private String MARKET_SEGMENT;

    @DataField(pos = 48)
    private String CARE_FLAG;

    @DataField(pos = 49)
    private String OPERATION_AREA;

    @DataField(pos = 50)
    private String PREM_BASELINE_CHAR;

    @DataField(pos = 51)
    private String MTR_BADGENBR;

    @DataField(pos = 52)
    private String SM_STATUS;

    @DataField(pos = 53, pattern="MM/dd/yyyy")
    private Date MTR_INSTALL_DT;

    @DataField(pos = 54, pattern="MM/dd/yyyy HH:mm:ss")
    private Date MTR_UNINSTALL_DT;

    @DataField(pos = 55)
    private String SM_MODULE_MFR;

    @DataField(pos = 56)
    private String MTR_CONFIG_TYPE;

    @DataField(pos = 57)
    private String MTR_READ_FREQ;

    @DataField(pos = 58)
    private String MTR_MFG;

    @DataField(pos = 59)
    private String SA_TYPE_CD;

    @DataField(pos = 60)
    private String CUSTOMER_MDMA_COMPANY_NAME;

    @DataField(pos = 61)
    private String CUSTOMER_MSP_COMPANY_NAME;

    @DataField(pos = 62)
    private String CUSTOMER_LSE_COMPANY_NAME;

    @DataField(pos = 63)
    private String CUST_METER_READ_CYCLE12;

    @DataField(pos = 64)
    private String CUST_SERVICE_VOLTAGE_CLASS;

    @DataField(pos = 65)
    private String SA_SP_ID;

    @DataField(pos = 66,pattern="MM/dd/yyyy")
    private Date RATE_CODE_EFFECTIVE_DATE;

    @DataField(pos = 67)
    private String SERVICE_TYPE;

    @DataField(pos = 68)
    private String BUSINESS_ACTIVITY_DESC;

    @DataField(pos = 69)
    private String BUS_OWNER;

    @DataField(pos = 70)
    private String DIVISION_CODE_19;

    @DataField(pos = 71)
    private String CIRCUIT_NUMBER;

    @DataField(pos = 72)
    private String SUB_STATION_NUMBER;

    @DataField(pos = 73)
    private String COUNTY;

    @DataField(pos = 74)
    private String CLIMATE_ZONE;

    @DataField(pos = 75)
    private String ESS_DIVISION_CODE;

    @DataField(pos = 76)
    private String SOURCE_SIDE_DEVICE_NUMBER;

    @DataField(pos = 77)
    private String LATITUDE;

    @DataField(pos = 78)
    private String LONGITUDE;

    @DataField(pos = 79)
    private String TRFMR_NUMBER;

    @DataField(pos = 80)
    private String TRFMR_BDG_NUMBER;

    @DataField(pos = 81)
    private String ROB_CODE;

    @DataField(pos = 82)
    private String CUSTOMER_MDMA_CODE;
    @DataField(pos = 83)
    private String CUSTOMER_MSP_CODE;
    @DataField(pos = 84)
    private String CUSTOMER_LSE_CODE;
    @DataField(pos = 85)
    private String ESSENTIAL_CUSTOMER_FLAG;

    private boolean lifeSupport;
    private boolean has3rdParty;
    private boolean supplierIsDRP;
    private boolean feraFlag;
    private boolean careFlag;
    private boolean resInd;


    @Override
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
        if("Y".equalsIgnoreCase(RES_YN_IND)){
            this.resInd = true;
        }
        if("Y".equalsIgnoreCase(MEDICAL_BASELINE_IND)){
            this.medicalBaseline = true;
        }
        if("Y".equalsIgnoreCase(LIFE_SUPPORT_IND)){
            this.lifeSupport = true;
        }
    }

}
