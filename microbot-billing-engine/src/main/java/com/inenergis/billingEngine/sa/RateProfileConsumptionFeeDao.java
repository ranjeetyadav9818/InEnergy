package com.inenergis.billingEngine.sa;

import com.inenergis.entity.genericEnum.RateConsumptionFeeType;
import com.inenergis.entity.genericEnum.RateEventFee;
import com.inenergis.entity.masterCalendar.TimeOfUse;
import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;
import com.inenergis.entity.program.rateProgram.RateProfileConsumptionFee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.LocalDateTime;
import java.util.List;

public interface RateProfileConsumptionFeeDao extends Repository<RateProfileConsumptionFee, LocalDateTime> {

    @Query("select fee from RateProfileConsumptionFee fee where (rateType = ?1 or rateType is null ) and (calendar = ?2 or calendar is null ) and (timeOfUse = ?3 or timeOfUse is null) and (event = ?4 or event is null )")
    List<RateProfileConsumptionFee> findAllBy(RateConsumptionFeeType rateType, TimeOfUseCalendar calendar, TimeOfUse timeOfUse, RateEventFee eventFee);
}
