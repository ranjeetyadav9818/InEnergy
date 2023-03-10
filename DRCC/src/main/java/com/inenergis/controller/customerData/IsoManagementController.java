package com.inenergis.controller.customerData;

import com.inenergis.controller.lazyDataModel.LazyIsoModel;
import com.inenergis.entity.marketIntegration.Iso;
import com.inenergis.service.IsoService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
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
public class IsoManagementController implements Serializable {

    Logger log =  LoggerFactory.getLogger(IsoManagementController.class);

    @Inject
    private EntityManager entityManager;

    @Inject
    private UIMessage uiMessage;

    @Inject
    private IsoService isoService;

    private LazyIsoModel isoList;

    private boolean newIso = false;

    private Iso iso;

    @PostConstruct
    public void init() {
        isoList = new LazyIsoModel(entityManager, new HashMap<>());
    }

    public void createNewIso() {
        newIso = true;
        iso = new Iso();
    }

    public void cancelNewIso() {
        newIso = false;
        iso = null;
    }

    public void saveNewIso() {
        try {
            isoService.saveOrUpdateIso(iso);
            uiMessage.addMessage("ISO {0} saved", iso.getName());
            newIso = false;
        } catch (IOException e) {
            uiMessage.addMessage("Error trying to save the ISO, please try again later and contact your administrator if you keep having this problem");
            log.warn("Error saving a Market", e);
        }
    }
}
