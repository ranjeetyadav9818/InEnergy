package com.inenergis.service;

import com.inenergis.dao.CriteriaCondition;
import com.inenergis.dao.NoComparisonCheck;
import com.inenergis.dao.RateCodeDao;
import com.inenergis.entity.genericEnum.RateCodeSector;
import com.inenergis.entity.program.rateProgram.RateCode;
import com.inenergis.entity.program.rateProgram.RateCodeSectors;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.formula.CollaboratingWorkbooksEnvironment;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@Getter
@Setter
public class RateCodeService {

    @Inject
    RateCodeDao rateCodeDao;

    public RateCode getById(Long id) {
        return rateCodeDao.getById(id);
    }

    public RateCode save(RateCode code) {
        code.setLastUpdate(new Date());
        return rateCodeDao.save(code);
    }

    public RateCode update(RateCode code) {
        code.setLastUpdate(new Date());
        return rateCodeDao.saveOrUpdate(code);
    }

    public Long searchOthersWithSameCodeAndSector(RateCode rateCode) {
        List<CriteriaCondition> conditions = new ArrayList<>();
        conditions.add(CriteriaCondition.builder().key("name").value(rateCode.getName()).matchMode(MatchMode.EXACT).build());
        final List<RateCodeSector> rateCodeSectorList = rateCode.getSectors().stream().map(s -> s.getSector()).collect(Collectors.toList());
        conditions.add(CriteriaCondition.builder().key("sectors.sector").value(rateCodeSectorList).matchMode(MatchMode.EXACT).build());
        if (rateCode.getId() != null) {
            conditions.add(CriteriaCondition.builder().key("id").value(rateCode.getId()).matchMode(MatchMode.EXACT).negate(true).build());
        }
        return rateCodeDao.countWithCriteria(conditions);
    }

    public List<RateCode> listBySector(RateCodeSector sector) {
        List<CriteriaCondition> conditions = new ArrayList<>();
        conditions.add(CriteriaCondition.builder().key("sectors.sector").value(sector).matchMode(MatchMode.EXACT).build());

        return rateCodeDao.getWithCriteria(conditions);
    }
}
