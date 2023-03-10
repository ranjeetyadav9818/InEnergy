package com.inenergis.controller.marketIntegration;

import com.inenergis.entity.Feeder;
import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.SubLap;
import com.inenergis.entity.Substation;
import com.inenergis.entity.contract.ContractEntity;
import com.inenergis.entity.genericEnum.LinkageType;
import com.inenergis.entity.genericEnum.Month;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.marketIntegration.BillMonth;
import com.inenergis.entity.marketIntegration.Commodity;
import com.inenergis.entity.marketIntegration.CommodityProgram;
import com.inenergis.entity.marketIntegration.CommoditySubProgram;
import com.inenergis.entity.marketIntegration.Credit;
import com.inenergis.entity.marketIntegration.EnergyContract;
import com.inenergis.entity.marketIntegration.Fee;
import com.inenergis.entity.marketIntegration.FeeDetail;
import com.inenergis.entity.marketIntegration.Party;
import com.inenergis.entity.marketIntegration.Quantity;
import com.inenergis.entity.marketIntegration.RelatedContract;
import com.inenergis.entity.program.RatePlan;
import com.inenergis.service.CommodityProgramService;
import com.inenergis.service.CommoditySubProgramService;
import com.inenergis.service.ContractEntityService;
import com.inenergis.service.EnergyContractService;
import com.inenergis.service.HistoryService;
import com.inenergis.service.IsoResourceService;
import com.inenergis.service.RatePlanService;
import com.inenergis.service.ServiceAgreementService;
import com.inenergis.service.ServicePointService;
import com.inenergis.service.SubLapService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.picketlink.Identity;
import org.primefaces.event.DragDropEvent;
import org.primefaces.model.DualListModel;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Named
@ViewScoped
@Getter
@Setter
public class EnergyContractController implements Serializable {

    public static final String ENERGY_CONTRACT_SAVED = "Energy Contract saved";
    @Inject
    EnergyContractService energyContractService;

    @Inject
    ContractEntityService contractEntityService;

    @Inject
    CommodityProgramService commodityProgramService;

    @Inject
    CommoditySubProgramService commoditySubProgramService;

    @Inject
    ServiceAgreementService serviceAgreementService;

    @Inject
    IsoResourceService resourceService;
    @Inject
    RatePlanService ratePlanService;
    @Inject
    SubLapService subLapService;
    @Inject
    ServicePointService servicePointService;

    @Inject
    HistoryService historyService;
    @Inject
    UIMessage uiMessage;
    @Inject
    Identity identity;

    Logger log = LoggerFactory.getLogger(EnergyContractController.class);

    private EnergyContract energyContract;
    private String rawServiceAgreements;
    private List<EnergyContract> availableEnergyContracts;
    private boolean readonly = false;
    private boolean editMode = false;
    private String selectedLink;

    private DualListModel<Month> billMonth = new DualListModel<>();

    private List<CommodityProgram> commodityPrograms;
    private List<CommoditySubProgram> commoditySubPrograms;

    private LinkageType linkType;
    private List<IdentifiableEntity> availableLinks = new ArrayList<>();
    private List<IdentifiableEntity> selectedLinks = new ArrayList<>();

    @PostConstruct
    public void init() {
        energyContract = new EnergyContract();
        energyContract.setFees(new ArrayList<>());
        energyContract.setBillMonths(new ArrayList<>());
        energyContract.setRelatedContracts(new ArrayList<>());
        energyContract.setCommodities(new ArrayList<>());
        energyContract.setParties(new ArrayList<>());
        energyContract.setCredits(new ArrayList<>());
        energyContract.setContractDevices(new ArrayList<>());
        energyContract.setContractServiceAgreements(new HashSet<>());
        availableEnergyContracts = energyContractService.getAll();

        List<Month> billMonthTarget = new ArrayList<>();
        for (BillMonth billMonth : energyContract.getBillMonths()) {
            billMonthTarget.add(Month.findByMonthNumber(billMonth.getMonth()));
        }

        List<Month> billMonthSource = Arrays.stream(Month.values())
                .filter(month -> !billMonthTarget.contains(month))
                .collect(Collectors.toList());

        billMonth.setSource(billMonthSource);
        billMonth.setTarget(billMonthTarget);

        commodityPrograms = commodityProgramService.getAll();
        commoditySubPrograms = commoditySubProgramService.getAll();
    }

