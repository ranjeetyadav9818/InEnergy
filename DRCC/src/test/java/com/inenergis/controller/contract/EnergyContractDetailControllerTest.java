package com.inenergis.controller.contract;

import com.inenergis.controller.carousel.ContractEntityCarousel;
import com.inenergis.controller.customerData.EnergyContractDetailController;
import com.inenergis.controller.general.DocumentHelper;
import com.inenergis.controller.marketIntegration.EnergyContractController;
import com.inenergis.entity.contract.ContractDevice;
import com.inenergis.entity.contract.ContractEntity;
import com.inenergis.entity.contract.Device;
import com.inenergis.entity.marketIntegration.EnergyContract;
import com.inenergis.entity.marketIntegration.Party;
import com.inenergis.service.DocumentService;
import com.inenergis.service.EnergyContractService;
import com.inenergis.service.ExternalFileRepositoryService;
import com.inenergis.util.UIMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.picketlink.Identity;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class EnergyContractDetailControllerTest {

    @Mock
    private EnergyContractService energyContractService;
    @Mock
    private DocumentService documentService;
    @Mock
    private Identity identity;
    @Mock
    private ExternalFileRepositoryService fileRepositoryService;
    @Mock
    private ContractEntityCarousel entityCarousel;
    @Mock
    private EnergyContractController energyContractController;
    @Mock
    private DocumentHelper documentHelper;
    @Mock
    private EntityManager entityManager;
    @Mock
    UIMessage uiMessage;
    @InjectMocks
    private EnergyContractDetailController controller = new EnergyContractDetailController();

    @BeforeEach
    void inject() {
        MockitoAnnotations.initMocks(this);
        init();
    }

    @Test
    void init() {
        Mockito.when(energyContractService.getById(Mockito.any())).thenReturn(getEnergyContract());
        controller.doInit(Mockito.any());
    }


    @Test
    void addContractDevices() {
        controller.addContractDevices();
        Assertions.assertTrue(controller.isShowAddLocationTab());
        Assertions.assertNotNull(controller.getContractDevicesToAdd());
        Assertions.assertFalse(controller.getContractDevicesToAdd().isEmpty());
    }

    @Test
    void addContractDevice() {
        addContractDevices();
        final List<ContractDevice> contractDevicesToAdd = controller.getContractDevicesToAdd();
        final ContractDevice contractDevice = contractDevicesToAdd.get(0);
        controller.addContractDevice(contractDevice);
        Assertions.assertTrue(controller.getContract().getContractDevices().contains(contractDevice));
        Assertions.assertFalse(contractDevicesToAdd.contains(contractDevice));
    }

    @Test
    void removeContractDevice() {
        addContractDevices();
        addContractDevice();
        final ContractDevice contractDeviceToRemove = controller.getContract().getContractDevices().get(0);
        controller.removeContractDevice(contractDeviceToRemove);
        Assertions.assertFalse(controller.getContract().getContractDevices().contains(contractDeviceToRemove));
        Assertions.assertTrue(controller.getContractDevicesToAdd().contains(contractDeviceToRemove));
    }

    @Test
    void saveContractDevices() throws IOException {
        Mockito.when(energyContractService.saveOrUpdate(controller.getContract())).thenReturn(controller.getContract());
        controller.saveContractDevices();
        Mockito.verify(energyContractService).saveOrUpdate(controller.getContract());
        Mockito.verify(uiMessage).addMessage(EnergyContractController.ENERGY_CONTRACT_SAVED);
        Assertions.assertFalse(controller.isShowAddLocationTab());
    }


    private EnergyContract getEnergyContract() {
        final EnergyContract energyContract = new EnergyContract();
        energyContract.setParties(new ArrayList<>());

        energyContract.setBillMonths(new ArrayList<>());
        energyContract.setParties(new ArrayList<>());
        energyContract.setFees(new ArrayList<>());
        energyContract.setCredits(new ArrayList<>());
        energyContract.setCommodities(new ArrayList<>());
        energyContract.setRelatedContracts(new ArrayList<>());
        energyContract.setContractDevices(new ArrayList<>());
        ContractEntity entity = new ContractEntity();
        entity.setBusinessName("Entity1 name");
        Device dev1 =  new Device(entity);
        entity.setDevices(new ArrayList<>());
        entity.getDevices().add(dev1);

        ContractEntity entity2 = new ContractEntity();
        entity.setBusinessName("Entity2 name");
        Device dev2 =  new Device(entity2);
        entity2.setDevices(new ArrayList<>());
        entity2.getDevices().add(dev2);
        final Party party1 = new Party();
        party1.setEnergyContract(energyContract);
        party1.setEntity(entity);
        energyContract.getParties().add(party1);

        final Party party2 = new Party();
        party2.setEnergyContract(energyContract);
        party2.setEntity(entity2);
        energyContract.getParties().add(party2);

        energyContract.setRatePlans(new ArrayList<>());
        energyContract.setSubLaps(new ArrayList<>());
        energyContract.setSubstations(new ArrayList<>());
        energyContract.setFeeders(new ArrayList<>());

        return energyContract;
    }


}