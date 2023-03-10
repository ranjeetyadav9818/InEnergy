package com.inenergis.service;

import com.inenergis.dao.RateCodeProfileDao;
import com.inenergis.entity.program.rateProgram.RateCodeProfile;
import lombok.Getter;
import lombok.Setter;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
@Getter
@Setter
public class RateCodeProfileService {

    @Inject
    RateCodeProfileDao rateCodeProfileDao;

    public void update(RateCodeProfile rateCodeProfile) {
        rateCodeProfileDao.saveOrUpdate(rateCodeProfile);
    }
}
