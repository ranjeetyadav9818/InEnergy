package com.inenergis.network.pgerestclient;

import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.bidding.Bid;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import com.inenergis.network.pgerestclient.model.BidRequestModel;
import com.inenergis.network.pgerestclient.model.BidResponseModel;
import com.inenergis.network.pgerestclient.model.BidStatusRequestModel;
import com.inenergis.network.pgerestclient.model.BidStatusResponseModel;
import com.inenergis.network.pgerestclient.model.PeakDemandRequestModel;
import com.inenergis.network.pgerestclient.model.PeakDemandResponseModel;
import com.inenergis.network.pgerestclient.model.PeakDemandResponseWrapper;
import com.inenergis.network.pgerestclient.model.RequestModel;
import com.inenergis.network.pgerestclient.model.ResourceRegistrationRequestModel;
import com.inenergis.network.pgerestclient.model.ResourceRegistrationResponseModel;
import com.inenergis.network.pgerestclient.model.weather.WeatherForecastResponse;
import org.apache.cxf.common.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class PgeLayer7 {

    private static final Logger log = LoggerFactory.getLogger(PgeLayer7.class);
    private Properties properties;

    public PgeLayer7(Properties properties) throws IOException {
        this.properties = properties;
    }

    public Map<BaseServiceAgreement, List<PeakDemandResponseModel>> getPeakDemand(List<BaseServiceAgreement> serviceAgreementsParam, LocalDateTime startDate, LocalDateTime endDate) throws Exception {
        List<BaseServiceAgreement> serviceAgreements = serviceAgreementsParam.stream().distinct().collect(Collectors.toList());
        Map<BaseServiceAgreement, List<PeakDemandResponseModel>> result = new HashMap<>();
        List<String> uuids = serviceAgreements.stream().filter(sa -> sa.getSaUuid() != null).map(BaseServiceAgreement::getSaUuid).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(uuids)){
            Map<String, List<PeakDemandResponseModel>> availabilities = getDemandData(startDate, endDate, uuids, RequestModel.IdType.UUID);
            for (String uuid : availabilities.keySet()) {
                BaseServiceAgreement serviceAgreement = serviceAgreements.stream().filter(sa -> uuid.equals(sa.getSaUuid())).findFirst().get();
                result.put(serviceAgreement,availabilities.get(uuid));
            }
        }
        List<String> saIds = serviceAgreements.stream().filter(sa -> sa.getSaUuid() == null).map(BaseServiceAgreement::getServiceAgreementId).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(saIds)){
            Map<String, List<PeakDemandResponseModel>> availabilities = getDemandData(startDate, endDate, saIds, RequestModel.IdType.SAID);
            for (String said : availabilities.keySet()) {
                BaseServiceAgreement serviceAgreement = serviceAgreements.stream().filter(sa -> said.equals(sa.getServiceAgreementId())).findFirst().get();
                result.put(serviceAgreement,availabilities.get(said));
            }
        }
        return result;
    }

    private Map<String, List<PeakDemandResponseModel>> getDemandData(LocalDateTime startDate, LocalDateTime endDate, List<String> ids, RequestModel.IdType idType) throws Exception {
        PeakDemandRequestModel requestModel = new PeakDemandRequestModel(ids, idType);
        requestModel.setStartDate(startDate);
        requestModel.setEndDate(endDate);

        return new PgeHttpClient(properties).doPost(requestModel, PeakDemandResponseWrapper.class).getAvailabilities();
    }

    public boolean validateMeterData(String id, RequestModel.IdType idType) throws IOException {
        return new MeterDataService(properties).validateMeterData(id, idType);
    }

    public boolean registerRegistration(RegistrationSubmissionStatus registration) throws Exception {
        return new PgeHttpClient(properties).doPost(new ResourceRegistrationRequestModel(registration), ResourceRegistrationResponseModel.class).isSuccess();
    }

    public BidResponseModel bid(Bid bid, String date) throws Exception {
        return new PgeHttpClient(properties).doPost(new BidRequestModel(bid, date), BidResponseModel.class, true);
    }

    public BidStatusResponseModel getBidStatus(String resourceId, String date) throws Exception {
        return new PgeHttpClient(properties).doPost(new BidStatusRequestModel(resourceId, date), BidStatusResponseModel.class, true);
    }

    public WeatherForecastResponse getWeatherForecast() throws Exception {
        String result = new PgeHttpClient(properties).doGet(properties.getProperty("pge.api.weather.forecast.url"));
        JAXBContext jaxbContext = JAXBContext.newInstance(WeatherForecastResponse.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        StringReader reader = new StringReader(result);
        WeatherForecastResponse weatherForecastResponse = (WeatherForecastResponse) unmarshaller.unmarshal(reader);
        return weatherForecastResponse;
    }
}