package com.inenergis.entity.genericEnum;


import com.inenergis.util.ConstantsProviderModel;

import java.time.LocalTime;

public enum HourOfDayAndHalf {
    HourBid_0(0, 0), HourBid_0_30(0, 30),
    HourBid_1(1, 0), HourBid_1_30(1, 30),
    HourBid_2(2, 0), HourBid_2_30(2, 30),
    HourBid_3(3, 0), HourBid_3_30(3, 30),
    HourBid_4(4, 0), HourBid_4_30(4, 30),
    HourBid_5(5, 0), HourBid_5_30(5, 30),
    HourBid_6(6, 0), HourBid_6_30(6, 30),
    HourBid_7(7, 0), HourBid_7_30(7, 30),
    HourBid_8(8, 0), HourBid_8_30(8, 30),
    HourBid_9(9, 0), HourBid_9_30(9, 30),
    HourBid_10(10, 0), HourBid_10_30(10, 30),
    HourBid_11(11, 0), HourBid_11_30(11, 30),
    HourBid_12(12, 0), HourBid_12_30(12, 30),
    HourBid_13(13, 0), HourBid_13_30(13, 30),
    HourBid_14(14, 0), HourBid_14_30(14, 30),
    HourBid_15(15, 0), HourBid_15_30(15, 30),
    HourBid_16(16, 0), HourBid_16_30(16, 30),
    HourBid_17(17, 0), HourBid_17_30(17, 30),
    HourBid_18(18, 0), HourBid_18_30(18, 30),
    HourBid_19(19, 0), HourBid_19_30(19, 30),
    HourBid_20(20, 0), HourBid_20_30(20, 30),
    HourBid_21(21, 0), HourBid_21_30(21, 30),
    HourBid_22(22, 0), HourBid_22_30(22, 30),
    HourBid_23(23, 0), HourBid_23_30(23, 30);


    private LocalTime hour;

    HourOfDayAndHalf(final Integer hourNumber, final Integer minutesNumber) {
        this.hour = LocalTime.of(hourNumber, minutesNumber);
    }

    public LocalTime getHour() {
        return hour;
    }

    public String getString() {
        return ConstantsProviderModel.HOUR_FORMATTER.format(this.hour);
    }


}
