package com.inenergis.service;

import com.inenergis.controller.general.ConstantsProvider;
import com.inenergis.dao.CriteriaCondition;
import com.inenergis.dao.IsoRegistrationDao;
import com.inenergis.entity.ProductType;
import com.inenergis.entity.SubLap;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import com.inenergis.entity.marketIntegration.Iso;
import com.inenergis.util.ConstantsProviderModel;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus.RegistrationStatus.FINISHED;
import static com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus.RegistrationStatus.REGISTERED;
import static org.hibernate.criterion.MatchMode.EXACT;

@Stateless
public class IsoRegistrationService {

    @Inject
    IsoRegistrationDao isoRegistrationDao;

    public RegistrationSubmissionStatus getById(Long id) {
        return isoRegistrationDao.getById(id);
    }

    public List<RegistrationSubmissionStatus> getBiddableRegistrations(ProductType resourceType, SubLap subLap, LocalDateTime localTradeDate, Iso iso) {
        Date tradeDate = Date.from(localTradeDate.atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant());
        return isoRegistrationDao.getByTypeSublapStatusesAndDate(resourceType, subLap, Arrays.asList(REGISTERED,FINISHED),tradeDate, iso);
    }


    public List<RegistrationSubmissionStatus> getByIds(Collection<Long> ids) {
        return isoRegistrationDao.getByIds(ids);
    }

    public RegistrationSubmissionStatus getUniqueResultWithCriteria(List<CriteriaCondition> conditions) {
        return isoRegistrationDao.getUniqueResultWithCriteria(conditions);
    }

    public void save(RegistrationSubmissionStatus registration) {
        isoRegistrationDao.saveOrUpdate(registration);
    }
}