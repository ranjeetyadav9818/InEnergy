package com.inenergis.controller.program;


import com.inenergis.entity.program.rateProgram.RateCode;
import com.inenergis.service.RateCodeService;
import com.inenergis.service.RateCodeServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RateCodelibraryControllerTest {

    @Mock
    private RateCodeService rateCodeService;

    @InjectMocks
    private RateCodeLibraryController controller = new RateCodeLibraryController();

    @BeforeEach
    public void inject(){
        MockitoAnnotations.initMocks(this);
        final RateCode rateCode = RateCodeServiceTest.buildRateCode();
        controller.setRateCode(rateCode);
    }

    @Test
    public void init(){
        controller.init();
        assertNotNull(controller.getLazyRateCodeModel());
        assertNotNull(controller.getSectorPickList());
        assertNotNull(controller.getSectorList());
    }

    @Test
    public void addCode(){
        controller.addCode();
        assertNotNull(controller.getRateCode());
        assertNotNull(controller.getSectorPickList());
        assertNotNull(controller.getSectorList());
        assertTrue(controller.getSectorPickList().getTarget().isEmpty());
    }

    @Test
    public void cancel(){
        controller.cancel();
        assertNull(controller.getRateCode());
        assertFalse(controller.isRenderNewCode());
    }


}
