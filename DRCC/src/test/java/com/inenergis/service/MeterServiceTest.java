package com.inenergis.service;

import com.inenergis.dao.MeterDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;


class MeterServiceTest {

    @Mock
    private MeterDao meterDao;

    @InjectMocks
    private MeterService meterService;

    @BeforeEach
    void inject() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(meterDao.getDistinctMtrConfigTypeValues()).thenReturn(Collections.singletonList("Type II"));
    }

    @Test
    void getDistinctMtrConfigTypeValues() {
        assertEquals(Collections.singletonList("Type II"), meterService.getDistinctMtrConfigTypeValues());
    }
}