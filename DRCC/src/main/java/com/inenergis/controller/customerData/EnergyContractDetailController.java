package com.inenergis.controller.customerData;


import com.inenergis.controller.carousel.ContractEntityCarousel;
import com.inenergis.controller.general.DocumentHelper;
import com.inenergis.controller.marketIntegration.EnergyContractController;
import com.inenergis.controller.model.EnergyArrayDataBeanList;
import com.inenergis.entity.Document;
import com.inenergis.entity.contract.ContractDevice;
import com.inenergis.entity.contract.ContractEntity;
import com.inenergis.entity.contract.Device;
import com.inenergis.entity.marketIntegration.EnergyContract;
import com.inenergis.entity.marketIntegration.Party;
import com.inenergis.model.FakeTransaction;
import com.inenergis.service.DocumentService;
import com.inenergis.service.EnergyContractService;
import com.inenergis.service.ExternalFileRepositoryService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.picketlink.Identity;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Named
@ViewScoped
@Getter
@Setter
public class EnergyContractDetailController implements Serializable {

    private EnergyContract contract;
    private List<EnergyArrayDataBeanList> contractDetails;
    private List<Document> documents;
    private List<FakeTransaction> transactions;
    private List<ContractDevice> contractDevicesToAdd;

    private boolean showAddLocationTab = false;

    @Inject
    private EnergyContractService energyContractService;
    @Inject
    private DocumentService documentService;
    @Inject
    private Identity identity;
    @Inject
    private ExternalFileRepositoryService fileRepositoryService;
    @Inject
    private ContractEntityCarousel entityCarousel;
    @Inject
    private EnergyContractController energyContractController;
    @Inject
    private DocumentHelper documentHelper;
    @Inject
    private EntityManager entityManager;
    @Inject
    UIMessage uiMessage;

    Logger log = LoggerFactory.getLogger(EnergyContractDetailController.class);

    @PostConstruct
    public void init() {
        final Long contractId = ParameterEncoderService.getDefaultDecodedParameterAsLong();
        doInit(contractId);
    }

    public void doInit(Long contractId) {
        contract = energyContractService.getById(contractId);
        contractDetails = printContractCarousel(contract);
        documents = documentService.getDocuments(contract);
        transactions = generateFakeTransactions();
        energyContractController.edit(contract);
        energyContractController.setReadonly(true);
    }

    public List<EnergyArrayDataBeanList> printContractCarousel(EnergyContract contract) { //todo icons to properties

        List<EnergyArrayDataBeanList> entityDetails = new ArrayList<>();
        // first build customer data
        for (Party party : contract.getParties()) {
            ContractEntity entity = party.getEntity();
            entityCarousel.generateContractEntityCarousel(entityDetails, entity);
        }

        return entityDetails;
    }


    public void submitFile(FileUploadEvent fileUpload) throws IOException {
        Document document = documentHelper.generateDocument(fileUpload.getFile(), identity, contract);
        fileRepositoryService.uploadFile(document.getUuid(), fileUpload.getFile().getInputstream(), fileUpload.getFile().getContents().length);
        documentService.saveDocument(document);
        documents.add(document);
    }

    public StreamedContent download(Document document) {
        return new DefaultStreamedContent(fileRepositoryService.getFile(document.getUuid()), document.getContentType(), document.getFileName());
    }

    public void delete(Document document) {
        fileRepositoryService.deleteFile(document.getUuid());
        documentService.deleteDocument(document);
        documents.remove(document);
    }


    public void editEntity(Object object) throws IOException {
        if (object instanceof ContractEntity) {
            ContractEntity entity = ((ContractEntity) object);
            FacesContext.getCurrentInstance().getExternalContext().redirect("NewContractEntity.xhtml?o=" + ParameterEncoderService.encode(entity.getId()));
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    private List<FakeTransaction> generateFakeTransactions() {
        List<FakeTransaction> result = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            result.add(FakeTransaction.builder().transactionId("" + i).date(new Date()).status("Completed").value("" + (new Random()).nextLong()).build());
        }
        return result;
    }

    public void forwardTransactionDetails(FakeTransaction transaction) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("TransactionDetails.xhtml");
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void addContractDevices() {
        initContractDevicesToAdd();
        showAddLocationTab = true;
    }

    public void addContractDevice(ContractDevice contractDevice) {
        contract.getContractDevices().add(contractDevice);
        contractDevicesToAdd.remove(contractDevice);
    }

    public void removeContractDevice(ContractDevice contractDevice) {
        contract.getContractDevices().remove(contractDevice);
        contractDevicesToAdd.add(contractDevice);
    }

    public void onDeviceRowEdit() {

    }

    public void saveContractDevices() {
        try {
            contract = energyContractService.saveOrUpdate(contract);
            showAddLocationTab = false;
            uiMessage.addMessage(energyContractController.ENERGY_CONTRACT_SAVED);
        } catch (IOException e) {
            uiMessage.addMessage("Error trying to save the Device, please try again later and contact your administrator if you keep having this problem");
            log.warn("Error saving a Market", e);
        }
    }

    public void onDeviceRowCancel() {
    }

    private void initContractDevicesToAdd() {
        final List<Party> parties = contract.getParties();
        if (CollectionUtils.isNotEmpty(parties)) {
            final List<ContractDevice> possibleContractDevicesFromBdd = parties.stream().map(p ->
                    {
                        final ContractEntity entity = p.getEntity();
                        final List<Device> devices = entity.getDevices();
                        if (CollectionUtils.isNotEmpty(devices)) {
                            final List<ContractDevice> entityDevices = devices.stream()
                                    .map(d -> new ContractDevice(d, contract))
                                    .collect(Collectors.toList());
                            return entityDevices;
                        }
                        return new ArrayList<ContractDevice>();
                    }
            ).flatMap(coll -> coll.stream()).collect(Collectors.toList());

            final List<ContractDevice> contractDevices = contract.getContractDevices();

            contractDevicesToAdd = CollectionUtils.isEmpty(contractDevices) ?
                    possibleContractDevicesFromBdd :
                    possibleContractDevicesFromBdd
                            .stream()
                            .filter(cd -> ! contractDevices.contains(cd))
                            .collect(Collectors.toList());

        }
    }
}
