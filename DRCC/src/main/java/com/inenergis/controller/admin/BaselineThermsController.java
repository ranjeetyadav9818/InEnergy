package com.inenergis.controller.admin;

import com.inenergis.entity.BaselineAllowance;
import com.inenergis.entity.BaselineTherms;
import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;
import com.inenergis.service.BaselineAllowanceService;
import com.inenergis.service.BaselineThermsService;
import com.inenergis.service.TimeOfUseCalendarService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDate;
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
public class BaselineThermsController implements Serializable {

    private static final long serialVersionUID = 1L;

    Logger log = LoggerFactory.getLogger(BaselineThermsController.class);

    @Inject
    UIMessage uiMessage;

//    @Inject
//    BaselineAllowanceService baselineAllowanceService;

    @Inject
    BaselineThermsService baselineThermsService;

    @Inject
    TimeOfUseCalendarService timeOfUseCalendarService;

    private boolean isNewBaselineTherms = false;

//    private BaselineAllowance baselineAllowance;
    private BaselineTherms baselineTherms;


//    private BaselineAllowance selectedBaselineAllowance;
    private BaselineTherms selectedTherms;


//    private List<BaselineAllowance> baselineAllowanceList;
    private List<BaselineTherms> baselineThermsList;

//    private List<TimeOfUseCalendar> timeOfUseCalendars = new ArrayList<>();
    private List<BaselineTherms> localDate = new ArrayList<>();


    @PostConstruct
    public void onCreate() {
        localDate = baselineThermsService.getAll();
        baselineThermsList = baselineThermsService.getAll();
    }

    public void add() {
        baselineTherms = new BaselineTherms();
        isNewBaselineTherms = true;
    }

    public void save() {
        baselineThermsService.saveOrUpdate(baselineTherms);
        uiMessage.addMessage("Baseline Allowance saved");
        baselineTherms = null;

        isNewBaselineTherms = false;

        this.onCreate();
    }

    public void cancelThems() {
        this.baselineTherms = null;
        isNewBaselineTherms = false;
    }

    @Transactional
    public void deleteTherms() {
        baselineThermsList.remove(selectedTherms);
//        baselineThermsService.delete(selectedTherms);
    }

    public void onRowEdit(RowEditEvent event) {
        BaselineTherms baselineTherms = (BaselineTherms) event.getObject();
        baselineThermsService.saveOrUpdate(baselineTherms);
    }
}