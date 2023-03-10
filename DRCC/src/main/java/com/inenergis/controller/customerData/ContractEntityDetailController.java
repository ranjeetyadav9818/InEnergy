package com.inenergis.controller.customerData;


import com.inenergis.controller.carousel.ContractEntityCarousel;
import com.inenergis.controller.general.ConstantsProvider;
import com.inenergis.controller.model.EnergyArrayDataBeanList;
import com.inenergis.entity.Note;
import com.inenergis.entity.contract.ContractAddress;
import com.inenergis.entity.contract.ContractEntity;
import com.inenergis.entity.contract.Device;
import com.inenergis.entity.contract.PointOfContact;
import com.inenergis.entity.maintenanceData.PowerSource;
import com.inenergis.service.ContractEntityService;
import com.inenergis.service.MaintenanceDataService;
import com.inenergis.service.NoteService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.picketlink.Identity;
import org.picketlink.idm.model.basic.User;
import org.primefaces.event.RowEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named
@ViewScoped
@Getter
@Setter
public class ContractEntityDetailController implements Serializable {

    private ContractEntity entity;
    private List<EnergyArrayDataBeanList> entityDetails;
    private Note newNote;
    private List<Note> notes;

    @Inject
    private ContractEntityService contractEntityService;
    @Inject
    private ContractEntityCarousel entityCarousel;
    @Inject
    private NoteService noteService;
    @Inject
    private Identity identity;
    @Inject
    private MaintenanceDataService maintenanceDataService;
    @Inject
    UIMessage uiMessage;

    Logger log = LoggerFactory.getLogger(ContractEntityDetailController.class);

    @PostConstruct
    public void init() {
        entity = contractEntityService.getById(ParameterEncoderService.getDefaultDecodedParameterAsLong());
        entityDetails = new ArrayList<>();

        entityCarousel.generateContractEntityCarousel(entityDetails, entity);
        notes = noteService.getNotes(ContractEntity.class.getName(), entity.getId().toString());
    }

    public void createNote() {
        newNote = new Note();
    }

    public void cancelNote() {
        newNote = null;
    }

    public void saveNote() {
        newNote.setAuthor(((User) identity.getAccount()).getEmail());
        newNote.setCreationDate(new Date());
        newNote.setEntity(ContractEntity.class.getName());
        newNote.setEntityId(entity.getId().toString());
        noteService.saveNote(newNote);
        notes.add(newNote);
        cancelNote();
    }

    public void editEntity() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("NewContractEntity.xhtml?o=" + ParameterEncoderService.encode(entity.getId()));
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void onRowCancel(RowEditEvent event) {
    }

    public void onRowEdit(RowEditEvent event) {
        final PointOfContact poc = (PointOfContact) event.getObject();
        try {
            entity = contractEntityService.save(poc.getContractEntity());
        } catch (IOException e) {
            uiMessage.addMessage("Error trying to save the Contract Entity, please try again later and contact your administrator if you keep having this problem");
            log.warn("Error saving a Contract Entity", e);
        }
    }

    public void onDeviceRowEdit(RowEditEvent event) {
        final Device device = (Device) event.getObject();
        try {
            entity = contractEntityService.save(device.getEntity());
        } catch (IOException e) {
            uiMessage.addMessage("Error trying to save the Device, please try again later and contact your administrator if you keep having this problem");
            log.warn("Error saving a Device", e);
        }
    }

    public List<PowerSource> getPowerSources() {
        return maintenanceDataService.getPowerSources();
    }
}
