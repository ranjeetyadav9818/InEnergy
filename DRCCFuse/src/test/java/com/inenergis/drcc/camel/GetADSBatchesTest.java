package com.inenergis.drcc.camel;

import com.caiso.ads.api.model.APIDispatchResponseType;
import com.caiso.ads.api.model.APITrajectoryResponseType;
import com.caiso.ads.api.model.DispatchBatchType;
import com.inenergis.util.soap.CaisoRequestWrapper;

public class GetADSBatchesTest
{
//    @Test
    public void testRoute() throws Exception {
        CaisoRequestWrapper caisoRequestWrapper = new CaisoRequestWrapper(CaisoTestProperties.getPropertiesForTestingCaiso());
        APIDispatchResponseType batchesSinceUUID = caisoRequestWrapper.getBatchesSinceUUID("DISP-D7BB1040-D715-4034-FFDC-AC19421EEF87");
        System.out.println(batchesSinceUUID);
    }

//    @Test
    public void testRoute2() throws Exception {
        CaisoRequestWrapper caisoRequestWrapper = new CaisoRequestWrapper(CaisoTestProperties.getPropertiesForTestingCaiso());
        APIDispatchResponseType batchesSinceUUID = caisoRequestWrapper.getBatchesSinceUUID("-1");
        for (DispatchBatchType dispatchBatchType : batchesSinceUUID.getDispatchBatchList().getDispatchBatch()) {
            try {
                DispatchBatchType dispatchBatch = caisoRequestWrapper.getDispatchBatch(dispatchBatchType.getBatchUID()).getRight();
                if (dispatchBatch.getInstructions().getInstruction() != null && !dispatchBatch.getInstructions().getInstruction().isEmpty()) {
                    System.out.println(dispatchBatch);
                } else {
                    System.out.println("dispatch " + dispatchBatch.getBatchUID() + " doesn't have instructions");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

//    @Test
    public void testRoute3() throws Exception {
        CaisoRequestWrapper caisoRequestWrapper = new CaisoRequestWrapper(CaisoTestProperties.getPropertiesForTestingCaiso());
        DispatchBatchType dispatchBatch = caisoRequestWrapper.getDispatchBatch("DISP-48D553A0-D697-4034-894F-AC15E2F57154").getRight();
        if (dispatchBatch.getInstructions().getInstruction() != null && !dispatchBatch.getInstructions().getInstruction().isEmpty()) {
            System.out.println(dispatchBatch);
        } else {
            System.out.println("dispatch " + dispatchBatch.getBatchUID() + " doesn't have instructions");
        }
    }

//    @Test
    public void testRouteisNewTD() throws Exception {
        CaisoRequestWrapper caisoRequestWrapper = new CaisoRequestWrapper(CaisoTestProperties.getPropertiesForTestingCaiso());
        boolean newTrajectoryData = caisoRequestWrapper.isNewTrajectoryData("DISP-48D553A0-D697-4034-894F-AC15E2F57154");
        System.out.println(newTrajectoryData);
    }

//    @Test
    public void testRouteGetTrajData() throws Exception {
        CaisoRequestWrapper caisoRequestWrapper = new CaisoRequestWrapper(CaisoTestProperties.getPropertiesForTestingCaiso());
        APITrajectoryResponseType trajectoryData = caisoRequestWrapper.getTrajectoryData("DISP-EEFAFAC0-E22F-4034-894F-AC15E2F57154").getRight();
        System.out.println(trajectoryData);
    }

//    @Test
    public void testRoute4() throws Exception {
        CaisoRequestWrapper caisoRequestWrapper = new CaisoRequestWrapper(CaisoTestProperties.getPropertiesForTestingCaiso());
        caisoRequestWrapper.validateBatch("DISP-48D553A0-D697-4034-894F-AC15E2F57154");
    }
}