package com.inenergis.controller.asset;

import com.inenergis.controller.carousel.AssetCarousel;
import com.inenergis.controller.general.DocumentHelper;
import com.inenergis.controller.model.EnergyArrayDataBeanList;
import com.inenergis.entity.AssetGroup;
import com.inenergis.entity.Document;
import com.inenergis.entity.Manufacturer;
import com.inenergis.entity.Note;
import com.inenergis.entity.asset.Asset;
import com.inenergis.entity.assetTopology.AssetProfile;
import com.inenergis.entity.contract.ContractEntity;
import com.inenergis.entity.device.AssetDevice;
import com.inenergis.service.AssetGroupService;
import com.inenergis.service.AssetProfileService;
import com.inenergis.service.AssetService;
import com.inenergis.service.ContractEntityService;
import com.inenergis.service.DocumentService;
import com.inenergis.service.ExternalFileRepositoryService;
import com.inenergis.service.ManufacturerService;
import com.inenergis.service.NoteService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Ajax;
import org.picketlink.Identity;
import org.picketlink.idm.model.basic.User;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
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
public class AssetDetailsController implements Serializable {


    Logger log = LoggerFactory.getLogger(AssetDetailsController.class);

    @Inject
    private ManufacturerService manufacturerService;

    @Inject
    private AssetProfileService assetProfileService;

    @Inject
    private AssetGroupService assetGroupService;

    @Inject
    private DocumentService documentService;

    @Inject
    private NoteService noteService;

    @Inject
    private AssetCarousel assetCarousel;

    @Inject
    private Identity identity;

    @Inject
    private DocumentHelper documentHelper;

    @Inject
    private ExternalFileRepositoryService fileRepositoryService;

    @Inject
    private ContractEntityService contractEntityService;

    @Inject
    private AssetService assetService;

    @Inject
    private UIMessage uiMessage;

    protected Asset asset;
    protected String detailsUrl;

    protected List<EnergyArrayDataBeanList> entityDetails;
    protected Note newNote;
    protected List<Note> notes;
    protected List<Document> documents;
    protected List<AssetProfile> assetProfiles;
    protected List<AssetGroup> assetGroups;

    protected boolean editMode = false;
    protected List<Manufacturer> manufacturers;

    @PostConstruct
    protected void init() {
        asset = assetService.getById(ParameterEncoderService.getDefaultDecodedParameterAsLong());
        doInit();
    }

    public void doInit() {
        entityDetails = new ArrayList<>();
        manufacturers = manufacturerService.getAll();
        assetProfiles = assetProfileService.getAll();
        assetGroups = assetGroupService.getAll();
        documents = documentService.getDocuments(asset);
        notes = noteService.getNotes(asset.getClass().getName(), asset.getId().toString());
        assetCarousel.generate(entityDetails, asset);
        detailsUrl = "DeviceDetails.xhtml";
    }

    public void clear() {
        editMode = false;
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
        newNote.setEntity(asset.getClass().getName());
        newNote.setEntityId(asset.getId().toString());
        noteService.saveNote(newNote);
        notes.add(newNote);
        cancelNote();
    }

    public void submitFile(FileUploadEvent fileUpload) throws IOException {
        Document document = documentHelper.generateDocument(fileUpload.getFile(), identity, asset);
        fileRepositoryService.uploadFile(document.getUuid(), fileUpload.getFile().getInputstream(), fileUpload.getFile().getContents().length);
        documentService.saveDocument(document);
        documents.add(document);
    }

    public void delete(Document document) {
        fileRepositoryService.deleteFile(document.getUuid());
        documentService.deleteDocument(document);
        documents.remove(document);
    }

    public StreamedContent download(Document document) {
        return new DefaultStreamedContent(fileRepositoryService.getFile(document.getUuid()), document.getContentType(), document.getFileName());
    }

    public List<ContractEntity> completeEntity(String query) {
        return contractEntityService.getByBusinessName(query);
    }

    public void editAsset() {
        editMode = true;
    }

    public void save() {
        try {
            doSave();
            Ajax.update("@form");
        } catch (IOException e) {
            uiMessage.addMessage(CatalogController.ERROR_TRYING_TO_SAVE_THE_ASSET);
            log.warn("Error saving the asset", e);
        }
    }

    public void doSave() throws IOException {
        asset = assetService.saveOrUpdate(asset);
        editMode = false;
        entityDetails.clear();
        assetCarousel.generate(entityDetails, asset);
        uiMessage.addMessage("Asset {0} saved",asset.getName());
    }

    public void editAttribute(RowEditEvent event) {
        try {
            asset = assetService.saveOrUpdate(asset);
        } catch (IOException e) {
            uiMessage.addMessage(CatalogController.ERROR_TRYING_TO_SAVE_THE_ASSET);
            log.warn("Error saving the asset", e);
        }
    }

    public void goToDeviceDetails(AssetDevice assetDevice) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(detailsUrl + "?o=" + ParameterEncoderService.encode(assetDevice.getId()));
        FacesContext.getCurrentInstance().responseComplete();
    }
}