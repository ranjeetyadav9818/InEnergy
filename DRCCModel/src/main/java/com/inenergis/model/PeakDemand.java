package com.inenergis.model;

import com.inenergis.entity.genericEnum.DemandMinType;
import com.inenergis.entity.genericEnum.Season;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public abstract class PeakDemand implements Comparable<PeakDemand> {

    protected transient LocalDate date;

    abstract public long getValueWatts(DemandMinType demandMinType, Season season);

    public int compareTo(PeakDemand peakDemandResponseModel) {
        return date.compareTo(peakDemandResponseModel.date);
    }
}