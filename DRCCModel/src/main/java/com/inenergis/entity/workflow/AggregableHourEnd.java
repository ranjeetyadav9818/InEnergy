package com.inenergis.entity.workflow;

import com.inenergis.entity.HourEndObject;

public interface AggregableHourEnd {

    void add(HourEndObject heObject);

    void substract(HourEndObject heObject);

    void substractToAllHourEnds(long value);

}
