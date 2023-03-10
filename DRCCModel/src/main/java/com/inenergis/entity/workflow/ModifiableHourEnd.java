package com.inenergis.entity.workflow;

import com.inenergis.entity.HourEndObject;

import java.util.Date;

/**
 * Created by egamas on 07/04/2017.
 */
public interface ModifiableHourEnd extends HourEndObject{
    void setHour(int hour, Long  value) ;
    void setUiName(String uiName);
    Date getMeasureDate();
}
