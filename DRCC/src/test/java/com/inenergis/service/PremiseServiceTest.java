package com.inenergis.service;

import com.inenergis.dao.PremiseDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PremiseServiceTest {

    @Mock
    private PremiseDao premiseDao;

    @InjectMocks
    private PremiseService premiseService = new PremiseService();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(premiseDao.getDistinctPremiseTypeValues()).thenReturn(Collections.singletonList("Premise Type II"));
    }

    @Test
    void getDistinctPremiseTypeValues() {
        assertEquals(Collections.singletonList("Premise Type II"), premiseService.getDistinctPremiseTypeValues());
    }
}