package com.inenergis.service;

import com.inenergis.dao.ChargesFeeDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class ChargesFeeServiceTest {

    @Mock
    private ChargesFeeDao chargesFeeDao;

    @InjectMocks
    private ChargesFeeService chargesFeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAll() {
        chargesFeeService.getAll();
        Mockito.verify(chargesFeeDao).getAll();
    }

    @Test
    void getAllComparisonEligible() {
        chargesFeeService.getAllComparisonEligible();
        Mockito.verify(chargesFeeDao).getAllComparisonEligible();
    }

    @Test
    void getById() {
        chargesFeeService.getById(1L);
        Mockito.verify(chargesFeeDao).getById(1L);
    }
}