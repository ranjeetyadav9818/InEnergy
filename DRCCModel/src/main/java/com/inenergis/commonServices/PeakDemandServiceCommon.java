package com.inenergis.commonServices;

import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.Layer7PeakDemandHistory;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.exception.BusinessException;
import com.inenergis.model.PeakDemandResponse;
import com.inenergis.network.pgerestclient.PgeLayer7;
import com.inenergis.network.pgerestclient.PgeLayer7Emulator;
import com.inenergis.network.pgerestclient.model.PeakDemandResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by egamas on 13/10/2017.
 */
public abstract class PeakDemandServiceCommon {

    private PgeLayer7 pgeLayer7;
    Logger log = LoggerFactory.getLogger(PeakDemandServiceCommon.class);

    protected PeakDemandServiceCommon(){
        try {
            this.pgeLayer7 = new PgeLayer7Emulator(getProperties());
        } catch (IOException e) {
            log.error("Error initializing PgeLayer7",e);
        }
    }

    protected abstract Properties getProperties();

    public PeakDemandResponse getPeakDemandData(List<BaseServiceAgreement> serviceAgreements) throws BusinessException {
        try {
            Map<BaseServiceAgreement, List<PeakDemandResponseModel>> peakDemandMap = pgeLayer7.getPeakDemand(serviceAgreements, LocalDateTime.now(), LocalDateTime.now().plusYears(1));
            peakDemandMap.forEach((serviceAgreement, models) -> models.sort(Comparator.comparing(PeakDemandResponseModel::getDate).reversed()));
            return new PeakDemandResponse(peakDemandMap);
        } catch (Exception e) {
            throw new BusinessException(BusinessException.ExceptionCode.CAN_NOT_CONNECT_TO_REMOTE_SERVICE, "Error connecting to PGE Layer 7 server", null);
        }
    }

    public void savePeakDemandResponses(PeakDemandResponse peakDemandResponse, Layer7PeakDemandHistoryServiceContract layer7PeakDemandHistoryService) {
        Layer7PeakDemandHistory layer7PeakDemandHistory;
        for (BaseServiceAgreement serviceAgreement : peakDemandResponse.getPeakDemandMap().keySet()) {
            List<PeakDemandResponseModel> peakDemandList = peakDemandResponse.getPeakDemandMap().get(serviceAgreement);
            for (PeakDemandResponseModel peakDemand : peakDemandList) {
                layer7PeakDemandHistory = Layer7PeakDemandHistory.builder()
                        .serviceAgreement(serviceAgreement)
                        .monthYear(peakDemand.getMonthYear())
                        .maxKwDemand(peakDemand.getMaxKwDemand())
                        .summerOnPeakKw(peakDemand.getSummerOnPeakKw())
                        .summerOnPeakKwH(peakDemand.getSummerOnPeakKwh())
                        .winterPartialPeakKw(peakDemand.getWinterPartialPeakKw())
                        .winterPartialPeakKwH(peakDemand.getWinterPartialPeakKwh())
                        .summerOnPeakHours(peakDemand.getSummerOnPeakHours())
                        .winterPartialPeakHours(peakDemand.getWinterPartialPeakHours())
                        .build();
                layer7PeakDemandHistoryService.save(layer7PeakDemandHistory);
            }
        }
    }
}
