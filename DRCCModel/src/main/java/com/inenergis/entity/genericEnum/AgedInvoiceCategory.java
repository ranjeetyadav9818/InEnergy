package com.inenergis.entity.genericEnum;

import lombok.Getter;

import java.time.temporal.ValueRange;

@Getter
public enum AgedInvoiceCategory {
    CURRENT(Integer.MIN_VALUE, 0),
    FROM_1_TO_30_DAYS(1, 30),
    FROM_31_TO_60_DAYS(31, 60),
    FROM_61_TO_90_DAYS(61, 90),
    VERY_OLD(91, Integer.MAX_VALUE);

    private ValueRange range;

    AgedInvoiceCategory(int from, int to) {
        range = ValueRange.of(from, to);
    }

    public String getName() {
        if (Integer.MIN_VALUE == range.getMinimum()) {
            return "Current";
        }
        if (Integer.MAX_VALUE == range.getMaximum()) {
            return "90+ days";
        }

        return range.getMinimum() + " - " + range.getMaximum() + " days";
    }

    public static AgedInvoiceCategory getByOverdueDays(long value) {
        for (AgedInvoiceCategory agedInvoiceCategory : AgedInvoiceCategory.values()) {
            if (agedInvoiceCategory.range.isValidValue(value)) {
                return agedInvoiceCategory;
            }
        }

        return VERY_OLD;
    }
}