package com.inenergis.test.controller.service;

import com.inenergis.dao.ProgramDao;
import com.inenergis.dao.ProgramProfileDao;
import com.inenergis.entity.program.EligibleProgram;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramProfile;
import com.inenergis.service.ProgramService;
import com.inenergis.util.ElasticActionsUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProgramServiceTest {

    @Test
    public void testRegister() throws Exception {


        Program program = new Program();
        program.setName("DAO TEST PROGRAM");
        program.setActive(true);
        program.setCapActive(true);
        program.setProfiles(new ArrayList());
        program.setId(1L);

        ProgramProfile programProfile = new ProgramProfile();
        programProfile.setName("DAO TEST PROGRAM PROFILE");
        programProfile.setProgram(program);
        programProfile.setEffectiveStartDate(new Date());
        programProfile.setEffectiveEndDate(new Date());

        EligibleProgram eligibleProgram = new EligibleProgram();
        eligibleProgram.setProgram(program);
        eligibleProgram.setProfile(programProfile);


        List eplist = new ArrayList(1);
        eplist.add(eligibleProgram);
        // programProfile.setEligiblePrograms(eplist);

        ProgramService service = new ProgramService();
        ElasticActionsUtil elasticActionsUtil = Mockito.mock(ElasticActionsUtil.class);
        service.setElasticActionsUtil(elasticActionsUtil);
        ProgramProfileDao profileDao = Mockito.mock(ProgramProfileDao.class);
        ProgramDao programDao = Mockito.mock(ProgramDao.class);
        service.setProgramProfileDao(profileDao);
        service.setProgramDao(programDao);
        Mockito.when(programDao.saveOrUpdate(Mockito.any())).thenReturn(program);
        Mockito.when(profileDao.saveOrUpdate(Mockito.any())).thenReturn(programProfile);
        service.saveProgramAndProfile(program, programProfile, "email");

        Mockito.verify(programDao).saveOrUpdate(program);
        Mockito.verify(profileDao).saveOrUpdate(programProfile);
    }


}
