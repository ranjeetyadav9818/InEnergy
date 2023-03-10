package com.inenergis.controller.customerData;


import com.inenergis.entity.Country;
import com.inenergis.entity.contract.ContractAddress;
import com.inenergis.entity.contract.ContractEntity;
import com.inenergis.entity.contract.Device;
import com.inenergis.entity.contract.POCEmail;
import com.inenergis.entity.contract.POCPhone;
import com.inenergis.entity.contract.PointOfContact;
import com.inenergis.entity.maintenanceData.PowerSource;
import com.inenergis.service.ContractEntityService;
import com.inenergis.service.CountryService;
import com.inenergis.service.MaintenanceDataService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@Getter
@Setter
public class NewContractController implements Serializable {

    private ContractEntity entity;

    @Inject
    private ContractEntityService contractEntityService;

    @Inject
    private CountryService countryService;

    @Inject
    UIMessage uiMessage;

    @Inject
    private MaintenanceDataService maintenanceDataService;

    private List<Country> countries;

    Logger log = LoggerFactory.getLogger(NewContractController.class);

    @PostConstruct
    public void init() {
        countries = countryService.getAll();
        final Long id = ParameterEncoderService.getDefaultDecodedParameterAsLong();
        if (id != null) {
            entity = contractEntityService.getById(id);
        } else {
            entity = new ContractEntity();
        }
    }

    public List<ContractEntity> completeParent(String query) {
        List<ContractEntity> result = new ArrayList<>();
        final List<ContractEntity> byBusinessName = contractEntityService.getByBusinessName(query);
        final List<ContractEntity> byDba = contractEntityService.getByDba(query);
        if (CollectionUtils.isNotEmpty(byBusinessName)) {
            result.addAll(byBusinessName);
        }
        if (CollectionUtils.isNotEmpty(byDba)) {
            result.addAll(byDba);
        }
        return result;
    }

    public void cancel() {
        entity = new ContractEntity();
    }

    public void save() {
        try {
            entity = contractEntityService.save(entity);
            uiMessage.addMessage("Contract Entity saved");
        } catch (IOException e) {
            uiMessage.addMessage("Error trying to save the ISO, please try again later and contact your administrator if you keep having this problem");
            log.warn("Error saving a Market", e);
        }

    }

    public void saveAndClose() {
        save();
        cancel();
    }

    public void addAddress() {
        entity.getContractAddresses().add(new ContractAddress(entity));
    }

    public void addDevice() {entity.getDevices().add(new Device(entity));}

    public void removeDevice(Device device) {entity.getDevices().remove(device);}

    public void removeAddress(ContractAddress address) {
        entity.getContractAddresses().remove(address);
    }


    public void addPOC() {
        entity.getPointOfContacts().add(new PointOfContact(entity));
    }

    public void removePOC(PointOfContact poc) {
        entity.getPointOfContacts().remove(poc);
    }

    public void addPhone(PointOfContact pointOfContact){
        pointOfContact.getPocPhones().add(new POCPhone(pointOfContact));
    }

    public void addEmail(PointOfContact pointOfContact){
        pointOfContact.getPocEmails().add(new POCEmail(pointOfContact));
    }

    public void removePhone(PointOfContact pointOfContact, POCPhone pocPhone){
        pointOfContact.getPocPhones().remove(pocPhone);
    }

    public void removeEmail(PointOfContact pointOfContact, POCEmail pocEmail){
        pointOfContact.getPocEmails().remove(pocEmail);
    }

    public List<PowerSource> getPowerSources(){
        return maintenanceDataService.getPowerSources();
    }

}
