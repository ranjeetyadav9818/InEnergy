package com.inenergis.microbot.camel.csv;

/**
 * Created by egamas on 24/05/2017.
 */
public interface CustomerInterface {
    void solveBooleanValues();

    String getSA_STATUS();

    String getSA_ID();

    String getACCOUNT_ID();

    String getPREMISE_ID();

    String getMETER_ID();

    String getPERSON_ID();

    String getLAST_NAME();

    String getSERVICE_ADDRESS1();

    String getSERVICE_ADDRESS2();

    String getSERVICE_CITY_UPR();

    String getSERVICE_STATE();

    String getSERVICE_POSTAL();

    String getPHONE();

    String getMAILING_ADDRESS1();

    String getMAILING_ADDRESS2();

    String getMAILING_CITY_UPR();

    String getMAILING_STATE();

    String getMAILING_ZIP();

    String getRS_CD();

    String getRES_YN_IND();

    String getSUBSTATION();

    String getFEEDER();

    String getSP_ID();

    String getMEDICAL_BASELINE_IND();

    boolean isMedicalBaseline();

    String getLIFE_SUPPORT_IND();

    java.util.Date getSA_SP_START_DTTM();

    java.util.Date getSA_SP_STOP_DTTM();

    String getPREMISE_TYPE();

    String getELEC_USAGE_NONRES();

    String getHAS_3RD_PARTY_DRP();

    String getPHONE_EXTENSION();

    String getSUPPLIER_IS_DRP();

    String getUNIQ_SA_ID();

    java.util.Date getUNIQ_SA_ID_CREATE_DATE();

    String getUNIQ_SA_ID_WARN_FLAG();

    String getSA_UUID();

    String getDO_BUS_AS_NM();

    java.util.Date getSA_START_DATE();

    java.util.Date getSA_END_DATE();

    String getSA_NAICS();

    String getBILL_CYCLE_CD();

    String getCUST_CLASS_CD();

    String getREVENUE_CLASS_DESC();

    String getFERA_FLAG();

    String getBILL_SYSTEM();

    String getCUST_SIZE();

    String getMARKET_SEGMENT();

    String getCARE_FLAG();

    String getOPERATION_AREA();

    String getPREM_BASELINE_CHAR();

    String getMTR_BADGENBR();

    String getSM_STATUS();

    java.util.Date getMTR_INSTALL_DT();

    java.util.Date getMTR_UNINSTALL_DT();

    String getSM_MODULE_MFR();

    String getMTR_CONFIG_TYPE();

    String getMTR_READ_FREQ();

    String getMTR_MFG();

    String getSA_TYPE_CD();

    String getCUSTOMER_MDMA_COMPANY_NAME();

    String getCUSTOMER_MSP_COMPANY_NAME();

    String getCUSTOMER_LSE_COMPANY_NAME();

    String getCUST_METER_READ_CYCLE12();

    String getCUST_SERVICE_VOLTAGE_CLASS();

    String getSA_SP_ID();

    java.util.Date getRATE_CODE_EFFECTIVE_DATE();

    String getSERVICE_TYPE();

    String getBUSINESS_ACTIVITY_DESC();

    String getBUS_OWNER();

    String getDIVISION_CODE_19();

    String getCIRCUIT_NUMBER();

    String getSUB_STATION_NUMBER();

    String getCOUNTY();

    String getCLIMATE_ZONE();

    String getESS_DIVISION_CODE();

    String getSOURCE_SIDE_DEVICE_NUMBER();

    String getLATITUDE();

    String getLONGITUDE();

    String getTRFMR_NUMBER();

    String getTRFMR_BDG_NUMBER();

    String getROB_CODE();

    String getCUSTOMER_MDMA_CODE();

    String getCUSTOMER_MSP_CODE();

    String getCUSTOMER_LSE_CODE();

    String getESSENTIAL_CUSTOMER_FLAG();

    boolean isLifeSupport();

    boolean isHas3rdParty();

    boolean isSupplierIsDRP();

    boolean isFeraFlag();

    boolean isCareFlag();

    boolean isResInd();

    void setSA_STATUS(String SA_STATUS);

    void setSA_ID(String SA_ID);

    void setACCOUNT_ID(String ACCOUNT_ID);

    void setPREMISE_ID(String PREMISE_ID);

    void setMETER_ID(String METER_ID);

    void setPERSON_ID(String PERSON_ID);

    void setLAST_NAME(String LAST_NAME);

    void setSERVICE_ADDRESS1(String SERVICE_ADDRESS1);

    void setSERVICE_ADDRESS2(String SERVICE_ADDRESS2);

    void setSERVICE_CITY_UPR(String SERVICE_CITY_UPR);

    void setSERVICE_STATE(String SERVICE_STATE);

    void setSERVICE_POSTAL(String SERVICE_POSTAL);

    void setPHONE(String PHONE);

    void setMAILING_ADDRESS1(String MAILING_ADDRESS1);

    void setMAILING_ADDRESS2(String MAILING_ADDRESS2);

    void setMAILING_CITY_UPR(String MAILING_CITY_UPR);

    void setMAILING_STATE(String MAILING_STATE);

    void setMAILING_ZIP(String MAILING_ZIP);

    void setRS_CD(String RS_CD);

    void setRES_YN_IND(String RES_YN_IND);

    void setSUBSTATION(String SUBSTATION);

    void setFEEDER(String FEEDER);

    void setSP_ID(String SP_ID);

