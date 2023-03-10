package com.inenergis.model;

import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.genericEnum.ElectricalUnit;
import com.inenergis.network.pgerestclient.model.PeakDemandResponseModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import static com.inenergis.util.EnergyUtil.convertToWatts;

@Getter
@Setter
public class PeakDemandResponse implements Serializable {

    private Map<BaseServiceAgreement, List<PeakDemandResponseModel>> peakDemandMap = new HashMap<>();

    public PeakDemandResponse(Map<BaseServiceAgreement, List<PeakDemandResponseModel>> peakDemandMap) {
        peakDemandMap.forEach((serviceAgreement, models) -> models.sort(Comparator.comparing(PeakDemandResponseModel::getDate).reversed()));
        this.peakDemandMap = peakDemandMap;
    }

    public boolean containsServiceAgreement(BaseServiceAgreement serviceAgreement) {
        return peakDemandMap.containsKey(serviceAgreement);
    }

    public Long getAverageSummerOnPeakWatt(BaseServiceAgreement serviceAgreement) {
        OptionalDouble value = peakDemandMap.get(serviceAgreement).stream()
                .mapToDouble(PeakDemandResponseModel::getAverageSummerOnPeakKw)
                .filter(monthAverage -> monthAverage != 0)
                .limit(6)
                .average();

        return convertToWatts(value.orElse(0), ElectricalUnit.KW);
    }

    public List<Long> getFslAverageSummerOnPeakWattList(BaseServiceAgreement serviceAgreement, int fslTimeHorizon) {
        return peakDemandMap.get(serviceAgreement).stream()
                .limit(fslTimeHorizon)
                .mapToDouble(PeakDemandResponseModel::getAverageSummerOnPeakKw)
                .filter(monthAverage -> monthAverage != 0)
                .boxed()
                .map(monthAverage -> convertToWatts(monthAverage, ElectricalUnit.KW))
                .collect(Collectors.toList());
    }

    public List<Long> getGasFslAverageSummerOnPeakWattList(BaseServiceAgreement serviceAgreement, int fslTimeHorizon) {
        return peakDemandMap.get(serviceAgreement).stream()
                .limit(fslTimeHorizon)
                .mapToDouble(PeakDemandResponseModel::getAverageSummerOnPeakKw)
                .filter(monthAverage -> monthAverage != 0)
                .boxed()
                .map(monthAverage -> convertToWatts(monthAverage, ElectricalUnit.THERMS))
                .collect(Collectors.toList());
    }

    public List<Long> getFslAverageWinterPartialPeakWattList(BaseServiceAgreement serviceAgreement, int fslTimeHorizon) {
        return peakDemandMap.get(serviceAgreement).stream()
                .limit(fslTimeHorizon)
                .mapToDouble(PeakDemandResponseModel::getAverageWinterPartialPeakKw)
                .filter(monthAverage -> monthAverage != 0)
                .boxed()
                .map(monthAverage -> convertToWatts(monthAverage, ElectricalUnit.KW))
                .collect(Collectors.toList());
    }

    public List<Long> getGasFslAverageWinterPartialPeakWattList(BaseServiceAgreement serviceAgreement, int fslTimeHorizon) {
        return peakDemandMap.get(serviceAgreement).stream()
                .limit(fslTimeHorizon)
                .mapToDouble(PeakDemandResponseModel::getAverageWinterPartialPeakKw)
                .filter(monthAverage -> monthAverage != 0)
                .boxed()
                .map(monthAverage -> convertToWatts(monthAverage, ElectricalUnit.THERMS))
                .collect(Collectors.toList());
    }

    public List<PeakDemand> getPeakDemandGenericList(BaseServiceAgreement serviceAgreement) {
        List<PeakDemand> peakDemandGenericList = new ArrayList<>();
        peakDemandGenericList.addAll(peakDemandMap.get(serviceAgreement));

        return peakDemandGenericList;
    }
}