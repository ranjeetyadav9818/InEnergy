package com.inenergis.service;

import com.inenergis.dao.HistoryDao;
import com.inenergis.dao.RatePlanDao;
import com.inenergis.dao.RatePlanProfileDao;
import com.inenergis.entity.program.RatePlan;
import com.inenergis.entity.program.RatePlanProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class HistoryServiceTest {

    @Mock
    private HistoryDao historyDao;

    @InjectMocks
    private HistoryService historyService = new HistoryService();

    @BeforeEach
    public void inject(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetRatePlanProfile() throws Exception {
        RatePlanProfile profile = new RatePlanProfile();
        historyService.getHistory(profile);
        Mockito.verify(historyDao).getHistory(profile);
    }
}