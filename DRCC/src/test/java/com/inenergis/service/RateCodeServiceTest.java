package com.inenergis.service;


import com.inenergis.dao.CriteriaCondition;
import com.inenergis.dao.RateCodeDao;
import com.inenergis.entity.genericEnum.ActivityStatus;
import com.inenergis.entity.genericEnum.RateCodeSector;
import com.inenergis.entity.program.rateProgram.RateCode;
import com.inenergis.entity.program.rateProgram.RateCodeSectors;
import org.hibernate.criterion.MatchMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RateCodeServiceTest {

    @Mock
    RateCodeDao rateCodeDao;

    @Captor
    ArgumentCaptor<List<CriteriaCondition>> conditionsCaptor;

    @InjectMocks
    RateCodeService rateCodeService;

    @BeforeEach
    public void inject() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void save() {
        RateCode rateCode = new RateCode();
        rateCodeService.save(rateCode);
        Mockito.verify(rateCodeDao).save(rateCode);
    }

    @Test
    public void update() {
        RateCode rateCode = new RateCode();
        rateCodeService.update(rateCode);
        Mockito.verify(rateCodeDao).saveOrUpdate(rateCode);
    }

    @Test
    public void searchOthersWithSameCodeAndSector() {
        List<RateCodeSectors> sectors = getRateCodeSectors();
        List<CriteriaCondition> conditions = new ArrayList<>();
        final CriteriaCondition condition1 = CriteriaCondition.builder().key("name").value("name").matchMode(MatchMode.EXACT).build();
        conditions.add(condition1);
        final List<RateCodeSector> rateCodeSectorList = sectors.stream().map(s -> s.getSector()).collect(Collectors.toList());
        final CriteriaCondition condition2 = CriteriaCondition.builder().key("sectors.sector").value(rateCodeSectorList).matchMode(MatchMode.EXACT).build();
        conditions.add(condition2);
        RateCode rateCode =  new RateCode();
        rateCode.setName("name");
        rateCode.setSectors(sectors);
        rateCodeService.searchOthersWithSameCodeAndSector(rateCode);
        Mockito.verify(rateCodeDao).countWithCriteria(conditionsCaptor.capture());
        for (CriteriaCondition criteriaCondition : conditionsCaptor.getValue()) {
            Assertions.assertTrue(criteriaCondition.equals(condition1) || criteriaCondition.equals(condition2));
        }
    }

    @Test
    public void listBySector() {
        RateCodeSector sector = RateCodeSector.AGRICULTURAL;
        final CriteriaCondition condition1 = CriteriaCondition.builder().key("sectors.sector").value(sector).matchMode(MatchMode.EXACT).build();
        rateCodeService.listBySector(sector);
        Mockito.verify(rateCodeDao).getWithCriteria(conditionsCaptor.capture());
        for (CriteriaCondition criteriaCondition : conditionsCaptor.getValue()) {
            Assertions.assertTrue(criteriaCondition.equals(condition1));
        }
    }

    private List<RateCodeSectors> getRateCodeSectors() {
        List<RateCodeSectors> sectors = new ArrayList<>();
        RateCode rateCode = new RateCode();
        rateCode.setName("R1");
        sectors.add(new RateCodeSectors());
        sectors.get(0).setRateCode(rateCode);
        sectors.get(0).setSector(RateCodeSector.AGRICULTURAL);
        return sectors;
    }

    public static RateCode buildRateCode() {
        RateCode rateCode = new RateCode();
        rateCode.setName("Test Rate Code");
        rateCode.setRateStatus(ActivityStatus.ACTIVE);
        rateCode.setSectors(Arrays.stream(RateCodeSector.values()).map(r -> {
            RateCodeSectors sector = new RateCodeSectors();
            sector.setSector(r);
            sector.setRateCode(rateCode);
            return sector;
        }).collect(Collectors.toList()));
        return rateCode;
    }
}
