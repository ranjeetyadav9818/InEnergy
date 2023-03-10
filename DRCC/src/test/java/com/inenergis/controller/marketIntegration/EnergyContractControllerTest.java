package com.inenergis.controller.marketIntegration;

import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.marketIntegration.EnergyContract;
import com.inenergis.service.ServiceAgreementService;
import com.inenergis.util.UIMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EnergyContractControllerTest {

    @Mock
    private ServiceAgreementService serviceAgreementService;

    @Mock
    private UIMessage uiMessage;

    @InjectMocks
    private EnergyContractController energyContractController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        energyContractController.setEnergyContract(new EnergyContract());
        energyContractController.getEnergyContract().setContractServiceAgreements(new HashSet<>());
        energyContractController.setRawServiceAgreements(" 123, 456 , 789");
    }

    @Test
    void addServiceAgreements_Valid() {
        ServiceAgreement sa1 = new ServiceAgreement();
        sa1.setServiceAgreementId("123");

        ServiceAgreement sa2 = new ServiceAgreement();
        sa2.setServiceAgreementId("123");

        ServiceAgreement sa3 = new ServiceAgreement();
        sa3.setServiceAgreementId("123");

        Mockito.when(serviceAgreementService.getAllById(Mockito.any())).thenReturn(Arrays.asList(sa1, sa2, sa3));

        energyContractController.addServiceAgreements();
        Mockito.verify(serviceAgreementService).getAllById(Arrays.asList("123", "456", "789"));
    }

    @Test
    void addServiceAgreements_InValid() {
        ServiceAgreement sa1 = new ServiceAgreement();
        sa1.setServiceAgreementId("123");

        ServiceAgreement sa2 = new ServiceAgreement();
        sa2.setServiceAgreementId("456");

        Mockito.when(serviceAgreementService.getAllById(Mockito.any())).thenReturn(Arrays.asList(sa1, sa2));

        energyContractController.addServiceAgreements();
        Mockito.verify(serviceAgreementService).getAllById(Arrays.asList("123", "456", "789"));
    }

    @Test
    void removeServiceAgreement() {
        ServiceAgreement sa = new ServiceAgreement();

        energyContractController.getEnergyContract().getContractServiceAgreements().add(sa);
        assertTrue(energyContractController.getEnergyContract().getContractServiceAgreements().contains(sa));

        energyContractController.removeServiceAgreement(sa);
        assertFalse(energyContractController.getEnergyContract().getContractServiceAgreements().contains(sa));
    }
}