package com.inenergis.service;


import com.inenergis.dao.RateCodeProfileDao;
import com.inenergis.entity.program.rateProgram.RateCodeProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class RateCodeProfileServiceTest {

    @Mock
    RateCodeProfileDao rateCodeProfileDao;

    @InjectMocks
    private RateCodeProfileService rateCodeProfileService = new RateCodeProfileService();

    @BeforeEach
    public void inject(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void update(){
        RateCodeProfile rateCodeProfile = new RateCodeProfile();
        rateCodeProfileService.update(rateCodeProfile);
        Mockito.verify(rateCodeProfileDao).saveOrUpdate(rateCodeProfile);
    }

}