    @Transactional
    public void save() {
        List<BillMonth> updatedBillMonths = new ArrayList<>();
        for (Month month : billMonth.getTarget()) {
            updatedBillMonths.add(new BillMonth(energyContract, month.getValue()));
        }
        assignLinkage();
        energyContract.setBillMonths(updatedBillMonths);
        try {
            energyContract = energyContractService.saveOrUpdate(energyContract);
            uiMessage.addMessage(ENERGY_CONTRACT_SAVED);
        } catch (IOException e) {
            uiMessage.addMessage("Error trying to save the Energy Contract, please try again later and contact your administrator if you keep having this problem");
            log.warn("Error saving an energy contract", e);
        }
    }

    public void addServiceAgreements() {
        List<String> serviceAgreementIds = Pattern.compile(",", Pattern.LITERAL)
                .splitAsStream(rawServiceAgreements)
                .map(String::trim)
                .collect(Collectors.toList());

        List<ServiceAgreement> serviceAgreementsToAdd = serviceAgreementService.getAllById(serviceAgreementIds);

        List<String> validServiceAgreementIds = serviceAgreementsToAdd.stream()
                .map(ServiceAgreement::getServiceAgreementId)
                .collect(Collectors.toList());

        if (serviceAgreementIds.size() != validServiceAgreementIds.size()) {
            String invalidServiceAgreementIds = serviceAgreementIds.stream()
                    .filter(id -> !validServiceAgreementIds.contains(id))
                    .collect(Collectors.joining(", "));

            uiMessage.addMessage("The following Service Agreement ids are invalid: " + invalidServiceAgreementIds, FacesMessage.SEVERITY_ERROR);
            return;
        }

        energyContract.getContractServiceAgreements().addAll(serviceAgreementsToAdd);
        rawServiceAgreements = null;
    }

    public void removeServiceAgreement(ServiceAgreement serviceAgreement) {
        energyContract.getContractServiceAgreements().remove(serviceAgreement);
    }

    public void saveAndClose() throws IOException {
        save();
        FacesContext.getCurrentInstance().getExternalContext().redirect("SearchContracts.xhtml");
    }

    public void clear() {
        init();
    }

    public void addParty() {
        if (energyContract.getParties() == null) {
            energyContract.setParties(new ArrayList<>());
        }

        energyContract.getParties().add(new Party(energyContract));
    }

    public void addFee() {
        if (energyContract.getFees() == null) {
            energyContract.setFees(new ArrayList<>());
        }

        energyContract.getFees().add(new Fee(energyContract));
    }

    public void addCredit() {
        if (energyContract.getCredits() == null) {
            energyContract.setCredits(new ArrayList<>());
        }

        energyContract.getCredits().add(new Credit(energyContract));
    }

    public void addCommodity() {
        if (energyContract.getCommodities() == null) {
            energyContract.setCommodities(new ArrayList<>());
        }

        energyContract.getCommodities().add(new Commodity(energyContract));
    }

    public void addQuantity(Commodity commodity) {
        if (commodity.getQuantities() == null) {
            commodity.setQuantities(new ArrayList<>());
        }

        commodity.getQuantities().add(new Quantity(commodity));
    }

    public void addRelatedContract() {
        if (energyContract.getRelatedContracts() == null) {
            energyContract.setRelatedContracts(new ArrayList<>());
        }

        energyContract.getRelatedContracts().add(new RelatedContract(energyContract));
    }

    public void removeRelatedContract(RelatedContract relatedContract) {
        energyContract.getRelatedContracts().remove(relatedContract);
    }

    public void removeParty(Party party) {
        energyContract.getParties().remove(party);
    }

    public void removeFee(Fee fee) {
        energyContract.getFees().remove(fee);
    }

    public void removeCredit(Credit credit) {
        energyContract.getCredits().remove(credit);
    }

