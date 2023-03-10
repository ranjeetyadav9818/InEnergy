package com.inenergis.model.adapter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by egamas on 25/09/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatePlanAdapter { //RatePlanEnrollment
    private Long id;
    private String name;
    private String type;
    private String sector;
    private String status;
    private Date startDate;

    //active profile
    private String profileName;
    private Date effectiveStartDate;
    private String designType;
    private String tierType;
    private String serviceType;
    private String rateScheduleTitle;
}