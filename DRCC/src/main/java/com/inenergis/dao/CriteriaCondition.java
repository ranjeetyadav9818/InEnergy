package com.inenergis.dao;

import lombok.Builder;
import lombok.Data;
import org.hibernate.criterion.MatchMode;

@Data
@Builder
public class CriteriaCondition {

    private String key;
    private Object value;
    private MatchMode matchMode;
    private NoComparisonCheck noComparisonCheck;
    private boolean negate;
}
