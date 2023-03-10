package com.inenergis.service;

import com.inenergis.entity.genericEnum.SeasonTOU;
import com.inenergis.entity.genericEnum.TimeOfUseDayType;
import com.inenergis.entity.masterCalendar.TimeOfUse;
import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;
import com.inenergis.entity.masterCalendar.TimeOfUseDay;
import com.inenergis.entity.masterCalendar.TimeOfUseHour;
import com.inenergis.billingEngine.service.billing.AncillaryCalculationsService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;



/**
 * Created by egamas on 05/12/2017.
 */
public class AncillaryCalculationServiceTest {

    @Mock
    private TimeOfUse timeOfUse;

    @Mock
    private TimeOfUse timeOfUseWeekDays;

    @Mock
    private TimeOfUseCalendar timeOfUseCalendar;

    @InjectMocks
    private AncillaryCalculationsService ancillaryCalculationsService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
        Mockito.when(timeOfUseCalendar.getName()).thenReturn("Winter");
        Mockito.when(timeOfUseCalendar.getDateFrom()).thenReturn(LocalDate.of(2016,1,1));
        Mockito.when(timeOfUseCalendar.getDateTo()).thenReturn(LocalDate.of(2016,2,1));
        Mockito.when(timeOfUse.getTou()).thenReturn(SeasonTOU.OFF_PEAK);
        Mockito.when(timeOfUse.getTimeOfUseCalendar()).thenReturn(timeOfUseCalendar);
        Mockito.when(timeOfUse.getTimeOfUseHours()).thenReturn(Collections.singletonList(new TimeOfUseHour(9, timeOfUse)));
        Mockito.when(timeOfUse.getTimeOfUseDays()).thenReturn(Collections.singletonList(new TimeOfUseDay(TimeOfUseDayType.WEEK_ENDS, timeOfUse)));
        Mockito.when(timeOfUseWeekDays.getTimeOfUseDays()).thenReturn(Collections.singletonList(new TimeOfUseDay(TimeOfUseDayType.WEEK_DAYS, timeOfUseWeekDays)));
        Mockito.when(timeOfUseCalendar.getTimeOfUses()).thenReturn(Collections.singletonList(timeOfUse));
    }

    @Test
    void valuesIsNotInCalendarOrTOU(){
        Pair<LocalDateTime, BigDecimal> timeAndDemandValue = new ImmutablePair<>(LocalDateTime.of(2017,8,12,8,0),BigDecimal.TEN);
        //12 8 2017 saturday
        assertTrue(ancillaryCalculationsService.valueIsNotInCalendarOrTOU(timeAndDemandValue,timeOfUseCalendar,null));
        assertTrue(ancillaryCalculationsService.valueIsNotInCalendarOrTOU(timeAndDemandValue,timeOfUseCalendar,timeOfUse));
        assertFalse(ancillaryCalculationsService.valueIsNotInCalendarOrTOU(timeAndDemandValue,null,timeOfUse));
        assertTrue(ancillaryCalculationsService.valueIsNotInCalendarOrTOU(timeAndDemandValue,null,timeOfUseWeekDays));
        Pair<LocalDateTime, BigDecimal> timeAndDemandValueOut = new ImmutablePair<>(LocalDateTime.of(2017,8,12,7,59),BigDecimal.TEN);
        assertTrue(ancillaryCalculationsService.valueIsNotInCalendarOrTOU(timeAndDemandValueOut,null,timeOfUse));
    }
}
