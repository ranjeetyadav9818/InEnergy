package com.inenergis.controller.converter;

import com.inenergis.entity.program.SeasonCalendar;
import com.inenergis.service.RateSeasonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RateSeasonConverterTest {

    @InjectMocks
    private RateSeasonConverter converter;

    @Mock
    private RateSeasonService rateSeasonService;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAsObjectUsesInjectedService() {
        SeasonCalendar seasonCalendar = new SeasonCalendar();
        Mockito.when(rateSeasonService.getById(1L)).thenReturn(seasonCalendar);
        Object object = converter.getAsObject(null, null, "1");
        assertEquals(object,seasonCalendar);
    }

    @Test
    void getAsStringReturnsId() {
        SeasonCalendar seasonCalendar = new SeasonCalendar();
        seasonCalendar.setId(2L);
        String string = converter.getAsString(null, null, seasonCalendar);
        assertEquals("2",string);
    }
}