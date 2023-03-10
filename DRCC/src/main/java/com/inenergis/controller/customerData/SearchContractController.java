package com.inenergis.controller.customerData;


import com.inenergis.controller.lazyDataModel.LazyContractEntityModel;
import com.inenergis.controller.lazyDataModel.LazyEnergyContractModel;
import com.inenergis.entity.contract.ContractEntity;
import com.inenergis.entity.marketIntegration.EnergyContract;
import com.inenergis.service.EnergyContractService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.util.DateRange;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

@Named
@ViewScoped
@Getter
@Setter
public class SearchContractController implements Serializable {


    @Inject
    private EnergyContractService contractService;

    @Inject
    private EntityManager entityManager;

    @Inject
    UIMessage uiMessage;

    @PostConstruct
    public void init() {
        search();
    }

    private LazyEnergyContractModel contracts;
    HashMap<String, Object> preFilters;

    public void search() {
        preFilters = new HashMap<>();
        contracts = new LazyEnergyContractModel(entityManager, preFilters);
    }

    public void forwardToNewContract() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("CreateNewContract.xhtml");
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void onContractSelected(SelectEvent event) throws IOException {
        EnergyContract contract = (EnergyContract) event.getObject();
        FacesContext.getCurrentInstance().getExternalContext().redirect("EnergyContractDetail.xhtml?o=" + ParameterEncoderService.encode(contract.getId()));
        FacesContext.getCurrentInstance().responseComplete();
    }


    //Code needed to filter dates by ranges
    DateRange startDateRange = new DateRange();
    DateRange endDateRange = new DateRange();

    public void filterDates() {
        if(startDateRange != null){
            preFilters.put("agreementStartDate", startDateRange);
        }
        if(endDateRange != null){
            preFilters.put("agreementEndDate", endDateRange);
        }
    }
}
