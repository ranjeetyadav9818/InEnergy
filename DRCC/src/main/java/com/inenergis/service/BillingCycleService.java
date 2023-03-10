package com.inenergis.service;

import com.inenergis.dao.BillingCycleDao;
import com.inenergis.dao.CriteriaCondition;
import com.inenergis.entity.BillingCycleSchedule;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class BillingCycleService {

    @Inject
    private BillingCycleDao billingCycleDao;

    public List<BillingCycleSchedule> getAll() {
        return billingCycleDao.getAll();
    }

    public void saveOrUpdate(BillingCycleSchedule billingCycle) {
        billingCycleDao.saveOrUpdate(billingCycle);
    }

    public void delete(BillingCycleSchedule billingCycle) {
        billingCycleDao.delete(billingCycle);
    }

    public BillingCycleSchedule getByDate(LocalDate date) {
        List<CriteriaCondition> conditions = new ArrayList<>();
        conditions.add(CriteriaCondition.builder().key("date").value(date).matchMode(MatchMode.EXACT).build());
        return billingCycleDao.getUniqueResultWithCriteria(conditions);
    }
}