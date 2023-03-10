package com.inenergis.network.pgerestclient;

import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.bidding.Bid;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import com.inenergis.network.pgerestclient.model.BidResponseModel;
import com.inenergis.network.pgerestclient.model.BidStatusResponseModel;
import com.inenergis.network.pgerestclient.model.PeakDemandResponseModel;
import com.inenergis.network.pgerestclient.model.RequestModel;
import com.inenergis.network.pgerestclient.model.weather.WeatherForecastResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class PgeLayer7Emulator extends PgeLayer7 {

    public PgeLayer7Emulator(Properties properties) throws IOException {
        super(properties);
    }

    @Override
    public Map<BaseServiceAgreement, List<PeakDemandResponseModel>> getPeakDemand(List<BaseServiceAgreement> serviceAgreements, LocalDateTime startDate, LocalDateTime endDate) {
        Map<BaseServiceAgreement, List<PeakDemandResponseModel>> peakDemandMap = new HashMap<>();

        PeakDemandResponseModel dummy = new PeakDemandResponseModel();
        dummy.setMaxKwDemand("1000");
        dummy.setDate(LocalDate.now());
        dummy.setMonthYear("05/2017");
        dummy.setSummerOnPeakHours("7");
        dummy.setSummerOnPeakKwh("1000");
        dummy.setSummerOnPeakKw("1000");
        dummy.setWinterPartialPeakHours("1000");
        dummy.setWinterPartialPeakKw("1000");
        dummy.setWinterPartialPeakKwh("1000");
        for (BaseServiceAgreement agreement : serviceAgreements) {
            peakDemandMap.put(agreement, Collections.singletonList(dummy));
        }

        return peakDemandMap;
    }

    @Override
    public boolean validateMeterData(String id, RequestModel.IdType idType) throws IOException {
        return true;
    }

    @Override
    public boolean registerRegistration(RegistrationSubmissionStatus registration) throws Exception {
        return true;
    }

    @Override
    public BidResponseModel bid(Bid bid, String date) throws Exception {
        return new BidResponseModel();
    }

    @Override
    public BidStatusResponseModel getBidStatus(String resourceId, String date) throws Exception {
        return new BidStatusResponseModel();
    }

    @Override
    public WeatherForecastResponse getWeatherForecast() throws Exception {
        return new WeatherForecastResponse();
    }
}
