package com.inenergis.controller.admin;

import com.inenergis.entity.genericEnum.MaintenanceClass;
import com.inenergis.entity.maintenanceData.MaintenanceData;
import com.inenergis.service.MaintenanceDataService;
import com.inenergis.service.ParameterEncoderService;
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
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Getter
@Setter
@Named
@ViewScoped
@Transactional
public class MaintenanceDataController implements Serializable {

    private static final long serialVersionUID = 1L;

    Logger log = LoggerFactory.getLogger(MaintenanceDataController.class);

    @Inject
    UIMessage uiMessage;

    @Inject
    MaintenanceDataService maintenanceDataService;

    private List<? extends MaintenanceData> list;
    private MaintenanceClass maintenanceClass;
    private boolean addingValue = false;
    private String newValue;

    @PostConstruct
    public void init() {
        String maintenanceData = ParameterEncoderService.getDefaultDecodedParameter();
        maintenanceClass = MaintenanceClass.valueOf(maintenanceData);
        list = maintenanceDataService.getMaintenanceDataListByClass(maintenanceClass.getClazz());
    }

    public void addValue() {
        addingValue = true;
    }

    public void save() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        maintenanceDataService.save(maintenanceClass.getClazz(), newValue);
        list = maintenanceDataService.getMaintenanceDataListByClass(maintenanceClass.getClazz());
        cancel();
    }

    public void cancel() {
        addingValue = false;
        newValue = null;
    }
}