    void setMEDICAL_BASELINE_IND(String MEDICAL_BASELINE_IND);

    void setMedicalBaseline(boolean medicalBaseline);

    void setLIFE_SUPPORT_IND(String LIFE_SUPPORT_IND);

    void setSA_SP_START_DTTM(java.util.Date SA_SP_START_DTTM);

    void setSA_SP_STOP_DTTM(java.util.Date SA_SP_STOP_DTTM);

    void setPREMISE_TYPE(String PREMISE_TYPE);

    void setELEC_USAGE_NONRES(String ELEC_USAGE_NONRES);

    void setHAS_3RD_PARTY_DRP(String HAS_3RD_PARTY_DRP);

    void setPHONE_EXTENSION(String PHONE_EXTENSION);

    void setSUPPLIER_IS_DRP(String SUPPLIER_IS_DRP);

    void setUNIQ_SA_ID(String UNIQ_SA_ID);

    void setUNIQ_SA_ID_CREATE_DATE(java.util.Date UNIQ_SA_ID_CREATE_DATE);

    void setUNIQ_SA_ID_WARN_FLAG(String UNIQ_SA_ID_WARN_FLAG);

    void setSA_UUID(String SA_UUID);

    void setDO_BUS_AS_NM(String DO_BUS_AS_NM);

    void setSA_START_DATE(java.util.Date SA_START_DATE);

    void setSA_END_DATE(java.util.Date SA_END_DATE);

    void setSA_NAICS(String SA_NAICS);

    void setBILL_CYCLE_CD(String BILL_CYCLE_CD);

    void setCUST_CLASS_CD(String CUST_CLASS_CD);

    void setREVENUE_CLASS_DESC(String REVENUE_CLASS_DESC);

    void setFERA_FLAG(String FERA_FLAG);

    void setBILL_SYSTEM(String BILL_SYSTEM);

    void setCUST_SIZE(String CUST_SIZE);

    void setMARKET_SEGMENT(String MARKET_SEGMENT);

    void setCARE_FLAG(String CARE_FLAG);

    void setOPERATION_AREA(String OPERATION_AREA);

    void setPREM_BASELINE_CHAR(String PREM_BASELINE_CHAR);

    void setMTR_BADGENBR(String MTR_BADGENBR);

    void setSM_STATUS(String SM_STATUS);

    void setMTR_INSTALL_DT(java.util.Date MTR_INSTALL_DT);

    void setMTR_UNINSTALL_DT(java.util.Date MTR_UNINSTALL_DT);

    void setSM_MODULE_MFR(String SM_MODULE_MFR);

    void setMTR_CONFIG_TYPE(String MTR_CONFIG_TYPE);

    void setMTR_READ_FREQ(String MTR_READ_FREQ);

    void setMTR_MFG(String MTR_MFG);

    void setSA_TYPE_CD(String SA_TYPE_CD);

    void setCUSTOMER_MDMA_COMPANY_NAME(String CUSTOMER_MDMA_COMPANY_NAME);

    void setCUSTOMER_MSP_COMPANY_NAME(String CUSTOMER_MSP_COMPANY_NAME);

    void setCUSTOMER_LSE_COMPANY_NAME(String CUSTOMER_LSE_COMPANY_NAME);

    void setCUST_METER_READ_CYCLE12(String CUST_METER_READ_CYCLE12);

    void setCUST_SERVICE_VOLTAGE_CLASS(String CUST_SERVICE_VOLTAGE_CLASS);

    void setSA_SP_ID(String SA_SP_ID);

    void setRATE_CODE_EFFECTIVE_DATE(java.util.Date RATE_CODE_EFFECTIVE_DATE);

    void setSERVICE_TYPE(String SERVICE_TYPE);

    void setBUSINESS_ACTIVITY_DESC(String BUSINESS_ACTIVITY_DESC);

    void setBUS_OWNER(String BUS_OWNER);

    void setDIVISION_CODE_19(String DIVISION_CODE_19);

    void setCIRCUIT_NUMBER(String CIRCUIT_NUMBER);

    void setSUB_STATION_NUMBER(String SUB_STATION_NUMBER);

    void setCOUNTY(String COUNTY);

    void setCLIMATE_ZONE(String CLIMATE_ZONE);

    void setESS_DIVISION_CODE(String ESS_DIVISION_CODE);

    void setSOURCE_SIDE_DEVICE_NUMBER(String SOURCE_SIDE_DEVICE_NUMBER);

    void setLATITUDE(String LATITUDE);

    void setLONGITUDE(String LONGITUDE);

    void setTRFMR_NUMBER(String TRFMR_NUMBER);

    void setTRFMR_BDG_NUMBER(String TRFMR_BDG_NUMBER);

    void setROB_CODE(String ROB_CODE);

    void setCUSTOMER_MDMA_CODE(String CUSTOMER_MDMA_CODE);

    void setCUSTOMER_MSP_CODE(String CUSTOMER_MSP_CODE);

    void setCUSTOMER_LSE_CODE(String CUSTOMER_LSE_CODE);

    void setESSENTIAL_CUSTOMER_FLAG(String ESSENTIAL_CUSTOMER_FLAG);

    void setLifeSupport(boolean lifeSupport);

    void setHas3rdParty(boolean has3rdParty);

    void setSupplierIsDRP(boolean supplierIsDRP);

    void setFeraFlag(boolean feraFlag);

    void setCareFlag(boolean careFlag);

    void setResInd(boolean resInd);
}
