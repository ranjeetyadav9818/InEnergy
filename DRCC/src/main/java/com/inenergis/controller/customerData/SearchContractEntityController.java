package com.inenergis.controller.customerData;


import com.inenergis.controller.lazyDataModel.LazyContractEntityModel;
import com.inenergis.entity.contract.ContractEntity;
import com.inenergis.service.ContractEntityService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

@Named
@ViewScoped
@Getter
@Setter
public class SearchContractEntityController implements Serializable{


    @Inject
    private ContractEntityService contractEntityService;

    @Inject
    private EntityManager entityManager;

    @Inject
    UIMessage uiMessage;

    @PostConstruct
    public void init() {
        search();
    }

    private LazyContractEntityModel entities;

    public void search(){
        entities = new LazyContractEntityModel(entityManager, new HashMap<>());
    }

    public void forwardToNewContractEntity() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("NewContractEntity.xhtml");
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void editEntity(ContractEntity entity) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("NewContractEntity.xhtml?o=" + ParameterEncoderService.encode(entity.getId()));
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void viewEntity(ContractEntity entity) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("ContractEntityDetail.xhtml?o=" + ParameterEncoderService.encode(entity.getId()));
        FacesContext.getCurrentInstance().responseComplete();
    }
}
