package com.inenergis.service;


import com.inenergis.dao.ContractEntityDao;
import com.inenergis.dao.CriteriaCondition;
import com.inenergis.entity.contract.ContractEntity;
import com.inenergis.util.ElasticActionsUtil;
import org.hibernate.criterion.MatchMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContractEntityServiceTest {

    @Mock
    ContractEntityDao contractEntityDao;

    @InjectMocks
    ContractEntityService contractEntityService;

    @Captor
    ArgumentCaptor<List<CriteriaCondition>> conditionsCaptor;

    @BeforeEach
    void inject() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getByBusinessName() {

        final String name = "Business ";
        List<CriteriaCondition> conditions = new ArrayList<>();
        conditions.add(CriteriaCondition.builder().key("businessName").value(name).matchMode(MatchMode.START).build());
        contractEntityService.getByBusinessName(name);
        Mockito.verify(contractEntityDao).getWithCriteria(conditionsCaptor.capture());
        assert (conditionsCaptor.getValue().containsAll(conditions));
    }

    @Test
    void getByDba() {
        String param = "dba";
        List<CriteriaCondition> conditions = new ArrayList<>();
        conditions.add(CriteriaCondition.builder().key("dba").value(param).matchMode(MatchMode.START).build());
        contractEntityService.getByDba(param);
        Mockito.verify(contractEntityDao).getWithCriteria(conditionsCaptor.capture());
        assert (conditionsCaptor.getValue().containsAll(conditions));
    }

    @Test
    void save() throws IOException {
        ContractEntity contractEntity = new ContractEntity();
        ElasticActionsUtil elasticActionsUtil = Mockito.mock(ElasticActionsUtil.class);
        contractEntityService.setElasticActionsUtil(elasticActionsUtil);
        contractEntity.setId(1L);
        Mockito.when(contractEntityDao.saveOrUpdate(Mockito.any())).thenReturn(contractEntity);
        contractEntityService.save(contractEntity);
        Mockito.verify(contractEntityDao).saveOrUpdate(contractEntity);
    }

    @Test
    void getById() {
        contractEntityService.getById(1L);
        Mockito.verify(contractEntityDao).getById(1L);
    }

    @Test
    void getAll() {
        contractEntityService.getAll();
        Mockito.verify(contractEntityDao).getAll();
    }
}
