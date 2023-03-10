package com.inenergis.billingEngine.service;

import com.inenergis.entity.genericEnum.DayOfWeek;
import com.inenergis.entity.program.RatePlan;
import com.inenergis.entity.program.RatePlanProfile;
import com.inenergis.billingEngine.sa.RatePlanDao;
import com.inenergis.billingEngine.sa.RatePlanProfileDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RatePlanService {

    @Autowired
    private RatePlanDao ratePlanDao;

    @Autowired
    private RatePlanProfileDao ratePlanProfileDao;

    @Transactional("mysqlTransactionManager")
    public List<RatePlanProfile> findBy(java.time.DayOfWeek dayOfWeek, Integer dayOfMonth, Integer month) {
        return ratePlanDao.findAllBy(DayOfWeek.valueOf(dayOfWeek.name()), dayOfMonth, month, LocalDate.now());
    }

    @Transactional("mysqlTransactionManager")
    public Set<RatePlan> findAllWithDueDate(LocalDate localDate) {
        List<RatePlanProfile> ratePlanProfiles = findBy(localDate.getDayOfWeek(), localDate.getDayOfMonth(), localDate.getMonthValue());

        return ratePlanProfiles.stream()
                .filter(RatePlanProfile::isActive)
                .map(RatePlanProfile::getRatePlan)
                .collect(Collectors.toSet());
    }

    public void updateLastPaymentDate(RatePlanProfile ratePlanProfile) {
        ratePlanProfile.setLastPaymentDate(LocalDate.now());
        ratePlanProfileDao.save(ratePlanProfile);
    }
}
