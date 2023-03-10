package com.inenergis.controller.model;

import com.inenergis.entity.genericEnum.RateConsumptionFeeType;
import com.inenergis.entity.genericEnum.RateEventFee;
import com.inenergis.entity.masterCalendar.TimeOfUse;
import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;
import com.inenergis.entity.program.rateProgram.RateProfileConsumptionFee;
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
public class RateConsumptionFeeGroup {

    private List<RateProfileConsumptionFee> fees;

    public RateEventFee getEvent() {
        if (CollectionUtils.isEmpty(fees)) {
            return null;
        }
        return fees.get(0).getEvent();
    }

    public RateConsumptionFeeType getRateType() {
        if (CollectionUtils.isEmpty(fees)) {
            return null;
        }
        return fees.get(0).getRateType();
    }

    public TimeOfUse getTimeOfUse() {
        if (CollectionUtils.isEmpty(fees)) {
            return null;
        }
        return fees.get(0).getTimeOfUse();
    }

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

    public RateProfileConsumptionFee getConsumptionFee(RateTier tier) {
        for (RateProfileConsumptionFee fee : fees) {
            if (fee.getRateTier().equals(tier)) {
                return fee;
            }
        }
        return null;
    }

    public int getGroupId(){
        if (CollectionUtils.isEmpty(fees)) {
            return 0;
        }
        return fees.get(0).getGroupId();
    }

    public void setName(String name){
        fees.forEach(fee -> fee.setName(name));
    }

    public void setCalendar(TimeOfUseCalendar calendar){
        fees.forEach(fee -> fee.setCalendar(calendar));
    }

    public void setTimeOfUse(TimeOfUse tou){
        fees.forEach(fee -> fee.setTimeOfUse(tou));
    }

    public void setEvent(RateEventFee event){
        fees.forEach(fee -> fee.setEvent(event));
    }

    public void setRateType(RateConsumptionFeeType type){
        fees.forEach(fee -> fee.setRateType(type));
    }

    public void setPrice(BigDecimal value){
        fees.forEach(fee -> fee.setPrice(value));
    }



}
