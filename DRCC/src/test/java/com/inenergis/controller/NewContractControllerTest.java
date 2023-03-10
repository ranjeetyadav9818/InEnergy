package com.inenergis.controller;

import com.inenergis.controller.customerData.NewContractController;
import com.inenergis.entity.contract.ContractEntity;
import com.inenergis.service.ContractEntityService;
import com.inenergis.util.UIMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NewContractControllerTest {

    @Mock
    private ContractEntityService contractEntityService;

    @Mock
    UIMessage uiMessage;

    @InjectMocks
    private NewContractController newContractController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        newContractController.setEntity(new ContractEntity());
    }

    @Test
    void completeParent() {
        String query = "any";
        newContractController.completeParent(query);
        Mockito.verify(contractEntityService).getByBusinessName(Mockito.any());
        Mockito.verify(contractEntityService).getByDba(Mockito.any());
    }

    @Test
    void saveAndClose() throws IOException {
        newContractController.save();
        Mockito.verify(contractEntityService).save(Mockito.any());
        Mockito.verify(uiMessage).addMessage("Contract Entity saved");
    }


    @Test
    public void addRemoveAddress() {
        newContractController.addAddress();
        assertEquals(1, newContractController.getEntity().getContractAddresses().size());
        newContractController.removeAddress(newContractController.getEntity().getContractAddresses().get(0));
        assertEquals(0, newContractController.getEntity().getContractAddresses().size());
    }

    @Test
    public void addRemovePOC() {
        newContractController.addPOC();
        assertEquals(1, newContractController.getEntity().getPointOfContacts().size());
        newContractController.removePOC(newContractController.getEntity().getPointOfContacts().get(0));
        assertEquals(0, newContractController.getEntity().getPointOfContacts().size());
    }

}
