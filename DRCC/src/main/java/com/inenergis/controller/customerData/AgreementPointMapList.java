package com.inenergis.controller.customerData;

import com.inenergis.commonServices.AgreementPointMapServiceContract;
import com.inenergis.controller.authentication.AuthorizationChecker;
import com.inenergis.controller.carousel.GasServiceAgreementCarousel;
import com.inenergis.controller.carousel.ServiceAgreementCarousel;
import com.inenergis.controller.general.LeftMenu;
import com.inenergis.controller.lazyDataModel.LazyAgreementPointMapDataModel;
import com.inenergis.controller.lazyDataModel.LazyCustomerNotificationPreferenceDataModel;
import com.inenergis.controller.lazyDataModel.LazyInvoiceDataModel;
import com.inenergis.controller.model.BillingModel;
import com.inenergis.controller.model.RatePlanEligibility;
import com.inenergis.controller.program.RateEnrollmentHandler;
import com.inenergis.entity.*;
import com.inenergis.entity.asset.Asset;
import com.inenergis.entity.billing.Invoice;
import com.inenergis.entity.billing.Payment;
import com.inenergis.entity.genericEnum.CommodityType;
import com.inenergis.entity.genericEnum.ComodityType;
import com.inenergis.entity.program.AssetWrapper;
import com.inenergis.entity.program.ImpactedCustomer;
import com.inenergis.entity.program.RatePlan;
import com.inenergis.entity.program.RatePlanEnrollment;
import com.inenergis.exception.BusinessException;
import com.inenergis.service.*;
import com.inenergis.service.aws.IntervalDataService;
import com.inenergis.util.DateRange;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.picketlink.Identity;
import org.primefaces.component.export.PDFOptions;
import org.primefaces.context.PrimeFacesContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.PieChartModel;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.inenergis.entity.billing.Invoice.InvoiceStatus.FINAL;

