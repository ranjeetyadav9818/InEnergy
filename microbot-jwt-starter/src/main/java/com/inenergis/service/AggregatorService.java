package com.inenergis.service;

import com.inenergis.dao.AggregatorDao;
import com.inenergis.entity.program.ProgramAggregator;
import com.inenergis.model.adapter.AggregatorAdapter;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by egamas on 04/10/2017.
 */
@Getter
@Setter
@Component
public class AggregatorService {

    private static final Logger log = LoggerFactory.getLogger(AggregatorService.class);

    @Autowired
    private AggregatorDao aggregatorDao;


    @Transactional("mysqlTransactionManager")
    public List<AggregatorAdapter> getAll(){
        final List<ProgramAggregator> all = aggregatorDao.findAll();
        if (CollectionUtils.isNotEmpty(all)) {
            return all.stream().map(a -> AggregatorAdapter.builder().name(a.getName()).id(a.getId()).build()).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
    
    @Transactional("mysqlTransactionManager")
    public ProgramAggregator getById(long id) {
        return aggregatorDao.getById(id);
    }
}
