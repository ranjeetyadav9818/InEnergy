package com.inenergis.model.adapter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgreementPointMapAdapter { //AgreementPointMap
    //id is said + sp id
    //service Point
    private Date endDate;
    private Date startDate;
    private String servicePointId;
    private String customerMdmaCompanyName;
    private String customerMspCompanyName;
    private String serviceType;
    private String latitude;
    private String longitude;

    //premise
    private String premiseId;
    private String serviceAddress1;
    private String serviceAddress2;
    private String serviceCityUpr;
    private String servicePostal;
    private String serviceState;
    private String baseLineChar;
    private String county;
    private String premiseType;

    //meter
    private String meterId;
    private String badgeNumber;
    private String smStatus;
    private Date installDate;
    private Date uninstallDate;
    private String readFreq;

}