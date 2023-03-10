package com.inenergis.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class DateRange implements Serializable {

    private Date from;
    private Date to;
    private boolean localDateCompare; //If the columns are defined as java8 LocalDate at entity level

    public DateRange(boolean localDateCompare) {
        this.localDateCompare = localDateCompare;
    }

    public void reset() {
        this.from = null;
        this.to = null;
    }
}