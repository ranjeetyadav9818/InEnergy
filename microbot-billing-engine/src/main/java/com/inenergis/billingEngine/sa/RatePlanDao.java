package com.inenergis.billingEngine.sa;

import com.inenergis.entity.genericEnum.DayOfWeek;
import com.inenergis.entity.program.RatePlanProfile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.LocalDate;
import java.util.List;

public interface RatePlanDao extends Repository<RatePlanProfile, String> {

    @Query("select profile from RatePlanProfile  profile where (profile.lastPaymentDate is null OR profile.lastPaymentDate < ?4) AND (profile.billingTermFrequency = 'WEEKLY' AND profile.billingDayOfWeek = ?1) OR (profile.billingTermFrequency = 'MONTHLY' AND profile.billingDayOfMonth = ?2) OR (profile.billingTermFrequency = 'ANNUALLY' AND profile.billingDayOfMonth = ?2 AND profile.billingMonth = ?3)")
    List<RatePlanProfile> findAllBy(DayOfWeek dayOfWeek, Integer dayOfMonth, Integer month, LocalDate localDate);
}
