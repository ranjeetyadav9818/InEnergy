package com.inenergis.service;

import com.inenergis.dao.AwardDao;
import com.inenergis.dao.AwardExceptionDao;
import com.inenergis.entity.award.Award;
import com.inenergis.entity.award.AwardException;
import com.inenergis.entity.award.Trajectory;
import com.inenergis.util.ConstantsProviderModel;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.inenergis.util.ConstantsProviderModel.MW_PRECISION;

@Stateless
public class AwardService {
    @Inject
    AwardDao awardDao;

    @Inject
    AwardExceptionDao awardExceptionDao;

    public void save(Award award) {
        awardDao.save(award);
    }

    public List<Award> getAll() {
        return awardDao.getAll();
    }

    public Award getById(Long id) {
        return awardDao.getById(id);
    }

    public void saveException(AwardException awardException) {
        awardExceptionDao.saveOrUpdate(awardException);
    }

    public AwardException getExcetionById(Long id) {
        return awardExceptionDao.getById(id);
    }

    public List<Long> calculateAwardCapacity(Award award) {
        List<Long> capacities = new ArrayList<>(24);
        for (int i = 0; i < 24; i++) {
            long biggestCapacity = 0L;
            for (Trajectory trajectory : award.getTrajectories()) {
                if (trajectory.getTargetTime().toInstant().atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).getHour()==i) {
                    biggestCapacity = Math.max(biggestCapacity,trajectory.getDop());
                }
            }
            capacities.add(biggestCapacity * MW_PRECISION);
        }
        return capacities;
    }
}
