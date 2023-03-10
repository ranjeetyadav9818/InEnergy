package com.inenergis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class FakeTransaction {

    private String transactionId;
    private Date date;
    private String value;
    private String status;
}
