package com.inenergis.service;

import com.inenergis.dao.CriteriaCondition;
import com.inenergis.dao.ProgramAggregatorDao;
import com.inenergis.entity.program.ProgramAggregator;
import com.inenergis.model.AggregatorConverter;
import com.inenergis.util.ElasticActionsUtil;
import com.inenergis.util.ElasticConnectionPool;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.inenergis.model.ElasticAggregator.ELASTIC_TYPE;
import static com.inenergis.util.ElasticActionsUtil.ENERGY_ARRAY_INDEX;

@Stateless
public class ProgramAggregatorService {

    private static final Logger log = LoggerFactory.getLogger(ProgramAggregatorService.class);

    @Inject
    ProgramAggregatorDao aggregatorDao;

    @Inject
    ElasticConnectionPool elasticConnectionPool;

    @Inject
    ElasticActionsUtil elasticActionsUtil;

    @Transactional
    public void saveAggregator(ProgramAggregator aggregator) throws Exception {
        aggregator = aggregatorDao.saveOrUpdate(aggregator);
        elasticActionsUtil.indexDocument(aggregator.getId().toString(), AggregatorConverter.convert(aggregator), ENERGY_ARRAY_INDEX, ELASTIC_TYPE);
    }


    public List<ProgramAggregator> getAggregators() {
        return aggregatorDao.getAll();
    }

    public List<ProgramAggregator> getAllActive() {
        return aggregatorDao.getAllActive();
    }

    public List<ProgramAggregator> getAggregators(String name, String pOCName, String pOCPhone) {
        List<CriteriaCondition> conditions = new ArrayList<>();
        if (StringUtils.isNotBlank(name)) {
            conditions.add(CriteriaCondition.builder().key("name").value(name).matchMode(MatchMode.START).build());
        }
        if (StringUtils.isNotBlank(pOCName)) {
            conditions.add(CriteriaCondition.builder().key("primaryPOC").value(pOCName).matchMode(MatchMode.START).build());
        }
        if (StringUtils.isNotBlank(pOCPhone)) {
            conditions.add(CriteriaCondition.builder().key("primaryPOCPhone").value(pOCPhone).matchMode(MatchMode.START).build());
        }
        return aggregatorDao.getWithCriteria(conditions);
    }

    public ProgramAggregator getAggregatorById(Long id) {
        return aggregatorDao.getById(id);
    }
}