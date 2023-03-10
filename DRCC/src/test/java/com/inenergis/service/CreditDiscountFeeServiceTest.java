package com.inenergis.service;

import com.inenergis.dao.CreditDiscountFeeDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class CreditDiscountFeeServiceTest {

    @Mock
    private CreditDiscountFeeDao creditDiscountFeeDao;

    @InjectMocks
    private CreditDiscountFeeService creditDiscountFeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAll() {
        creditDiscountFeeService.getAll();
        Mockito.verify(creditDiscountFeeDao).getAll();
    }

    @Test
    void getAllComparisonEligible() {
        creditDiscountFeeService.getAllComparisonEligible();
        Mockito.verify(creditDiscountFeeDao).getAllComparisonEligible();
    }

    @Test
    void getById() {
        creditDiscountFeeService.getById(2L);
        Mockito.verify(creditDiscountFeeDao).getById(2L);
    }
}