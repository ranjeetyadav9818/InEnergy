package com.inenergis.controller.admin;

import com.inenergis.entity.PricingNode;
import com.inenergis.entity.SubLap;
import com.inenergis.service.PricingNodeService;
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
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Named
@ViewScoped
public class PricingNodeController implements Serializable {

    private static final long serialVersionUID = 1L;

    private Logger log = LoggerFactory.getLogger(PricingNodeController.class);

    @Inject
    private UIMessage uiMessage;

    @Inject
    private PricingNodeService pricingNodeService;

    @Inject
    private SubLapService subLapService;

    private PricingNode pricingNode = null;
    private PricingNode selectedPricingNode;
    private List<PricingNode> pricingNodeList = null;
    private boolean creatingPricingNode = false;
    private List<SubLap> subLapList;

    @PostConstruct
    public void onCreate() {
        subLapList = subLapService.getAll();
        pricingNodeList = pricingNodeService.getAll();
    }

    public void add() {
        pricingNode = new PricingNode();
        creatingPricingNode = true;
    }

    public void save() {
        pricingNodeService.saveOrUpdate(pricingNode);
        uiMessage.addMessage("{0} saved", pricingNode.getName());
        cancel();
        onCreate();
    }

    public void cancel() {
        pricingNode = null;
        creatingPricingNode = false;
    }

    public void onRowEdit(RowEditEvent event) {
        pricingNodeService.saveOrUpdate(selectedPricingNode);
    }

    public void onRowCancel(RowEditEvent event) {
    }
}