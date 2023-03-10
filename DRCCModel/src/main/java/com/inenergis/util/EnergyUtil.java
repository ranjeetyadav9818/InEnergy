package com.inenergis.util;

import com.inenergis.entity.genericEnum.ElectricalUnit;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class EnergyUtil {
    public static Long convertToWatts(Long value, ElectricalUnit unit) {
        if (value == null || unit == null) {
            return null;
        }

        return convert(value.doubleValue(), unit);
    }

    public static Long convertToWatts(Double value, ElectricalUnit unit) {
        if (value == null || unit == null) {
            return null;
        }

        return convert(value, unit);
    }

    public static Long convertToWatts(BigDecimal value, ElectricalUnit unit) {
        if (value == null || unit == null) {
            return null;
        }

        return convert(value.doubleValue(), unit);
    }

    public static Long parseToWatts(String s, ElectricalUnit unit) {
        try {
            return convertToWatts(Double.parseDouble(s), unit);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    public static BigDecimal convertWattsTo(Long value, ElectricalUnit unit) {
        BigDecimal result;

        switch (unit) {
            case KW:
                result = BigDecimal.valueOf(value).divide(BigDecimal.valueOf(1_000), 2, RoundingMode.HALF_UP);
                break;
            case MW:
                result = BigDecimal.valueOf(value).divide(BigDecimal.valueOf(1_000_000), 2, RoundingMode.HALF_UP);
                break;
            default:
                result = new BigDecimal(value);
        }

        return result;
    }

    private static Long convert(Double value, ElectricalUnit unit) {
        Double result;
        switch (unit) {
            case KW:
                result = value * 1_000;
                break;
            case MW:
                result = value * 1_000_000;
                break;
            default:
                result = value;
        }

        return Math.round(result);
    }
}
