package com.inenergis.service;

import com.inenergis.dao.RatePlanDao;
import com.inenergis.dao.RatePlanEnrollmentDao;
import com.inenergis.dao.RatePlanProfileDao;
import com.inenergis.entity.History;
import com.inenergis.entity.program.RatePlan;
import com.inenergis.entity.program.RatePlanEnrollment;
import com.inenergis.entity.program.RatePlanProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RatePlanServiceTest {

    @Mock
    private RatePlanDao ratePlanDao;

    @Mock
    private HistoryService historyService;

    @Mock
    private RatePlanProfileDao ratePlanProfileDao;

    @Mock
    private RatePlanEnrollmentDao ratePlanEnrollmentDao;

    @InjectMocks
    private RatePlanService ratePlanService = new RatePlanService();

    @BeforeEach
    public void inject(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAll() throws Exception {
        ratePlanService.getAll();
        Mockito.verify(ratePlanDao).getAll();
    }

    @Test
    public void testGetById() throws Exception {
        ratePlanService.getById(1L);
        Mockito.verify(ratePlanDao).getById(1L);
    }

    @Test
    public void testSaveOrUpdate() throws Exception {
        RatePlan ratePlan = new RatePlan();
        ratePlanService.saveOrUpdate(ratePlan);
        Mockito.verify(ratePlanDao).saveOrUpdate(ratePlan);
    }

    @Test
    public void testSaveProfile() throws Exception {
        RatePlanProfile profile = new RatePlanProfile();
        ratePlanService.saveOrUpdateProfile(profile,"author");
        Mockito.verify(ratePlanProfileDao).saveOrUpdate(profile);
        Mockito.verify(historyService,Mockito.times(0)).saveHistory((List<History>) Mockito.any());
    }

    @Test
    public void testUpdateProfile() throws Exception {
        RatePlanProfile profile = new RatePlanProfile();
        profile.setId(1L);
        Mockito.when(ratePlanProfileDao.getById(1L)).thenReturn(profile);
        ratePlanService.saveOrUpdateProfile(profile,"author");
        Mockito.verify(ratePlanProfileDao).saveOrUpdate(profile);
        Mockito.verify(historyService).saveHistory(new ArrayList<>());
    }

    @Test
    public void testSAveOrUpdateOneEnrollment() throws Exception {
        RatePlanEnrollment enrollment = new RatePlanEnrollment();
        ratePlanService.saveOrUpdate(enrollment);
        Mockito.verify(ratePlanEnrollmentDao).saveOrUpdate(enrollment);
    }

    @Test
    public void testSAveOrUpdateSeveralEnrollments() throws Exception {
        RatePlanEnrollment enrollment = new RatePlanEnrollment();
        RatePlanEnrollment enrollment2 = new RatePlanEnrollment();
        ratePlanService.saveOrUpdate(Arrays.asList(enrollment,enrollment2));
        Mockito.verify(ratePlanEnrollmentDao).saveOrUpdate(enrollment);
        Mockito.verify(ratePlanEnrollmentDao).saveOrUpdate(enrollment2);
    }

}