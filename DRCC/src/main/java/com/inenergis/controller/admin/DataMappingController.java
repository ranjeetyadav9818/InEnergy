package com.inenergis.controller.admin;

import com.inenergis.commonServices.DataMappingServiceContract;
import com.inenergis.entity.DataMapping;
import com.inenergis.entity.DataMappingType;
import com.inenergis.entity.Lse;
import com.inenergis.entity.SubLap;
import com.inenergis.entity.genericEnum.MeterType;
import com.inenergis.service.LseService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.service.ServicePointService;
import com.inenergis.service.SubLapService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.RowEditEvent;
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
@Transactional
public class DataMappingController implements Serializable {

    private static final long serialVersionUID = 1L;

    Logger log = LoggerFactory.getLogger(DataMappingController.class);

    @Inject
    UIMessage uiMessage;

    @Inject
    SubLapService subLapService;

    @Inject
    LseService lseService;

    @Inject
    ServicePointService servicePointService;

    @Inject
    DataMappingServiceContract dataMappingService;

    @Inject
    ParameterEncoderService parameterEncoderService;

    private Object entity = new Object();

    private boolean isNewDataMapping = false;

    private DataMapping dataMapping;

    private DataMapping selectedDataMapping;

    private List<DataMapping> dataMappingList;

    private DataMappingType currentDataMappingType;

    public List<SubLap> getSubLapList() {
        return subLapService.getAll();
    }

    public List<Lse> getLseList() {
        return lseService.getAll();
    }

    private DataMappingType[] dataMappingTypes = DataMappingType.values();

    private MeterType[] meterTypes = MeterType.values();

    @PostConstruct
    public void onCreate() {
        String type = ParameterEncoderService.getDefaultDecodedParameter();
        if (type != null) {
            try {
                currentDataMappingType = DataMappingType.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.error("Unknown data mapping type supplied: {}", type);
            }
        }

        if (currentDataMappingType != null) {
            dataMappingList = dataMappingService.getByType(currentDataMappingType);
        } else {
            dataMappingList = dataMappingService.getAll();
        }
    }

    public void add() {
        dataMapping = new DataMapping();
        dataMapping.setType(currentDataMappingType);
        isNewDataMapping = true;
    }

    public void save() {
        if (entity instanceof SubLap) {
            if (!isFeederIdValid(dataMapping.getSource())) {
                uiMessage.addMessage("Feeder {0} does not exist", FacesMessage.SEVERITY_ERROR, dataMapping.getSource());
                return;
            }
            SubLap sl = (SubLap) entity;
            dataMapping.setDestination(sl.getName());
            dataMapping.setDestinationCode(sl.getCode());
        } else if (entity instanceof Lse) {
            Lse lse = (Lse) entity;
            dataMapping.setDestination(lse.getName());
            dataMapping.setDestinationCode(lse.getCode());
        } else {
            dataMapping.setDestination(entity.toString());
            dataMapping.setDestinationCode(entity.toString());
        }

        dataMappingService.saveOrUpdate(dataMapping);
        uiMessage.addMessage("Data Mapping for {0} saved", dataMapping.getSource());
        dataMapping = null;
        entity = null;

        isNewDataMapping = false;

        this.onCreate();
    }

    public void cancelDataMapping() {
        this.dataMapping = null;
        isNewDataMapping = false;
    }

    @Transactional
    public void deleteDataMapping() {
        if (selectedDataMapping != null) {
            dataMappingList.remove(selectedDataMapping);
            dataMappingService.delete(selectedDataMapping);
        }
    }

    public void onRowEdit(RowEditEvent event) {
        DataMapping dataMapping = (DataMapping) event.getObject();
        dataMappingService.saveOrUpdate(dataMapping);
    }

    public void onRowCancel(RowEditEvent event) {
        this.dataMapping = (DataMapping) event.getObject();
    }

    private boolean isFeederIdValid(String feederId) {
        return !servicePointService.getByFeederId(feederId).isEmpty();
    }
}