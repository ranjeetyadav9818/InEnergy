package com.inenergis.controller.award;


import lombok.Data;

import java.util.List;

@Data
public class ScheduleRow {
    private String scheduleType;
    private List<Long> values;
    private boolean price;
}


