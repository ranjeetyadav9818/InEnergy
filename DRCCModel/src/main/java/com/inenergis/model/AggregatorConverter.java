package com.inenergis.model;

import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.program.ProgramAggregator;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by Antonio on 18/08/2017.
 */
public final class AggregatorConverter {

    private AggregatorConverter(){}

    public static ElasticAggregator convert(ProgramAggregator aggregator){
        ElasticAggregator elasticAggregator = new ElasticAggregator();
        elasticAggregator.setId(aggregator.getId());
        elasticAggregator.setName(aggregator.getName());
        elasticAggregator.setAddress(aggregator.getMailingAddress());
        elasticAggregator.setCity(aggregator.getMailingCity());
        elasticAggregator.setPrimaryPOC(aggregator.getPrimaryPOC());
        elasticAggregator.setPhone(aggregator.getPrimaryPOCPhone());
        elasticAggregator.setState(aggregator.getMailingState().getLabel());
        return elasticAggregator;
    }

}
