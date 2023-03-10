package com.inenergis.service;

import com.inenergis.dao.RateSeasonDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class RateSeasonServiceTest {

    @Mock
    private RateSeasonDao rateSeasonDao;

    @InjectMocks
    private RateSeasonService rateSeasonService = new RateSeasonService();

    @BeforeEach
    public void inject(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetById() throws Exception {
        rateSeasonService.getById(1L);
        Mockito.verify(rateSeasonDao).getById(1L);
    }
}