    public void removeCommodity(Commodity commodity) {
        energyContract.getCommodities().remove(commodity);
    }

    public void removeQuantity(Commodity commodity, Quantity quantity) {
        commodity.getQuantities().remove(quantity);
    }

    public void edit(EnergyContract energyContract) {
        clear();
        editMode = true;
        this.energyContract = energyContractService.getById(energyContract.getId());
        this.energyContract.getBillMonths().forEach(bm -> {
            billMonth.getSource().remove(Month.findByMonthNumber(bm.getMonth()));
            billMonth.getTarget().add(Month.findByMonthNumber(bm.getMonth()));
        });
        assignSelectedLinks();
    }

    private void assignSelectedLinks() {
        prepareLinkage();
        for (RatePlan ratePlan : energyContract.getRatePlans()) {
            selectedLinks.add(ratePlan);
        }
        for (IsoResource resource : energyContract.getResources()) {
            selectedLinks.add(resource);
        }
        for (SubLap subLap : energyContract.getSubLaps()) {
            selectedLinks.add(subLap);
        }
        for (Substation substation : energyContract.getSubstations()) {
            selectedLinks.add(substation);
        }
        for (Feeder feeder : energyContract.getFeeders()) {
            selectedLinks.add(feeder);
        }
    }

    public List<ContractEntity> completeEntity(String query) {
        return contractEntityService.getByBusinessName(query);
    }

    public List<EnergyContract> completeContract(String query) {
        FacesContext context = FacesContext.getCurrentInstance();
        RelatedContract contractTypeComponent = (RelatedContract) UIComponent.getCurrentComponent(context).getAttributes().get("contractType");

        return energyContractService.findByTypeName(contractTypeComponent.getType(), query);
    }

    public void makeReadonly() {
        readonly = true;
    }

    public void addDetail(Fee fee) {
        fee.getFeeDetailList().add(new FeeDetail(fee));
    }

    public void removeDetail(Fee fee, FeeDetail detail) {
        fee.getFeeDetailList().remove(detail);
    }

    public List<String> completeLink(String query) {
        if (linkType.equals(LinkageType.SUBSTATIONS)) {
            List<String> collect = availableLinks.stream().filter(link -> StringUtils.containsIgnoreCase(((Substation) link).getName(), query)).limit(5).map(link -> ((Substation) link).getName()).collect(Collectors.toList());
            return collect;
        } else {
            List<String> collect = availableLinks.stream().filter(link -> StringUtils.containsIgnoreCase(((Feeder) link).getName(), query)).limit(5).map(link -> ((Feeder) link).getName()).collect(Collectors.toList());
            return collect;
        }
    }

    public void updateSearchForm(AjaxBehaviorEvent event) {
        String s = (String) event.getComponent().getAttributes().get("value");
        if (linkType.equals(LinkageType.SUBSTATIONS)) {
            Optional<IdentifiableEntity> first = availableLinks.stream().filter(link -> ((Substation) link).getName().equals(s)).findFirst();
            selectedLinks.add(first.get());
            availableLinks.remove(first.get());
        } else {
            Optional<IdentifiableEntity> first = availableLinks.stream().filter(link -> ((Feeder) link).getName().equals(s)).findFirst();
            selectedLinks.add(first.get());
            availableLinks.remove(first.get());
        }
        selectedLink = null;
    }

    public void updateLinkage() {
        selectedLink = null;
        if (linkType == null) {
            return;
        }
        availableLinks.clear();
        switch (linkType) {
            case RATE_PLANS:
                availableLinks.addAll(cleanSelectedLinks(ratePlanService.getAll()));
                break;
            case RESOURCES:
                availableLinks.addAll(cleanSelectedLinks(resourceService.getAll()));
                break;
            case SUBLAPS:
                availableLinks.addAll(cleanSelectedLinks(subLapService.getAll()));
                break;
            case SUBSTATIONS:
                availableLinks.addAll(cleanSelectedLinks(servicePointService.getSubstations()));
                break;
            case FEEDERS:
                availableLinks.addAll(cleanSelectedLinks(servicePointService.getFeeders()));
                break;
        }
    }

