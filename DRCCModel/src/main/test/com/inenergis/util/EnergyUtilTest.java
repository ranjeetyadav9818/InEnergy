package com.inenergis.util;

import com.inenergis.entity.genericEnum.ElectricalUnit;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class EnergyUtilTest {

    @Test
    public void parseKwToWatts() {
        assertEquals(12_370, (long) EnergyUtil.parseToWatts("12.37", ElectricalUnit.KW));
    }

    @Test
    public void parseInvalidNumber() {
        assertEquals(0, (long) EnergyUtil.parseToWatts("Invalid Number", ElectricalUnit.MW));
    }

    @Test
    public void convertKwToWatts() {
        assertEquals(1_000, (long) EnergyUtil.convertToWatts(1L, ElectricalUnit.KW));
    }

    @Test
    public void convertZeroKwToWatts() {
        assertEquals(0, (long) EnergyUtil.convertToWatts(0L, ElectricalUnit.KW));
    }

    @Test
    public void convertMwToWatts() {
        assertEquals(5_000_000, (long) EnergyUtil.convertToWatts(5L, ElectricalUnit.MW));
    }

    @Test
    public void convertZeroMwToWatts() {
        assertEquals(0, (long) EnergyUtil.convertToWatts(0L, ElectricalUnit.MW));
    }

    @Test
    public void convertFromNull() {
        assertEquals(null, EnergyUtil.convertToWatts((Long) null, ElectricalUnit.KW));
    }

    @Test
    public void convertWithNullUnit() {
        assertEquals(null, EnergyUtil.convertToWatts(100L, (ElectricalUnit) null));
    }

    @Test
    public void convertDoubleWithNullUnits() {
        assertEquals(null, EnergyUtil.convertToWatts(1d, null));
    }

    @Test
    public void convertWattsTo() {
        assertEquals(BigDecimal.valueOf(1).setScale(2, BigDecimal.ROUND_HALF_UP), EnergyUtil.convertWattsTo(999999L, ElectricalUnit.MW));
    }
}
