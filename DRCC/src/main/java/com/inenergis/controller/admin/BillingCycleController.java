package com.inenergis.controller.admin;

import com.inenergis.entity.BillingCycleSchedule;
import com.inenergis.service.BillingCycleService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.primefaces.event.RowEditEvent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Named
@ViewScoped
@Transactional
public class BillingCycleController implements Serializable {

    private static final long serialVersionUID = 1L;

    Logger log = LoggerFactory.getLogger(BillingCycleController.class);

    @Inject
    UIMessage uiMessage;

    @Inject
    BillingCycleService billingCycleService;

    private BillingCycleSchedule billingCycle;

    private List<BillingCycleSchedule> billingCycleSchedules;

    @PostConstruct
    public void onCreate() {
        billingCycleSchedules = billingCycleService.getAll();
    }

    public void add() {
        billingCycle = new BillingCycleSchedule();
    }

    public void save() {
        BillingCycleSchedule existingCycle = billingCycleService.getByDate(billingCycle.getDate());
        if (existingCycle != null) {
            uiMessage.addMessage("There is already a serial assigned to that date", FacesMessage.SEVERITY_ERROR);
            return;
        }
        billingCycle.setSerial(billingCycle.getSerial().toUpperCase());
        billingCycleService.saveOrUpdate(billingCycle);
        uiMessage.addMessage("Billing cycle schedule saved");
        onCreate();
        cancel();
    }

    public void cancel() {
        billingCycle = null;
    }

    @Transactional
    public void delete( BillingCycleSchedule billingCycle) {
        billingCycleService.delete(billingCycle);
        onCreate();
    }

    public void onRowEdit(RowEditEvent event) {
        BillingCycleSchedule billingCycleSchedule = (BillingCycleSchedule) event.getObject();
        billingCycleService.saveOrUpdate(billingCycleSchedule);
    }
}