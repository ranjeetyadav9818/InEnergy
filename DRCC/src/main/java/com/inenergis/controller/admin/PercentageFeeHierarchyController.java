package com.inenergis.controller.admin;

import com.inenergis.entity.program.rateProgram.PercentageFeeHierarchyEntry;
import com.inenergis.service.PercentageFeeHierarchyService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.RowEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Named
@ViewScoped
@Transactional
public class PercentageFeeHierarchyController implements Serializable {

    Logger log = LoggerFactory.getLogger(PercentageFeeHierarchyController.class);

    @Inject
    UIMessage uiMessage;

    @Inject
    PercentageFeeHierarchyService percentageFeeHierarchyService;

    private List<PercentageFeeHierarchyEntry> fees = new ArrayList<>();
    private boolean creatingFee = false;
    private PercentageFeeHierarchyEntry fee;

    @PostConstruct
    public void onCreate() {
        fees = percentageFeeHierarchyService.getAll();
    }

    public void add(){
        creatingFee = true;
        fee = new PercentageFeeHierarchyEntry();
    }

    public void save(){
        percentageFeeHierarchyService.saveOrUpdate(fee);
        cancel();
        onCreate();
    }

    public void cancel(){
        creatingFee = false;
        fee = null;
    }

    public void onRowEdit(RowEditEvent event){
        PercentageFeeHierarchyEntry fee = (PercentageFeeHierarchyEntry) event.getObject();
        percentageFeeHierarchyService.saveOrUpdate(fee);
    }
}