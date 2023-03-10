package com.inenergis.controller.servicePointData;

import com.inenergis.controller.authentication.AuthorizationChecker;
import com.inenergis.controller.carousel.GasServicePointCarousel;
import com.inenergis.controller.customerData.DataBeanList;
import com.inenergis.controller.customerData.GasDataBeanList;
import com.inenergis.controller.lazyDataModel.GasLazyAgreementPointMapDataModel;
import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.History;
import com.inenergis.entity.Premise;
import com.inenergis.entity.meterData.IntervalData;
import com.inenergis.service.HistoryService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.service.aws.IntervalDataService;
import com.inenergis.service.aws.PeakDemandDataService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
@Transactional
@Getter
@Setter
public class GasServicePointList implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String FILE_METER_DATA_COULD_NOT_BE_DOWNLOADED = "Meter Data file could not be downloaded";
    public static final String FILE_PEAK_DEMAND_COULD_NOT_BE_DOWNLOADED = "Peak Demand file could not be downloaded";

    @Inject
    EntityManager entityManager;

    @Inject
    AuthorizationChecker authorizationChecker;

    @Inject
    HistoryService historyService;

    @Inject
    IntervalDataService intervalDataService;

    @Inject
    PeakDemandDataService peakDemandDataService;

    @Inject
    UIMessage uiMessage;

    Logger log = LoggerFactory.getLogger(GasServicePointList.class);

    private String premisesId;
    private String servicePointId;
    private String address;
    private String city;
    private String postCode;

    private boolean showData = false;
    private boolean showSearch = true;
    private boolean showCarousel = false;

    private String energyUsageViewType = "hourly";
    private Date energyUsageDate;
    private Date energyUsageDateFrom;
    private Date energyUsageDateTo;
    private BarChartModel energyUsageModel;

    private List<Premise> premises = new ArrayList<>();

    private GasLazyAgreementPointMapDataModel agreementPointMapLazyModel;

    private AgreementPointMap selectedAgreementPointMap;
    private List<DataBeanList> premiseDetails = new ArrayList<>();

    private List<History> gasHistory;

    private GasServicePointCarousel servicePointCarousel = new GasServicePointCarousel();

    @PostConstruct
    public void init() {
        final String servicePointData = ParameterEncoderService.getDefaultDecodedParameter();
        String servicePointId =null;
        servicePointId = servicePointData.substring(0,10);
        if (servicePointId != null) {
            this.servicePointId = servicePointId;
            searchPremises();
        }
        LocalDateTime ldt = LocalDateTime.of(2015, Month.OCTOBER, 1, 0, 0);
        energyUsageDate = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        energyUsageDateFrom = Date.from(LocalDateTime.now().minusDays(30).atZone(ZoneId.systemDefault()).toInstant());
        energyUsageDateTo = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        createEnergyUsageModel();
    }

    public void onPremiseClear() {
        this.searchPremises();
        this.setShowSearch(true);
        this.setShowCarousel(false);
        this.clearCarouselData();
        this.selectedAgreementPointMap = null;
    }

    public void searchPremises() {

        agreementPointMapLazyModel = new GasLazyAgreementPointMapDataModel(this.address, this.city, this.postCode, this.servicePointId, this.premisesId, this);
        List<AgreementPointMap> agreementPointMapList = agreementPointMapLazyModel.load(0, 25, null, null, null);
        this.setShowData(true);
        if (agreementPointMapList.size() == 1) {
            this.selectedAgreementPointMap = agreementPointMapList.get(0);
            this.showAgreementPointMap(this.selectedAgreementPointMap);
        }
    }

    public void selectAgreementPointMap(SelectEvent event) {
        AgreementPointMap apm = (AgreementPointMap) event.getObject();
        if (apm != null) {
            showAgreementPointMap(apm);
        }
    }

    private void showAgreementPointMap(AgreementPointMap apm) {
        Premise prem = apm.getServicePoint().getPremise();
        servicePointId = apm.getServicePoint().getServicePointId();
        if (prem != null) {
            this.setShowSearch(false);
            this.setShowCarousel(true);
            agreementPointMapLazyModel = new GasLazyAgreementPointMapDataModel(null, null, null, null, prem.getPremiseId(), this);
            this.loadCarouselData();
            this.loadHistory();
        }
    }

    private void loadHistory() {
        gasHistory = historyService.getHistory(selectedAgreementPointMap);
    }


    public void onServiceAgreementSelect(SelectEvent event) throws IOException {
        AgreementPointMap apm = (AgreementPointMap) event.getObject();
        if (apm != null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("CustomerList.xhtml?o=" + ParameterEncoderService.encode(apm.getServiceAgreement().getServiceAgreementId()));
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    public void loadCarouselData() {
        premiseDetails.clear();
        final AgreementPointMap sa_sp = this.selectedAgreementPointMap;
        if (sa_sp != null) {
            premiseDetails = servicePointCarousel.printServicePointCarousel(sa_sp);
        }
    }

    public void clearCarouselData() {
        premiseDetails = new ArrayList<>();
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public String getPremisesId() {
        return premisesId;
    }

    public void setPremisesId(String premisesId) {
        this.premisesId = premisesId;
    }

    public String getServicePointId() {
        return servicePointId;
    }

    public void setServicePointId(String servicePointId) {
        this.servicePointId = servicePointId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public boolean isShowData() {
        return showData;
    }

    public void setShowData(boolean showData) {
        this.showData = showData;
    }

    public boolean isShowSearch() {
        return showSearch;
    }

    public void setShowSearch(boolean showSearch) {
        this.showSearch = showSearch;
    }

    public boolean isShowCarousel() {
        return showCarousel;
    }

    public void setShowCarousel(boolean showCarousel) {
        this.showCarousel = showCarousel;
    }

    public List<DataBeanList> getPremiseDetails() {
        return premiseDetails;
    }

    public void setPremiseDetails(List<DataBeanList> premiseDetails) {
        this.premiseDetails = premiseDetails;
    }

    public LazyDataModel<AgreementPointMap> getAgreementPointMapLazyModel() {
        return agreementPointMapLazyModel;
    }

    public void setAgreementPointMapLazyModel(GasLazyAgreementPointMapDataModel agreementPointMapLazyModel) {
        this.agreementPointMapLazyModel = agreementPointMapLazyModel;
    }

    public List<Premise> getPremises() {
        return premises;
    }

    public void setPremises(List<Premise> premises) {
        this.premises = premises;
    }

    public AgreementPointMap getSelectedAgreementPointMap() {
        return selectedAgreementPointMap;
    }

    public void setSelectedAgreementPointMap(AgreementPointMap selectedAgreementPointMap) {
        this.selectedAgreementPointMap = selectedAgreementPointMap;
    }

    public List<History> getHistory() {
        return gasHistory;
    }

    public void setGasHistory(List<History> gasHistory) {
        this.gasHistory = gasHistory;
    }

    public HistoryService getHistoryService() {
        return historyService;
    }

    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }

    private List<IntervalData> getIntervalData() {
        LocalDateTime dateFrom;
        LocalDateTime dateTo;
        if (energyUsageViewType.equals("hourly")) {
            dateFrom = LocalDateTime.ofInstant(energyUsageDate.toInstant(), ZoneId.systemDefault()).withHour(0).withMinute(0);
            dateTo = LocalDateTime.ofInstant(energyUsageDate.toInstant(), ZoneId.systemDefault()).withHour(23).withMinute(59);
        } else {
            dateFrom = LocalDateTime.ofInstant(this.energyUsageDateFrom.toInstant(), ZoneId.systemDefault()).withHour(0).withMinute(0);
            dateTo = LocalDateTime.ofInstant(this.energyUsageDateTo.toInstant(), ZoneId.systemDefault()).withHour(23).withMinute(59);
        }
        List<IntervalData> gasIntervalDataList = new ArrayList<>();
//        try {
//            intervalDataList = intervalDataService.getIntervalDate(servicePointId, dateFrom, dateTo);
//        } catch (IOException | SQLException e) {
//            uiMessage.addMessage(e.getMessage(), FacesMessage.SEVERITY_ERROR);
//            e.printStackTrace();
//        }

        return gasIntervalDataList;
    }

    public void showPreviousDay() {
        LocalDateTime ldt = LocalDateTime.ofInstant(energyUsageDate.toInstant(), ZoneId.systemDefault());
        energyUsageDate = Date.from(ldt.minusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        createEnergyUsageModel();
    }

    public void showNextDay() {
        LocalDateTime ldt = LocalDateTime.ofInstant(energyUsageDate.toInstant(), ZoneId.systemDefault());
        energyUsageDate = Date.from(ldt.plusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        createEnergyUsageModel();
    }

    public void createEnergyUsageModel(String id) {
        if (id.equals("energyUsageDateFrom")) {
            LocalDateTime dateFrom = LocalDateTime.ofInstant(this.energyUsageDateFrom.toInstant(), ZoneId.systemDefault()).withHour(0).withMinute(0);
            energyUsageDateTo = Date.from(dateFrom.plusMonths(1).atZone(ZoneId.systemDefault()).toInstant());
        } else if (id.equals("energyUsageDateTo")) {
            LocalDateTime dateTo = LocalDateTime.ofInstant(this.energyUsageDateTo.toInstant(), ZoneId.systemDefault()).withHour(0).withMinute(0);
            energyUsageDateFrom = Date.from(dateTo.minusMonths(1).atZone(ZoneId.systemDefault()).toInstant());
        }
        createEnergyUsageModel();
    }

    public void createEnergyUsageModel() {
        energyUsageModel = new BarChartModel();
        energyUsageModel.setExtender("transparentBackgroundChartExtender");
        ChartSeries costs = new ChartSeries();
        costs.setLabel("kW/h");

        if (energyUsageViewType.equals("hourly")) {
            getIntervalData().forEach(gasIntervalData -> costs.set(gasIntervalData.getLocalDateTime().getHour(), gasIntervalData.getValue().doubleValue()));
        } else {
            Map<LocalDate, Double> dailyUsage = new HashMap<>();
            Double usage;
            for (IntervalData data : getIntervalData()) {
                dailyUsage.putIfAbsent(data.getLocalDateTime().toLocalDate(), 0.0);
                usage = dailyUsage.get(data.getLocalDateTime().toLocalDate());
                dailyUsage.replace(data.getLocalDateTime().toLocalDate(), usage + data.getValue().doubleValue());
            }

            dailyUsage.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> costs.set(entry.getKey().getMonthValue() + "/" + entry.getKey().getDayOfMonth(), entry.getValue()));
        }

        if (!costs.getData().isEmpty()) {
            energyUsageModel.addSeries(costs);
        }

        energyUsageModel.setTitle("Energy Usage");
        energyUsageModel.setLegendPosition("ne");

        Axis yAxis = energyUsageModel.getAxis(AxisType.Y);
        yAxis.setMin(0);
    }

    public void changeEnergyUsageViewType() {
        createEnergyUsageModel();
    }

//    public StreamedContent getServicePointMeterDataFile(String servicePointId) {
//        final ByteArrayInputStream stream;
//        try {
//            stream = intervalDataService.getByServicePointId(servicePointId);
//            return new DefaultStreamedContent(stream, "text/plain", servicePointId + "MeterData.csv");
//        } catch (Exception e) {
//            log.error(FILE_METER_DATA_COULD_NOT_BE_DOWNLOADED, e);
//            uiMessage.addMessage(FILE_METER_DATA_COULD_NOT_BE_DOWNLOADED, FacesMessage.SEVERITY_ERROR);
//            return null;
//        }
//    }
//
//    public StreamedContent getServicePointPeKDemandDataFile(String servicePointId) {
//        final ByteArrayInputStream stream;
//        try {
//            stream = peakDemandDataService.getByServicePointId(servicePointId);
//            return new DefaultStreamedContent(stream, "text/plain", servicePointId + "PeakDemand.csv");
//        } catch (Exception e) {
//            log.error(FILE_PEAK_DEMAND_COULD_NOT_BE_DOWNLOADED, e);
//            uiMessage.addMessage(FILE_PEAK_DEMAND_COULD_NOT_BE_DOWNLOADED, FacesMessage.SEVERITY_ERROR);
//            return null;
//        }
//    }
}
