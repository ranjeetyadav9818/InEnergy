package com.inenergis.microbot.camel.services;

import com.inenergis.entity.IsoOutage;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.microbot.camel.dao.IsoOutageDao;
import com.inenergis.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class IsoOutageService {

    private static final Logger logger = LoggerFactory.getLogger(IsoOutageService.class);

    @Autowired
    private IsoOutageDao isoOutageDao;

    public IsoOutage getByOrDefault(IsoResource resource, Date date, IsoOutage defaultIsoOutage) {
        IsoOutage isoOutage = getBy(resource, date);
        if (isoOutage == null) {
            isoOutage = defaultIsoOutage;
        }

        return isoOutage;
    }

    @Transactional
    private IsoOutage getBy(IsoResource resource, Date date) {
        return isoOutageDao.findFirstByIsoResourceIdAndDateGreaterThanEqualAndDateLessThanEqual(resource.getId(), TimeUtil.getStartOfDay(date), TimeUtil.getEndOfDay(date));
    }
}