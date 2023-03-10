package com.inenergis.microbot.camel.processors;

import com.inenergis.microbot.camel.csv.Customer;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerProcessor implements Processor {

    static Logger log = LoggerFactory.getLogger(CustomerProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        Object body = exchange.getIn().getBody();
        //if(Debug.doDebug)log.info("Original input "+body+" class "+body.getClass());
        if (body instanceof HashMap) {
            Map m = (Map) body;
            if (!m.isEmpty()) {
                body = m.values().iterator().next();
            } else {
                return;
            }
        }


        Customer c = (Customer) body;

        List<Object> list = Arrays.asList(
                (Object) c.getSA_STATUS(),
                c.getSA_ID(),
                c.getACCOUNT_ID(),
                c.getPREMISE_ID(),
                c.getMETER_ID(),
                c.getPERSON_ID(),
                c.getLAST_NAME(),
                c.getSERVICE_ADDRESS1(),
                c.getSERVICE_ADDRESS2(),
                c.getSERVICE_CITY_UPR(),
                c.getSERVICE_STATE(),
                c.getSERVICE_POSTAL(),
                c.getPHONE(),
                c.getMAILING_ADDRESS1(),
                c.getMAILING_ADDRESS2(),
                c.getMAILING_CITY_UPR(),
                c.getMAILING_STATE(),
                c.getMAILING_ZIP(),
                c.getRS_CD(),
                Boolean.valueOf(c.isResInd()),
                c.getSUBSTATION(),
                c.getFEEDER(),
                c.getSP_ID(),
                Boolean.valueOf(c.isMedicalBaseline()),
                Boolean.valueOf(c.isLifeSupport()),
                Boolean.valueOf(c.isSmartRate()),
                c.getSA_SP_START_DTTM(),
                c.getSA_SP_STOP_DTTM(),
                c.getPREMISE_TYPE(),
                c.getSR_START_DATE(),
                c.getSR_END_DATE(),
                Boolean.valueOf(c.isPdpEnrolled()),
                c.getPDP_STATUS(),
                c.getPDP_START_DATE(),
                c.getPDP_STOP_DATE(),
                c.getPDP_PLAN_OPTIONS(),
                c.getELEC_USAGE_NONRES(),
                c.getPDP_RESV_CAP_VAL(),
                Boolean.valueOf(c.isHas3rdParty()),
                c.getPHONE_EXTENSION(),
                Boolean.valueOf(c.isSupplierIsDRP()),
                c.getUNIQ_SA_ID(),
                c.getUNIQ_SA_ID_CREATE_DATE(),
                c.getUNIQ_SA_ID_WARN_FLAG(),
                c.getSA_UUID(),
                c.getDO_BUS_AS_NM(),
                c.getSA_START_DATE(),
                c.getSA_END_DATE(),
                c.getSA_NAICS(),
                c.getBILL_CYCLE_CD(),
                c.getCUST_CLASS_CD(),
                c.getREVENUE_CLASS_DESC(),
                Boolean.valueOf(c.isFeraFlag()),
                c.getBILL_SYSTEM(),
                c.getCUST_SIZE(),
                c.getMARKET_SEGMENT(),
                Boolean.valueOf(c.isCareFlag()),
                c.getOPERATION_AREA(),
                c.getPREM_BASELINE_CHAR(),
                c.getMTR_BADGENBR(),
                c.getSM_STATUS(),
                c.getMTR_INSTALL_DT(),
                c.getMTR_UNINSTALL_DT(),
                c.getSM_MODULE_MFR(),
                c.getMTR_CONFIG_TYPE(),
                c.getMTR_READ_FREQ(),
                c.getMTR_MFG(),
                Boolean.valueOf(c.isPdpBillProtection()),
                Boolean.valueOf(c.isSrBillProtection()),
                c.getSA_TYPE_CD(),
                c.getCUSTOMER_MDMA_COMPANY_NAME(),
                c.getCUSTOMER_MSP_COMPANY_NAME(),
                c.getCUSTOMER_LSE_COMPANY_NAME(),
                c.getCUST_METER_READ_CYCLE12(),
                c.getCUST_SERVICE_VOLTAGE_CLASS(),
                c.getSA_SP_ID(),
                c.getRATE_CODE_EFFECTIVE_DATE(),
                c.getSERVICE_TYPE(),
                c.getBUSINESS_ACTIVITY_DESC(),
                c.getBUS_OWNER(),
                c.getDIVISION_CODE_19(),
                c.getCIRCUIT_NUMBER(),
                c.getSUB_STATION_NUMBER(),
                c.getCOUNTY(),
                c.getCLIMATE_ZONE(),
                c.getESS_DIVISION_CODE(),
                c.getSOURCE_SIDE_DEVICE_NUMBER(),
                c.getLATITUDE(),
                c.getLONGITUDE(),
                c.getTRFMR_NUMBER(),
                c.getTRFMR_BDG_NUMBER(),
                c.getROB_CODE(),
                c.getCUSTOMER_MDMA_CODE(),
                c.getCUSTOMER_MSP_CODE(),
                c.getCUSTOMER_LSE_CODE(),
                c.getESSENTIAL_CUSTOMER_FLAG());


        exchange.getIn().setBody(list, List.class);

    }

}
