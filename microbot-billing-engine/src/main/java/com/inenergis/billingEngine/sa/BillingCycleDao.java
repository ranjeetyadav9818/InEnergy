package com.inenergis.billingEngine.sa;

import com.inenergis.entity.BillingCycleSchedule;
import org.springframework.data.repository.Repository;

import java.time.LocalDate;

public interface BillingCycleDao extends Repository<BillingCycleSchedule, Long> {

    BillingCycleSchedule findByDate(LocalDate date);

    void save(BillingCycleSchedule billingCycleSchedule);
}
