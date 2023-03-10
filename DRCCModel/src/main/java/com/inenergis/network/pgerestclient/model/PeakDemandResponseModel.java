package com.inenergis.network.pgerestclient.model;

import com.google.gson.annotations.SerializedName;
import com.inenergis.entity.genericEnum.DemandMinType;
import com.inenergis.entity.genericEnum.ElectricalUnit;
import com.inenergis.entity.genericEnum.Season;
import com.inenergis.model.PeakDemand;
import com.inenergis.util.EnergyUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeakDemandResponseModel extends PeakDemand {
    private static final Integer DEFAULT_SUMMER_ON_PEAK_HOURS = 120;
    private static final Integer DEFAULT_WINTER_PARTIAL_PEAK_HOURS = 260;

    @SerializedName("MonthYear")
    private String monthYear;

    @SerializedName("MaxKwDemand")
    private String maxKwDemand;

    @SerializedName("SummerOnPeakKw")
    private String summerOnPeakKw;

    @SerializedName("SummerOnPeakKwh")
    private String summerOnPeakKwh;

    @SerializedName("WinterPartialPeakKw")
    private String winterPartialPeakKw;

    @SerializedName("WinterPartialPeakKwh")
    private String winterPartialPeakKwh;

    @SerializedName("SummerOnPeakHours")
    private String summerOnPeakHours;

    @SerializedName("WinterPartialPeakHours")
    private String winterPartialPeakHours;

    public Double getAverageSummerOnPeakKw() {
        return getAverageKw(summerOnPeakKwh, summerOnPeakHours, DEFAULT_SUMMER_ON_PEAK_HOURS);
    }

    public Double getAverageWinterPartialPeakKw() {
        return getAverageKw(winterPartialPeakKwh, winterPartialPeakHours, DEFAULT_WINTER_PARTIAL_PEAK_HOURS);
    }

    private Double getAverageKw(String kwh, String hours, Integer defaultHours) {
        double parsedKwh;
        try {
            parsedKwh = Double.valueOf(kwh);
        } catch (NumberFormatException e) {
            parsedKwh = 0;
        }

        int parsedHours;
        try {
            parsedHours = Integer.valueOf(hours);
        } catch (NumberFormatException e) {
            parsedHours = defaultHours;
        }

        return parsedKwh / parsedHours;
    }

    @Override
    public long getValueWatts(DemandMinType demandMinType, Season season) {
        long valueWatts;
        switch (demandMinType) {
            case ON_PEAK:
                valueWatts = EnergyUtil.convertToWatts(getAverageSummerOnPeakKw().longValue(), ElectricalUnit.KW);
                break;
            case PARTIAL_PEAK:
                valueWatts = EnergyUtil.convertToWatts(getAverageWinterPartialPeakKw().longValue(), ElectricalUnit.KW);
                break;
            default:
                valueWatts = 0L;
        }

        return valueWatts;
    }
}