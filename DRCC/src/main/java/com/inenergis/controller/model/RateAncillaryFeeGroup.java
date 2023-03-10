package com.inenergis.controller.model;

import com.inenergis.entity.genericEnum.GasRateAncillaryFrequency;
import com.inenergis.entity.genericEnum.RateAncillaryFrequency;
import com.inenergis.entity.masterCalendar.TimeOfUse;
import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;
import com.inenergis.entity.program.rateProgram.RateProfileAncillaryFee;
import com.inenergis.entity.program.rateProgram.RateTier;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.common.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Antonio on 19/07/2017.
 */
@Data
@AllArgsConstructor
public class RateAncillaryFeeGroup {

    private List<RateProfileAncillaryFee> fees;

    public String getName() {
        if (CollectionUtils.isEmpty(fees)) {
            return StringUtils.EMPTY;
        }
        return fees.get(0).getName();
    }


    public TimeOfUseCalendar getCalendar() {
        if (CollectionUtils.isEmpty(fees)) {
            return null;
        }
        return fees.get(0).getCalendar();
    }

    public RateAncillaryFrequency getFrequency(){
        if (CollectionUtils.isEmpty(fees)) {
            return null;
        }
        return fees.get(0).getFrequency();
    }
    public GasRateAncillaryFrequency getGasFrequency(){
        if (CollectionUtils.isEmpty(fees)) {
            return null;
        }
        return fees.get(0).getGasFrequency();
    }


    public RateProfileAncillaryFee getAncillaryFee(RateTier tier) {
        for (RateProfileAncillaryFee fee : fees) {
            if (fee.getRateTier().equals(tier)) {
                return fee;
            }
        }
        return null;
    }

    public TimeOfUse getTimeOfUse(){
        if (CollectionUtils.isEmpty(fees)) {
            return null;
        }
        return fees.get(0).getTimeOfUse();
    }

    public int getGroupId(){
        if (CollectionUtils.isEmpty(fees)) {
            return 0;
        }
        return fees.get(0).getGroupId();
    }

    public void setTimeOfUse(TimeOfUse timeOfUse){
        fees.forEach(fee -> fee.setTimeOfUse(timeOfUse));
    }

    public void setName(String name){
        fees.forEach(fee -> fee.setName(name));
    }

    public void setCalendar(TimeOfUseCalendar calendar){
        fees.forEach(fee -> fee.setCalendar(calendar));
    }

    public void setPrice(BigDecimal value){
        fees.forEach(fee -> fee.setPrice(value));
    }

    public void setFrequency(RateAncillaryFrequency frequency){
        fees.forEach(fee -> fee.setFrequency(frequency));
    }

    public void setGasFrequency(GasRateAncillaryFrequency gasFrequency){
        fees.forEach(fee -> fee.setGasFrequency(gasFrequency));
    }



}