@Named
@ViewScoped
@Getter
@Setter
public class AgreementPointMapList extends RateEnrollmentHandler implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final int INVOICES_TAB = 10;
    public static final String INVOICE_TAB = "invoiceTab";

    @Inject
    EntityManager entityManager;

    @Inject
    AuthorizationChecker authorizationChecker;

    @Inject
    HistoryService historyService;

    @Inject
    HistoryService gasHistoryService;


    @Inject
    private ServiceAgreementCarousel serviceAgreementCarousel;

    @Inject
    private EventService eventService;

    @Inject
    private RatePlanService ratePlanService;

    @Inject
    private UIMessage uiMessage;
    @Inject
    ServiceAgreementService serviceAgreementService;
    @Inject
    RatePlanEligibilityService ratePlanEligibilityService;

    @Inject
    private LeftMenu leftMenu;

    @Inject
    CustomerSearchNavigator customerSearchNavigator;

    @Inject
    AssetService assetService;

    @Inject
    ServiceAgreementAssetService serviceAgreementAssetService;

    @Inject
    AgreementPointMapServiceContract agreementPointMapService;


    @Inject
    GasAgreementPointMapList gasAgreementPointMapService;

    @Inject
    private GasServiceAgreementCarousel gasServiceAgreementCarousel;

    @Inject
    SecondaryContactService secondaryContactService;

    @Inject
    IntervalDataService intervalDataService;

    @Inject
    InvoiceService invoiceService;

    @Inject
    private PaymentService paymentService;

    @Inject
    private InvoiceChartService invoiceChartService = new InvoiceChartService();

    @Inject
    private Identity identity;

    private InvoicePDFHelper invoicePDFHelper;

    Logger log = LoggerFactory.getLogger(AgreementPointMapList.class);

    private String personId;
    private String serviceAgreementId;
    private String servicePointId;
    private String name;
    private String address;
    private String premisesId;
    private String phone;
    private String accountId;
    private String city;
    private String postCode;

    private boolean showData = false;
    private boolean showSearch = true;
    private boolean showCarousel = false;
    private boolean showSecondaryContactsSaveButton = false;
    private SecondaryContact newSecondaryContact;

    private AgreementPointMap selectedAgreementPointMap;
    private BaseServiceAgreement selectedServiceAgreement;
    private List<DataBeanList> serviceAgreementDetails = new ArrayList<>();
    private List<DataBeanList> customerDetails = new ArrayList<>();
    private List<GasDataBeanList> gasServiceAgreementDetails = new ArrayList<>();
    private List<GasDataBeanList> gasCustomerDetails = new ArrayList<>();
    private List<Asset> availableAssets = new ArrayList<>();
    private Asset asset;
    private List<AssetWrapper> assets = new ArrayList<>();
    private List<PdpSrEvent> pdpSrEvents = new ArrayList<>();

    private List<ServiceAgreement.SaStatus> saStatusList = Arrays.asList(ServiceAgreement.SaStatus.values());

    private LazyDataModel<AgreementPointMap> agreementPointMapLazyModel;
    private LazyDataModel<CustomerNotificationPreference> customerNotificationPreferenceLazyModel;
    private LazyInvoiceDataModel lazyInvoiceDataModel;
    private Map<String, Object> preFilters = new HashMap<>();
    private List<History> history;
    private List<History> gasHistory;

    private List<ImpactedCustomer> selectedServiceAgreementEvents = new ArrayList<>();

    private List<RatePlanEligibility> ratePlans = new ArrayList<>();

    private Long invoiceBalance;
    private Long debitTotal;
    private Long creditTotal;

    private List<Invoice> invoices;
    private List<Payment> payments;
    private List<BillingModel> bills = new ArrayList<>();
    private Payment newPayment;

    private int tabActiveIndex;
    private boolean isSearched = false;

    private RatePlanEnrollment tempEnrollment;
    private boolean showModal = false;
    private boolean showRatesTable = false;
    private Invoice selectedInvoice;
    private PDFOptions invoicePdfOptions;
    private BarChartModel monthlyBillingHistoryChart;
    private BarChartModel monthlyBillingHistoryConsumptionChart;
    private BarChartModel electricUsageThisPeriodChart;
    private PieChartModel feesPieChart;
    private String selectedMenu;
    private String saId;
    private List<AgreementPointMap> custlist;
    private BaseServiceAgreement serviceAgreement1;
    private  String commoditySA;
    private String gasServiceAgreementId;
    private int flag=0;
    @PostConstruct
    public void onCreate() {
        if(ParameterEncoderService.getDefaultDecodedParameter()!=null) {
            if (ParameterEncoderService.getDecodedParameter()==null) {
                final String serviceAgreementId = ParameterEncoderService.getDefaultDecodedParameter();
                if (serviceAgreementId != null) {
                    this.serviceAgreementId = serviceAgreementId;
                    doMoreSearch();
                }
            } else {
                String Commodity = ParameterEncoderService.getDecodedParameter();
                if (Commodity.equals("Gas")) {
                    String SAID = ParameterEncoderService.getDefaultDecodedParameter();
                    gasAgreementPointMapService.onGasCreate();
                    ParameterEncoderService.encode(SAID);
                } else {
                    final String serviceAgreementId = ParameterEncoderService.getDefaultDecodedParameter();
                    if (serviceAgreementId != null) {
                        this.serviceAgreementId = serviceAgreementId;
                        moreSearch();
                    }
                }
            }
        }
        else {
            customerSearchNavigator.setAgreementPointMapLazyModel(new LazyAgreementPointMapDataModel(personId, null, name, address, city, postCode, premisesId, accountId, null, this));
            agreementPointMapLazyModel = new LazyAgreementPointMapDataModel(this);
            customerNotificationPreferenceLazyModel = new LazyCustomerNotificationPreferenceDataModel(this);
            lazyInvoiceDataModel = new LazyInvoiceDataModel(entityManager, preFilters);
            List<AgreementPointMap> listAccount = agreementPointMapLazyModel.load(0, 1000, null, null, null);
            if(custlist != null && custlist.size() > 0){
                custlist.clear();
            }
            for(int i = 0; i < listAccount.size(); i++){
                if (custlist != null && custlist.size() > 0){
                    List<String> id = new ArrayList<>();
                    for(int j = 0; j < custlist.size(); j++){

                        id = Collections.singletonList(custlist.get(j).getServiceAgreement().getAccount().getAccountId());
                    }
                    if(!(id.contains(listAccount.get(i).getServiceAgreement().getAccount().getAccountId()))){

                        custlist.add(listAccount.get(i));
                    }
                }else{
                    custlist = new ArrayList<>();
                    custlist.add(listAccount.get(i));
                }
            }
        }
    }

    private void retrieveInvoiceTabIndex() {
        String invoiceTab = PrimeFacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(INVOICE_TAB);
        if (StringUtils.isNotEmpty(invoiceTab)) {
            tabActiveIndex = INVOICES_TAB;
        } else {
            tabActiveIndex = 0;
        }
    }

    private void doMoreSearch() {
        try{
            String saId = ParameterEncoderService.getDefaultDecodedParameter();
            serviceAgreement1 = serviceAgreementService.getById(saId);
            commoditySA = serviceAgreement1.getDecriminatorValue();
            gasServiceAgreementId=saId;
            moreSearch();
        }
        catch (Exception e)
        {
            moreSearch();
        }
    }
    private void moreSearch() {
        if(commoditySA != null) {
            if (commoditySA.equals("Gas")) {
                gasAgreementPointMapService.onGasCreate();
                ParameterEncoderService.encode(gasServiceAgreementId);
            } else {
                String phoneFormatted = ServiceAgreement.stripOutNoDigit(phone);
                agreementPointMapLazyModel = new LazyAgreementPointMapDataModel(personId, serviceAgreementId, name, address, city, postCode, premisesId, accountId, phoneFormatted, this);
                customerNotificationPreferenceLazyModel = new LazyCustomerNotificationPreferenceDataModel(personId, serviceAgreementId, name, address, city, postCode, premisesId, accountId, phoneFormatted, this);
                this.loadEvents();
                this.showData = true;
                showServiceAgreementDetails();
            }
        }
        else
        {
            String phoneFormatted = ServiceAgreement.stripOutNoDigit(phone);
            agreementPointMapLazyModel = new LazyAgreementPointMapDataModel(personId, serviceAgreementId, name, address, city, postCode, premisesId, accountId, phoneFormatted, this);
            customerNotificationPreferenceLazyModel = new LazyCustomerNotificationPreferenceDataModel(personId, serviceAgreementId, name, address, city, postCode, premisesId, accountId, phoneFormatted, this);
            this.loadEvents();
            this.showData = true;
            showServiceAgreementDetails();
        }
    }

    private void loadInvoicesAndPayments() {
        invoices = ((ServiceAgreement)selectedServiceAgreement).getInvoices();
        payments = ((ServiceAgreement)selectedServiceAgreement).getPayments();
        if (invoices != null && payments != null) {
            bills.clear();
            invoices.forEach(invoice -> bills.add(new BillingModel(invoice)));
            payments.forEach(payment -> bills.add(new BillingModel(payment)));
            bills.sort(Comparator.comparing(BillingModel::getDate));

            debitTotal = invoices.stream()
                    .filter(invoice -> invoice.getStatus().equals(FINAL))
                    .mapToLong(Invoice::getTotal)
                    .sum();
            creditTotal = payments.stream()
                    .mapToLong(Payment::getValue)
                    .sum();
            invoiceBalance = debitTotal - creditTotal;


            try {
                monthlyBillingHistoryChart = invoiceChartService.renderMonthlyBillingHistoryChart(selectedServiceAgreement.getServiceAgreementId(), LocalDateTime.now());
                monthlyBillingHistoryConsumptionChart = invoiceChartService.renderMonthlyBillingHistoryConsumptionChart(((ServiceAgreement)selectedServiceAgreement), LocalDateTime.now());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void doSearch() {
        try{
            gasAgreementPointMapService.pass(serviceAgreementId,address, name, accountId, city, phone, personId, postCode);

        }catch(Exception e){
            search();
            this.isSearched = true;
        }
    }

    public void search() {

        moreSearch();
        customerSearchNavigator.setAgreementPointMapLazyModel(new LazyAgreementPointMapDataModel(personId, serviceAgreementId, name, address, city, postCode, premisesId, accountId, ServiceAgreement.stripOutNoDigit(phone), this));
        buildPrefFilters(personId, serviceAgreementId, address, accountId, ServiceAgreement.stripOutNoDigit(phone));
        lazyInvoiceDataModel = new LazyInvoiceDataModel(entityManager, preFilters);
        this.isSearched = true;


    }

    private void showServiceAgreementDetails() {
        System.out.println("in line 354");
        //This for the automatic popping up
        if (selectedAgreementPointMap == null || selectedServiceAgreement == null) {
            final List<AgreementPointMap> list = agreementPointMapLazyModel.load(0, 2, null, null, null);
            if (list.size() == 1 || (list.size() >= 1 && StringUtils.isNotEmpty(personId))) { //personId has value because a agreement point map has been selected
                this.selectedAgreementPointMap = list.get(0);
                this.selectedServiceAgreement =  list.get(0).getServiceAgreement();
                searchSAEvents();
            }
        }
        if (selectedAgreementPointMap != null && selectedServiceAgreement != null) {

            this.loadSACarouselData();
            this.setShowSearch(false);
            this.setShowCarousel(true);
            this.loadHistory();
            retrieveInvoiceTabIndex();
            availableAssets = assetService.getAll(); //todo network type should be assigned at contract level
            availableAssets.removeAll(((ServiceAgreement) selectedServiceAgreement).getAssets().stream().map(ServiceAgreementAsset::getAsset).collect(Collectors.toList()));
            updateAssetWrapper();
            loadInvoicesAndPayments();
        }
        else {
            this.setSelectedAgreementPointMap(null);
            this.setSelectedServiceAgreement(null);
            this.setShowSearch(true);
            this.setShowCarousel(false);
            this.tabActiveIndex = 1;
        }
    }


    public void onServicePointSelect(SelectEvent event) throws IOException {
        System.out.println("insid onServicePointSelect ");
        AgreementPointMap apm = ((AgreementPointMap) event.getObject());
        if (apm.getCommodity().equals("Gas")){

            gasAgreementPointMapService.onServicePointSelect(event);
        }else{
            System.out.println("insid else ");
            FacesContext.getCurrentInstance().getExternalContext().redirect("ServicePointList.xhtml?o=" + ParameterEncoderService.encode(apm.getServicePoint().getServicePointId()));
            FacesContext.getCurrentInstance().responseComplete();
            System.out.println("outside else ");
        }

    }




    public void onEventSelect(SelectEvent event) throws IOException {
        ImpactedCustomer customer = ((ImpactedCustomer) event.getObject());
        FacesContext.getCurrentInstance().getExternalContext().redirect("EventDispatchDetail.xhtml?o=" + ParameterEncoderService.encode(customer.getEvent().getId()));
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void onServiceAgreementSelect(SelectEvent event) throws IOException, SQLException {
        final AgreementPointMap apm = (AgreementPointMap) event.getObject();
        this.accountId = null;
        this.serviceAgreementId = null;
//        selectedServiceAgreement = apm.getServiceAgreement();
//        the error is here selectedServiceAgreement from a previous transaction???
        if (apm.getCommodity().equals("Gas")){
            this.setShowCarousel(false);
            gasAgreementPointMapService.onServiceAgreementSelect(event);
        }
        else {
            gasAgreementPointMapService.clearData();
            this.serviceAgreementId = apm.getServiceAgreement().getServiceAgreementId();
            this.accountId = null;
            search();
        }
    }

    private void updateAssetWrapper() {
        assets.clear();

        for (ServiceAgreementAsset serviceAgreementAsset :((ServiceAgreement)selectedServiceAgreement).getAssets()) {
            Asset asset = serviceAgreementAsset.getAsset();
            AssetWrapper assetWrapper = new AssetWrapper();
            assetWrapper.setServicePoint("");
            assetWrapper.setProgram("");
            assetWrapper.setDeviceName(asset.getName());
            assetWrapper.setDeviceType(asset.getAssetProfile() != null ? asset.getAssetProfile().getName() : "");
            assetWrapper.setUsage(asset.getUsage() == null ? "?" : asset.getUsage().getName());
            assetWrapper.setManufacturer(asset.getManufacturer() != null ? asset.getManufacturer().getName() : "");
            assetWrapper.setModel(asset.getModel());
            assetWrapper.setSerial(asset.getSupplierPartNumber());
            assetWrapper.setOwner(asset.getOwnership() == null ? "?" : asset.getOwnership().getName());
            assetWrapper.setServiceAgreementAsset(serviceAgreementAsset);

            assets.add(assetWrapper);
        }

        for (AgreementPointMap agreementPointMap : agreementPointMapService.getListByIds(Collections.singletonList(selectedServiceAgreement.getServiceAgreementId()))) {
            AssetWrapper assetWrapper = new AssetWrapper();
            assetWrapper.setServicePoint(agreementPointMap.getServicePoint().getServicePointId());
            assetWrapper.setProgram("");
            assetWrapper.setDeviceName("Energy Meter");
            assetWrapper.setDeviceType(((ServicePoint)agreementPointMap.getServicePoint()).getMeter().getConfigType());
            assetWrapper.setUsage("?");
            assetWrapper.setManufacturer(((ServicePoint)agreementPointMap.getServicePoint()).getMeter().getMfg());
            assetWrapper.setSerial(((ServicePoint)agreementPointMap.getServicePoint()).getMeter().getBadgeNumber());
            assetWrapper.setInstalled(((ServicePoint)agreementPointMap.getServicePoint()).getMeter().getInstallDate());
            assetWrapper.setOwner("?");

            assets.add(assetWrapper);
        }
    }

    private void onServiceAgreementSelect(ServiceAgreement serviceAgreement) {
        if (serviceAgreement != null) {
            this.setSelectedServiceAgreement(serviceAgreement);
            this.serviceAgreementId = getSelectedServiceAgreement().getServiceAgreementId();
            search();
            searchSAEvents();
        }
    }

    private void searchSAEvents() {
        selectedServiceAgreementEvents = eventService.getAllImpactedCustomerBySAID(selectedServiceAgreement.getServiceAgreementId());
        generateServicePointMap();
    }


    public void onCustomerSelect(SelectEvent event) {
        AgreementPointMap apm = ((AgreementPointMap) event.getObject());
        if (apm.getCommodity().equals("Gas")){
            gasAgreementPointMapService.onCustomerSelect(event);
        }
        else{

            if (apm != null) {
                this.personId = apm.getServiceAgreement().getAccount().getPerson().getPersonId();
                this.accountId = apm.getServiceAgreement().getAccount().getAccountId();
                search();
            }
            loadInvoicesAndPayments();
        }

    }



    public void updateCard(String accountId){
        this.accountId = accountId;
        this.serviceAgreementId = null;
        gasAgreementPointMapService.clearData();
        loadInvoicesAndPayments();
        search();
    }

    public void setActiveTab(Integer index){
        this.tabActiveIndex = index;
        if(isSearched) {
            List<AgreementPointMap> list = agreementPointMapLazyModel.load(0, 2, null, null, null);
            if(custlist != null && custlist.size() > 0){
                custlist.clear();
            }
            for (int i = 0; i < list.size(); i++) {
                if (custlist != null && custlist.size() > 0) {
                    for (int j = 0; j < custlist.size(); j++) {
                        if (!(list.get(i).getServiceAgreement().getAccount().getAccountId()).equals(custlist.get(j).getServiceAgreement().getAccount().getAccountId())) {
                            custlist.add(list.get(i));
                        }
                    }
                } else {
                    custlist = new ArrayList<>();
                    custlist.add(list.get(i));
                }
            }
        }
    }
    public void onENPSelect(SelectEvent event) {

        CustomerNotificationPreference notificationPreference = ((CustomerNotificationPreference) event.getObject());
        if (notificationPreference != null) {
            this.serviceAgreementId = notificationPreference.getServiceAgreement().getServiceAgreementId();
            this.search();
        }
    }


    private void loadHistory() {
        if(selectedAgreementPointMap.getCommodity().equals("Gas")){
            gasHistory = gasHistoryService.getHistory(selectedAgreementPointMap);
        }
        else{
            history = historyService.getHistory(selectedAgreementPointMap);
        }


    }

    public void loadSACarouselData() {
        serviceAgreementDetails.clear();
        if(selectedAgreementPointMap.getCommodity().equals("Gas")){
            if(accountId != null) {
                gasCustomerDetails = gasServiceAgreementCarousel.printCustomerCarousel(selectedAgreementPointMap);
            }
            else{
                gasServiceAgreementDetails = gasServiceAgreementCarousel.printServiceAgreementCarousel(selectedAgreementPointMap);
            }

        }
        else {
            if (accountId != null) {
                customerDetails = serviceAgreementCarousel.printCustomerCarousel(selectedAgreementPointMap);
            } else {
                serviceAgreementDetails = serviceAgreementCarousel.printServiceAgreementCarousel(selectedAgreementPointMap);
            }
        }
    }

    public void onServiceAgreementClear() {
        this.setSelectedAgreementPointMap(null);
        this.setSelectedServiceAgreement(null);
        this.clearSACarouselData();
        this.setShowSearch(true);
        this.setShowCarousel(false);
    }

    public void clearSACarouselData() {
        serviceAgreementDetails = new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public void loadEvents() {
        pdpSrEvents = (List<PdpSrEvent>) entityManager.createQuery("SELECT e FROM PdpSrEvent e "
                        + "JOIN e.pdpSrParticipants as p "
                        + "JOIN p.serviceAgreement sa "
                        + "JOIN sa.account acc "
                        + "JOIN acc.person as per "
                        + "JOIN sa.agreementPointMaps apm "
                        + "JOIN apm.servicePoint sp "
                        + "JOIN sp.premise prem "
                        + "WHERE (:saId is NULL or p.saId = :saId) "
                        + "AND (:personId is NULL or per.personId = :personId) "
                        + "AND (:name is NULL or per.customerName = :name or per.businessName = :name) "
                        + "AND (:address IS NULL or prem.serviceAddress1 = :address) "
                        + "AND (:premisesId IS NULL or prem.premiseId = :premisesId) "
                        + "AND (:accountId IS NULL or acc.accountId = :accountId) "
                        + "AND (:phone IS NULL or sa.phone = :phone) "
                        + "AND (:postCode IS NULL or prem.servicePostal = :postCode) "
                        + "AND (:city IS NULL or prem.serviceCityUpr = :city) "
                        + "group by e")
                .setParameter("saId", this.serviceAgreementId)
                .setParameter("personId", this.personId)
                .setParameter("name", this.name)
                .setParameter("address", this.address)
                .setParameter("premisesId", this.premisesId)
                .setParameter("accountId", this.accountId)
                .setParameter("phone", ServiceAgreement.stripOutNoDigit(this.phone))
                .setParameter("postCode", this.postCode)
                .setParameter("city", this.city)
                .getResultList();
    }

    BaseServiceAgreement serviceAgreement;
    public void enrollRatePlan(RatePlanEnrollment enrollment) {
        System.out.println("inside nrollRatePlan 595");

        enroll(enrollment.getRatePlan());

    }

    public void unenrollRatePlan(RatePlanEnrollment enrollment) {
        System.out.println("inside unenrollRatePlan 601");

        unenroll(enrollment.getRatePlan());

    }

    public void checkRatePlansEligibility() {

        showRatesTable = true;
        ratePlans = generateRatePlans("Electricity");

    }

    public void enroll(RatePlanEligibility eligibility) {
        System.out.println("in eligibility");
        enroll(eligibility.getRatePlan());
    }

    public void enroll(RatePlan ratePlan) {
        System.out.println("in enrolllll");
        List<RatePlanEnrollment> enrollments = null;
        ComodityType commodityPlan =ratePlan.getCommodityType();
        String stringComm = commodityPlan.toString();
        String commoditySA = selectedServiceAgreement.getDecriminatorValue();

        if (stringComm.equals(commoditySA)) {

            try {
                enrollments = ratePlanService.enroll(ratePlan, ((ServiceAgreement) selectedServiceAgreement));
                if (((ServiceAgreement) selectedServiceAgreement).getRatePlanEnrollments() == null) {
                    ((ServiceAgreement) selectedServiceAgreement).setRatePlanEnrollments(new ArrayList<>());
                }
                ((ServiceAgreement) selectedServiceAgreement).getRatePlanEnrollments().addAll(enrollments);
                ratePlans = generateRatePlans(commoditySA);

                uiMessage.addMessage("Customer with SA {0} enrolled to {1}", selectedServiceAgreement.getServiceAgreementId(), generateIds(enrollments));

            }
            catch (BusinessException e) {
                log.error(e.getMessage());
                uiMessage.addMessage(e.getMessage(), FacesMessage.SEVERITY_ERROR, e.getBusinessInfo());
                System.out.println(e.getMessage());
            }
        }
        else {

            uiMessage.addMessage("Commodity of Service Agreement not matched with the Commodity of Rate Plan ", FacesMessage.SEVERITY_ERROR);

        }
    }

    public String generateIds(List<RatePlanEnrollment> enrollments) {
        StringBuilder sb = new StringBuilder();
        for (RatePlanEnrollment enrollment : enrollments) {
            sb = sb.append(enrollment.getRatePlan().getActiveProfile().buildRateCodeId()).append(" ");
        }
        return sb.toString();
    }


    private void unenrollOnePlan(RatePlanEnrollment enrollment) {
        try {
            ratePlanService.unenroll(enrollment);
            ratePlans = generateRatePlans("Electricity");
            uiMessage.addMessage("Customer with SA {0} unenrolled from {1}", selectedServiceAgreement.getServiceAgreementId(), enrollment.getRatePlan().getActiveProfile().buildRateCodeId());
        } catch (BusinessException e) {
            log.error(e.getMessage());
            uiMessage.addMessage(e.getMessage(), FacesMessage.SEVERITY_ERROR, e.getBusinessInfo());
        }
    }


    public void unenroll(RatePlanEligibility eligibility) {
        unenroll(eligibility.getRatePlan());
    }

    public void unenroll(RatePlan ratePlan) {

        if(flag ==0){

            flag++;
        RatePlanEnrollment enrollment = ratePlanService.findActiveRatePlanEnrollmentInServiceAgreement(((ServiceAgreement)selectedServiceAgreement), ratePlan);
        if (CollectionUtils.isEmpty(enrollment.getRatePlan().getActiveProfile().getApplicableRatePlans())) {
            unenrollOnePlan(enrollment);
        } else {
            askHowManyUnenrolls();
            tempEnrollment = enrollment;
        }
    }
        else{

            uiMessage.addMessage("Rate Plan already unenrolled",FacesMessage.SEVERITY_ERROR);

        }
    }


    public void unenrollAll() {
        List<RatePlanEnrollment> unenrollments = null;
        try {
            unenrollments = ratePlanService.unenrollAndRelated(tempEnrollment, ((ServiceAgreement)selectedServiceAgreement));
            uiMessage.addMessage("Customer with SA {0} unenrolled from {1}", selectedServiceAgreement.getServiceAgreementId(), generateIds(unenrollments));
            tempEnrollment = null;
            showModal = false;
            ratePlans = generateRatePlans("Electicity");
        } catch (BusinessException e) {
            log.error(e.getMessage());
            uiMessage.addMessage(e.getMessage(), FacesMessage.SEVERITY_ERROR, e.getBusinessInfo());
        }
    }

    public void unenrollOne() {
        unenrollOnePlan(tempEnrollment);
        tempEnrollment = null;
        showModal = false;
    }

    public void cancelUnenrollment() {
        tempEnrollment = null;
        showModal = false;
    }

    public void askHowManyUnenrolls() {
        showModal = true;
    }

    @Override
    public ServiceAgreement getServiceAgreement() {
        return ((ServiceAgreement)selectedServiceAgreement);
    }

    public boolean serviceAgreementDoeNotHaveThisActiveEnrollment(RatePlanEnrollment enrollment) {
        return ratePlanService.findActiveRatePlanEnrollmentInServiceAgreement(((ServiceAgreement)selectedServiceAgreement), enrollment.getRatePlan()) == null;
    }


    private MapModel mapModel;
    private Marker marker;
    private String mapCenter;

    public void generateServicePointMap() {
        mapModel = new DefaultMapModel();
        if(selectedServiceAgreement.getAgreementPointMaps().get(0).getCommodity().equals("Electricity")) {
            for (AgreementPointMap agreementPointMap : selectedServiceAgreement.getAgreementPointMaps()) {
                ServicePoint servicePoint = (ServicePoint) agreementPointMap.getServicePoint();
                if (servicePoint.getLatitude() != null && servicePoint.getLongitude() != null) {
                    LatLng coord1 = new LatLng(new Double(servicePoint.getLatitude()), new Double(servicePoint.getLongitude()));
                    Marker overlay = new Marker(coord1, "SP ID: " + servicePoint.getServicePointId());
                    mapModel.addOverlay(overlay);
                }
            }
            ServicePoint servicePoint = (ServicePoint) selectedServiceAgreement.getAgreementPointMaps().get(0).getServicePoint();
            mapCenter = "" + servicePoint.getLatitude() + "," + servicePoint.getLongitude();
        }
        else{
            for (AgreementPointMap agreementPointMap : selectedServiceAgreement.getAgreementPointMaps()) {
                GasServicePoint servicePoint = (GasServicePoint) agreementPointMap.getServicePoint();
                if (servicePoint.getLatitude() != null && servicePoint.getLongitude() != null) {
                    LatLng coord1 = new LatLng(new Double(servicePoint.getLatitude()), new Double(servicePoint.getLongitude()));
                    Marker overlay = new Marker(coord1, "SP ID: " + servicePoint.getServicePointId());
                    mapModel.addOverlay(overlay);
                }
            }
            GasServicePoint servicePoint = (GasServicePoint) selectedServiceAgreement.getAgreementPointMaps().get(0).getServicePoint();
            mapCenter = "" + servicePoint.getLatitude() + "," + servicePoint.getLongitude();
        }
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        if (event.getOverlay() instanceof Marker) {
            marker = (Marker) event.getOverlay();
        }
    }

    public void addAsset() {
        ((ServiceAgreement)selectedServiceAgreement).getAssets().add(new ServiceAgreementAsset(((ServiceAgreement)selectedServiceAgreement), asset));
        availableAssets.remove(asset);

        for (ServiceAgreementAsset serviceAgreementAsset : ((ServiceAgreement)selectedServiceAgreement).getAssets()) {
            serviceAgreementAssetService.save(serviceAgreementAsset);
        }
        updateAssetWrapper();
    }

    public void removeAsset(ServiceAgreementAsset serviceAgreementAsset) {
        ((ServiceAgreement)selectedServiceAgreement).getAssets().remove(serviceAgreementAsset);
        availableAssets.add(serviceAgreementAsset.getAsset());

        serviceAgreementAssetService.delete(serviceAgreementAsset);
        updateAssetWrapper();
    }

    public void removeSecondaryContact(SecondaryContact secondaryContact) {
        ((ServiceAgreement)selectedServiceAgreement).getSecondaryContacts().remove(secondaryContact);
        secondaryContactService.delete(secondaryContact);
        showSecondaryContactsSaveButton = true;
    }

    public void saveSecondaryContacts() {
        secondaryContactService.saveOrUpdate(newSecondaryContact);
        ((ServiceAgreement)selectedServiceAgreement).getSecondaryContacts().add(newSecondaryContact);
        newSecondaryContact = null;
        showSecondaryContactsSaveButton = false;
    }

    public void createNewSecondaryContact() {
        newSecondaryContact = new SecondaryContact(((ServiceAgreement)selectedServiceAgreement));
    }

    public void cancelNewSecondaryContract() {
        newSecondaryContact = null;
        tabActiveIndex= 0 ;
    }

    public void onSecondaryContactEdit(RowEditEvent event) {
        SecondaryContact secondaryContact = (SecondaryContact) event.getObject();
        secondaryContactService.saveOrUpdate(secondaryContact);
    }

    //Invoices
    //Code needed to filter dates by ranges
    DateRange dueDateRange = new DateRange(true);

    public void filterDates() {
        if (dueDateRange != null) {
            preFilters.put("dueDate", dueDateRange);
        }
    }

    private void buildPrefFilters(String personId, String serviceAgreementId, String address, String accountId, String phoneFormatted) {
        preFilters = new HashMap<>();
        if (StringUtils.isNotBlank(serviceAgreementId)) {
            preFilters.put("serviceAgreement.serviceAgreementId", serviceAgreementId);
        }
        if (StringUtils.isNotBlank(personId)) {
            preFilters.put("serviceAgreement.account.person.personId", personId);
        }
        if (StringUtils.isNotBlank(address)) {
            preFilters.put("serviceAgreement.account.accountId", accountId);
        }
        if (StringUtils.isNotBlank(phone)) {
            preFilters.put("serviceAgreement.phone", phoneFormatted);
        }
    }

    public void onSelectInvoice(SelectEvent e) throws IOException, SQLException {
        BillingModel billingModel = (BillingModel) e.getObject();
        if (billingModel.getInvoice() == null) {
            return;
        }
        selectedInvoice = billingModel.getInvoice();
        invoicePDFHelper = new InvoicePDFHelper(selectedInvoice);
        final LocalDateTime now = LocalDateTime.now();
        electricUsageThisPeriodChart = invoiceChartService.renderElectricUsageThisPeriodChart(((ServiceAgreement)selectedServiceAgreement), now);
        feesPieChart = invoiceChartService.renderFeesPieChart(selectedInvoice);
    }

    //InvoiceDetails
    public void onCloseSelectedInvoice() {
        selectedInvoice = null;
    }

    public List getSelectedInvoiceAsListForSummary() {
        return Collections.singletonList(selectedInvoice);
    }

    public void startNewPayment() {
        newPayment = new Payment();
        newPayment.setDate(LocalDateTime.now());
        newPayment.setServiceAgreement(((ServiceAgreement)selectedServiceAgreement));
    }

    public void saveNewPayment() {
        paymentService.save(newPayment);
        loadInvoicesAndPayments();
        newPayment = null;
    }

    public void cancelNewPayment() {
        newPayment = null;
    }

}