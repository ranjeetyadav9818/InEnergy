package com.inenergis.controller.admin;

import com.inenergis.entity.SubLap;
import com.inenergis.entity.marketIntegration.Iso;
import com.inenergis.service.IsoService;
import com.inenergis.service.SubLapService;
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
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Named
@ViewScoped
public class SublapsController implements Serializable {

    private static final long serialVersionUID = 1L;

    private Logger log = LoggerFactory.getLogger(SublapsController.class);

    @Inject
    private UIMessage uiMessage;

    @Inject
    private IsoService isoService;

    @Inject
    private SubLapService subLapService;

    private SubLap sublap = null;
    private SubLap selectedSublap;
    private List<SubLap> sublapList = null;
    private boolean creatingSublap = false;
    private List<Iso> isoList;

    @PostConstruct
    public void onCreate() {
        sublapList = subLapService.getAll();
        isoList = isoService.getIsos();
    }

    public void add() {
        sublap = new SubLap();
        creatingSublap = true;
    }

    public void save() {
        subLapService.saveOrUpdate(sublap);
        uiMessage.addMessage("{0} saved", sublap.getName());
        cancel();
        onCreate();
    }

    public void cancel() {
        sublap = null;
        creatingSublap = false;
    }

    public void onRowEdit(RowEditEvent event) {
        subLapService.saveOrUpdate(selectedSublap);
    }

    public void onRowCancel(RowEditEvent event) {
    }
}