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
public class EnergyContractAdapter {
    private Long id;
    private String name;
    private String type;
    private Date lastUpdate;
    private Date agreementStartDate;
    private Date agreementEndDate;
    private String paymentFrequency;
    private String status;

}