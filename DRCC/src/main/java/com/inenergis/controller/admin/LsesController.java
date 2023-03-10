package com.inenergis.controller.admin;

import com.inenergis.entity.Lse;
import com.inenergis.entity.marketIntegration.Iso;
import com.inenergis.service.IsoService;
import com.inenergis.service.LseService;
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
public class LsesController implements Serializable {

    private static final long serialVersionUID = 1L;

    private Logger log = LoggerFactory.getLogger(LsesController.class);

    @Inject
    private UIMessage uiMessage;

    @Inject
    private IsoService isoService;

    @Inject
    private LseService lseService;

    private Lse lse = null;
    private Lse selectedLse;
    private List<Lse> lseList = null;
    private boolean creatingLse = false;
    private List<Iso> isoList;

    @PostConstruct
    public void onCreate() {
        lseList = lseService.getAll();
        isoList = isoService.getIsos();
    }

    public void add() {
        lse = new Lse();
        creatingLse = true;
    }

    public void save() {
        lseService.saveOrUpdate(lse);
        uiMessage.addMessage("{0} saved", lse.getName());
        cancel();
        onCreate();
    }

    public void cancel() {
        lse = null;
        creatingLse = false;
    }

    public void onRowEdit(RowEditEvent event) {
        lseService.saveOrUpdate(selectedLse);
    }

    public void onRowCancel(RowEditEvent event) {
    }
}