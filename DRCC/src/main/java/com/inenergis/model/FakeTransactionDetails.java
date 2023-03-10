package com.inenergis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class FakeTransactionDetails {

    private String date;
    private String seller;
    private String buyer;
    private String quantity;
    private String price;
    private String consumer;
    private String producer;
    private String actualProduction;
    private String lastMeasuredDate;
    private String status;
    private String offerQuantity;
    private String realQuantity;
    private String total;

}
