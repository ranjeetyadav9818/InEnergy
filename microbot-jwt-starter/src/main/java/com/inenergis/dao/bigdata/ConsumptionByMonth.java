package com.inenergis.dao.bigdata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by Antonio on 27/09/2017.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConsumptionByMonth {

    private BigDecimal value;
    private String month;
}
