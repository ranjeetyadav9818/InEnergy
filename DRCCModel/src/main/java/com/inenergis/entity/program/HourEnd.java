package com.inenergis.entity.program;

public enum HourEnd {

    HE_1 (1),
    HE_2 (2),
    HE_3 (3),
    HE_4 (4),
    HE_5 (5),
    HE_6 (6),
    HE_7 (7),
    HE_8 (8),
    HE_9 (9),
    HE_10 (10),
    HE_11 (11),
    HE_12 (12),
    HE_13 (13),
    HE_14 (14),
    HE_15 (15),
    HE_16 (16),
    HE_17 (17),
    HE_18 (18),
    HE_19 (19),
    HE_20 (20),
    HE_21 (21),
    HE_22 (22),
    HE_23 (23),
    HE_24 (24);


    private final Integer hourNumber;

    HourEnd(final Integer hourNumber) {
        this.hourNumber = hourNumber;
    }

    public Integer getHourNumber() {
        return hourNumber;
    }
}
