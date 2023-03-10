package com.inenergis.controller.admin;

import com.inenergis.entity.BaselineAllowance;
import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;
import com.inenergis.service.BaselineAllowanceService;
import com.inenergis.service.TimeOfUseCalendarService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.primefaces.event.RowEditEvent;

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
public class BaselineAllowanceController implements Serializable {

    private static final long serialVersionUID = 1L;

    Logger log = LoggerFactory.getLogger(BaselineAllowanceController.class);

    @Inject
    UIMessage uiMessage;

    @Inject
    BaselineAllowanceService baselineAllowanceService;

    @Inject
    TimeOfUseCalendarService timeOfUseCalendarService;

    private boolean isNewBaselineAllowance = false;

    private BaselineAllowance baselineAllowance;

    private BaselineAllowance selectedBaselineAllowance;

    private List<BaselineAllowance> baselineAllowanceList;

    private List<TimeOfUseCalendar> timeOfUseCalendars = new ArrayList<>();

    @PostConstruct
    public void onCreate() {
        timeOfUseCalendars = timeOfUseCalendarService.getAll();
        baselineAllowanceList = baselineAllowanceService.getAll();
    }

    public void add() {
        baselineAllowance = new BaselineAllowance();
        isNewBaselineAllowance = true;
    }

    public void save() {
        baselineAllowanceService.saveOrUpdate(baselineAllowance);
        uiMessage.addMessage("Baseline Allowance saved");
        baselineAllowance = null;

        isNewBaselineAllowance = false;

        this.onCreate();
    }

    public void cancelBaselineAllowance() {
        this.baselineAllowance = null;
        isNewBaselineAllowance = false;
    }

    @Transactional
    public void deleteBaselineAllowance() {
        baselineAllowanceList.remove(selectedBaselineAllowance);
        baselineAllowanceService.delete(selectedBaselineAllowance);
    }

    public void onRowEdit(RowEditEvent event) {
        BaselineAllowance baselineAllowance = (BaselineAllowance) event.getObject();
        baselineAllowanceService.saveOrUpdate(baselineAllowance);
    }
}