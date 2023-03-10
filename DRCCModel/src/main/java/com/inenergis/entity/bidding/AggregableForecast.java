package com.inenergis.entity.bidding;

import com.inenergis.entity.HourEndObject;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.entity.workflow.AggregableHourEnd;
import com.inenergis.entity.workflow.ModifiableHourEnd;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AggregableForecast implements AggregableHourEnd {

    private ModifiableHourEnd hourEndObject;

    private ProgramServiceAgreementEnrollment enrollment;

    @Override
    public void add(HourEndObject heObject) {
        for (int i = 1 ; i <= 24 ; i++){
            hourEndObject.setHour(i, (Long) hourEndObject.getHourEnd(i) + (Long) heObject.getHourEnd(i));
        }
    }

    @Override
    public void substract(HourEndObject heObject) {
        for (int i = 1 ; i <= 24 ; i++){
            hourEndObject.setHour(i, Math.max(0L,(Long) hourEndObject.getHourEnd(i) - (Long) heObject.getHourEnd(i)));
        }
    }

    @Override
    public void substractToAllHourEnds(long value) {
        for (int i = 1 ; i <= 24 ; i++){
            hourEndObject.setHour(i, Math.max(0L,(Long) hourEndObject.getHourEnd(i) - value));
        }
    }

    public void add(List<AggregableForecast> list){
        for (AggregableForecast object : list) {
            add(object.getHourEndObject());
        }
    }

    public void summarize(List<AggregableForecast> list){
        for (int i = 1 ; i <= 24 ; i++){
            final int ind = i;
            final LongSummaryStatistics heStats = list.stream().collect(Collectors.summarizingLong(f -> (long) f.getHourEndObject().getHourEnd(ind)));
            this.getHourEndObject().setHour(i, ((long) heStats.getAverage()));
        }
    }

}
