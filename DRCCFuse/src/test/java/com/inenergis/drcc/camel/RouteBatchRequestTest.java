package com.inenergis.drcc.camel;

import com.caiso.soa.batchvalidationstatus_v1.BatchStatus;
import com.caiso.soa.batchvalidationstatus_v1.BatchValidationStatus;
import com.caiso.soa.batchvalidationstatus_v1.MessagePayload;
import com.inenergis.util.soap.CaisoRequestWrapper;

public class RouteBatchRequestTest //extends CamelBlueprintTestSupport
{


//    @Test
    public void testRoute() throws Exception{
        CaisoRequestWrapper caisoRequestWrapper = new CaisoRequestWrapper(CaisoTestProperties.getPropertiesForTestingCaiso());
        BatchValidationStatus batchValidationStatus = caisoRequestWrapper.retrieveBatchStatus(generateBatchValidationStatus("11691"));
        System.out.println(batchValidationStatus);
    }

    private BatchValidationStatus generateBatchValidationStatus( String mrid) {
        BatchValidationStatus batchStatus = new BatchValidationStatus();
        MessagePayload payload = new MessagePayload();
        BatchStatus status = new BatchStatus();
        status.setMRID(mrid);
        payload.setBatchStatus(status);
        batchStatus.setMessagePayload(payload);
        return batchStatus;
    }
}