    private Collection<? extends IdentifiableEntity> cleanSelectedLinks(List<? extends IdentifiableEntity> list) {
        for (IdentifiableEntity link : selectedLinks) {
            list.remove(link);
        }
        return list;
    }

    public boolean showDragDrop() {
        if (linkType == null) {
            return false;
        }
        switch (linkType) {
            case RATE_PLANS:
            case RESOURCES:
            case SUBLAPS:
                return true;
            case SUBSTATIONS:
            case FEEDERS:
            default:
                return false;
        }
    }

    public void undo(IdentifiableEntity entity) {
        selectedLinks.remove(entity);
        updateLinkage();
    }

    public void onLinkDrop(DragDropEvent ddEvent) {
        IdentifiableEntity data = (IdentifiableEntity) ddEvent.getData();
        availableLinks.remove(data);
        selectedLinks.add(data);
    }

    private void assignLinkage() {
        if (selectedLinks.isEmpty()) {
            energyContract.clearLinkage();
        } else {
            prepareLinkage();
            removeUnselectedLinkage();
            includeSelectedLinkage();
        }
    }

    private void prepareLinkage() {
        if (energyContract.getRatePlans() == null) {
            energyContract.setRatePlans(new ArrayList<>());
        }
        if (energyContract.getResources() == null) {
            energyContract.setResources(new ArrayList<>());
        }
        if (energyContract.getSubLaps() == null) {
            energyContract.setSubLaps(new ArrayList<>());
        }
        if (energyContract.getSubstations() == null) {
            energyContract.setSubstations(new ArrayList<>());
        }
        if (energyContract.getFeeders() == null) {
            energyContract.setFeeders(new ArrayList<>());
        }
    }

    private void removeUnselectedLinkage() {
        Iterator<RatePlan> iterator1 = energyContract.getRatePlans().iterator();
        while (iterator1.hasNext()) {
            RatePlan ratePlan = iterator1.next();
            if (!selectedLinks.contains(ratePlan)) {
                iterator1.remove();
            }
        }
        Iterator<IsoResource> iterator2 = energyContract.getResources().iterator();
        while (iterator2.hasNext()) {
            IsoResource resource = iterator2.next();
            if (!selectedLinks.contains(resource)) {
                iterator2.remove();
            }
        }
        Iterator<SubLap> iterator3 = energyContract.getSubLaps().iterator();
        while (iterator3.hasNext()) {
            SubLap subLap = iterator3.next();
            if (!selectedLinks.contains(subLap)) {
                iterator3.remove();
            }
        }
        Iterator<Substation> iterator4 = energyContract.getSubstations().iterator();
        while (iterator4.hasNext()) {
            Substation substation = iterator4.next();
            if (!selectedLinks.contains(substation)) {
                iterator4.remove();
            }
        }
        Iterator<Feeder> iterator5 = energyContract.getFeeders().iterator();
        while (iterator5.hasNext()) {
            Feeder feeder = iterator5.next();
            if (!selectedLinks.contains(feeder)) {
                iterator5.remove();
            }
        }
    }

    private void includeSelectedLinkage() {
        for (IdentifiableEntity link : selectedLinks) {
            if (link instanceof RatePlan) {
                if (!energyContract.getRatePlans().contains(link)) {
                    energyContract.getRatePlans().add(((RatePlan) link));
                }
            }
            if (link instanceof IsoResource) {
                if (!energyContract.getResources().contains(link)) {
                    energyContract.getResources().add(((IsoResource) link));
                }
            }
            if (link instanceof SubLap) {
                if (!energyContract.getSubLaps().contains(link)) {
                    energyContract.getSubLaps().add(((SubLap) link));
                }
            }
            if (link instanceof Substation) {
                if (!energyContract.getSubstations().contains(link)) {
                    energyContract.getSubstations().add(((Substation) link));
                }
            }
            if (link instanceof Feeder) {
                if (!energyContract.getFeeders().contains(link)) {
                    energyContract.getFeeders().add(((Feeder) link));
                }
            }
        }
    }
}