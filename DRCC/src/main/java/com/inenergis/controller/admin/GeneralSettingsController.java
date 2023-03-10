package com.inenergis.controller.admin;

import com.inenergis.entity.config.CurrencyConfig;
import com.inenergis.service.CurrencyConfigService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class GeneralSettingsController implements Serializable {

    private static final long serialVersionUID = 1L;

    Logger log = LoggerFactory.getLogger(GeneralSettingsController.class);

    @Inject
    UIMessage uiMessage;

    @Inject
    private CurrencyConfigService currencyConfigService;

    private List<CurrencyConfig> list;
    private CurrencyConfig currentValue;

    @PostConstruct
    public void init() {
        list = currencyConfigService.getAll();
        currentValue = currencyConfigService.getSelected();
    }

    @Transactional
    public void update(){
        list = currencyConfigService.getAll();
        list.forEach(c -> c.setSelected(c.equals(currentValue)));
        currencyConfigService.saveAll(list);
        uiMessage.addMessage("Currency Changed", FacesMessage.SEVERITY_INFO);
    }

}