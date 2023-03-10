package com.inenergis.controller.converter;


import com.inenergis.entity.contract.ContractEntity;
import com.inenergis.service.ContractEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContractEntityConverterTest {

    @Mock
    FacesContext facesContext;

    @Mock
    UIComponent uiComponent;

    @Mock
    ContractEntityService contractEntityService;

    @InjectMocks
    ContractEntityConverter contractEntityConverter;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAsObjectUsesInjectedService() {
        ContractEntity contractEntity = buildContractEntity();
        Mockito.when(contractEntityService.getByBusinessName(Mockito.any())).thenReturn(Arrays.asList(contractEntity));
        Mockito.when(contractEntityService.getByDba(Mockito.any())).thenReturn(Arrays.asList(contractEntity));
        Object o = contractEntityConverter.getAsObject(facesContext, uiComponent, "Business");
        assertEquals(o, contractEntity);
    }
    @Test
    void getAsStringReturnsBusinessDBA() {
        ContractEntity entity = buildContractEntity();
        String s = contractEntityConverter.getAsString(facesContext,uiComponent,entity);
        assertEquals(s, entity.getBusinessName()+" ( "+entity.getDba()+" )");
    }

    private ContractEntity buildContractEntity() {
        final ContractEntity contractEntity = new ContractEntity();
        contractEntity.setBusinessName("Business");
        contractEntity.setDba("What we are doing");
        contractEntity.setId(1L);
        return contractEntity;
    }

}
