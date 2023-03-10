package com.inenergis.controller.admin;

import com.inenergis.entity.Manufacturer;
import com.inenergis.service.ManufacturerService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

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
@Transactional
public class ManufacturerController implements Serializable {

    private static final long serialVersionUID = 1L;

    Logger log = LoggerFactory.getLogger(ManufacturerController.class);

    @Inject
    UIMessage uiMessage;

    @Inject
    ManufacturerService manufacturerService;

    private List<Manufacturer> list;
    private boolean addingNew = false;
    private Manufacturer manufacturer;

    @PostConstruct
    public void init() {
        list = manufacturerService.getAll();
    }

    public void add() {
        addingNew = true;
        manufacturer = new Manufacturer();
    }

    public void save() {
        manufacturerService.save(manufacturer);
        list = manufacturerService.getAll();
        cancel();
    }

    public void cancel() {
        addingNew = false;
        manufacturer = null;
    }
}