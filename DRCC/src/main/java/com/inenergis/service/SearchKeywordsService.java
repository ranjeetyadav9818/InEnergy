package com.inenergis.service;

import com.inenergis.commonServices.ProgramServiceContract;
import com.inenergis.controller.general.ConstantsProvider;
import com.inenergis.entity.DataMappingType;
import com.inenergis.entity.assetTopology.NetworkType;
import com.inenergis.entity.genericEnum.CommodityType;
import com.inenergis.entity.genericEnum.MaintenanceClass;
import com.inenergis.entity.marketIntegration.Iso;
import com.inenergis.entity.program.Program;
import com.inenergis.model.SearchMatch;
import com.inenergis.model.SearchSuggestion;
import com.inenergis.util.BundleAccessor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.stream.Collectors;

import static com.inenergis.service.aws.ElasticUtilsService.AGGREGATOR;
import static com.inenergis.service.aws.ElasticUtilsService.APM;
import static com.inenergis.service.aws.ElasticUtilsService.ASSET;
import static com.inenergis.service.aws.ElasticUtilsService.CONTRACT;
import static com.inenergis.service.aws.ElasticUtilsService.CONTRACT_ENTITY;
import static com.inenergis.service.aws.ElasticUtilsService.DEVICE;
import static com.inenergis.service.aws.ElasticUtilsService.EVENT;
import static com.inenergis.service.aws.ElasticUtilsService.EVENT_NOTIFICATION;
import static com.inenergis.service.aws.ElasticUtilsService.INVOICE;
import static com.inenergis.service.aws.ElasticUtilsService.ISO;
import static com.inenergis.service.aws.ElasticUtilsService.LOCATION;
import static com.inenergis.service.aws.ElasticUtilsService.PROGRAM;
import static com.inenergis.service.aws.ElasticUtilsService.REGISTRATION;
import static com.inenergis.service.aws.ElasticUtilsService.RESOURCE;

@Stateless
@NoArgsConstructor //To avoid jboss isue https://github.com/wildfly/wildfly/pull/6165/files
public class SearchKeywordsService {

    @Inject
    IsoService isoService = new IsoService();
    @Inject
    ProgramServiceContract programService; // = new ProgramService();

    public static final String TASKS_AND_WORKFLOW = "Tasks &amp; Workflow";

    public static final String ASSET_MANAGEMENT = "Asset management";
    public static final String BILLING_MANAGEMENT = "Billing Management";
    public static final String PROGRAM_OPERATIONS = "Program Operations";
    public static final String SUPER_USER = "superUser";
    private static Map<String, LinkInfo> actions = new LinkedHashMap<>();
    private static Map<String, String> reservedWords = new LinkedHashMap<>();
    private static List<String> reservedWordsList;

    public static final String ADMINISTRATION = "Administration";

    @Inject
    public SearchKeywordsService(BundleAccessor bundleAccessor, NetworkTypeService networkTypeService) {
        addCRMActions();

        addAdministrationActions(bundleAccessor.getPropertyResourceBundle());

        addTasksAndWorkflowActions();

        addAssetManagementActions(networkTypeService);

        addBillingManagement();

        addProgramOperations();

        addIsoActions();

        addReservedWords();
    }

    private static void addReservedWords() {
        reservedWords.put("customers", APM);
        reservedWords.put("customer", APM);
        reservedWords.put("clients", APM);
        reservedWords.put("client", APM);
        reservedWords.put("service agreements", APM);
        reservedWords.put("service agreement", APM);
        reservedWords.put("service points", APM);
        reservedWords.put("service point", APM);
        reservedWords.put("aggregators", AGGREGATOR);
        reservedWords.put("aggregator", AGGREGATOR);
        reservedWords.put("market", ISO);
        reservedWords.put("markets", ISO);
        reservedWords.put("energy program", PROGRAM);
        reservedWords.put("energy programs", PROGRAM);
        reservedWords.put("event notification", EVENT_NOTIFICATION);
        reservedWords.put("event notifications", EVENT_NOTIFICATION);
        reservedWords.put("event", EVENT);
        reservedWords.put("events", EVENT);
        reservedWords.put("contract entity", CONTRACT_ENTITY);
        reservedWords.put("contract entities", CONTRACT_ENTITY);
        reservedWords.put("contract", CONTRACT);
        reservedWords.put("contracts", CONTRACT);
        reservedWords.put("asset", ASSET);
        reservedWords.put("assets", ASSET);
        reservedWords.put("device", DEVICE);
        reservedWords.put("devices", DEVICE);
        reservedWords.put("location", LOCATION);
        reservedWords.put("locations", LOCATION);
        reservedWords.put("registration", REGISTRATION);
        reservedWords.put("registrations", REGISTRATION);
        reservedWords.put("resource", RESOURCE);
        reservedWords.put("resources", RESOURCE);
        reservedWords.put("invoice", INVOICE);
        reservedWords.put("invoices", INVOICE);
        reservedWordsList = reservedWords.values().stream().distinct().collect(Collectors.toList());
    }

    private static void addCRMActions() {
        actions.put("Aggregator Search", new LinkInfo("AggregatorList.xhtml", "search", "Aggregators", "settings_input_component", null, null));
        actions.put("Aggregator Creation", new LinkInfo("CreateAggregator.xhtml", "add_box", "Aggregators", "settings_input_component", null, null));
        actions.put("Contract Entities Search", new LinkInfo("SearchContractEntity.xhtml", "search", "Contract entities", "perm_contact_calendar", null, null));
        actions.put("Contract Entities Creation", new LinkInfo("NewContractEntity.xhtml", "add_box", "Contract entities", "perm_contact_calendar", null, null));
        actions.put("Energy Contract Search", new LinkInfo("SearchContracts.xhtml", "search", "Contracts", "verified_user", null, null));
        actions.put("Energy Contract Creation", new LinkInfo("CreateNewContract.xhtml", "add_box", "Contracts", "verified_user", null, null));
        actions.put("Dashboard", new LinkInfo("dashboard.xhtml", "web", "Home", "home", null, null));
        actions.put("Customer Search", new LinkInfo("CustomerList.xhtml", "search", "Customers", "person", null, null));
        actions.put("Service Points", new LinkInfo("ServicePointList.xhtml", "power", "Customers", "person", null, null));
    }

    private static void addProgramOperations() {
        //Static
        actions.put("Event Schedule", new LinkInfo("ScheduleEvent.xhtml", "lightbulb_outline", PROGRAM_OPERATIONS, "lightbulb_outline", null, null));
        actions.put("Event Monitor", new LinkInfo("DispatchManagement.xhtml", "lightbulb_outline", PROGRAM_OPERATIONS, "lightbulb_outline", null, null));
        actions.put("Event Notification", new LinkInfo("EventList.xhtml", "lightbulb_outline", PROGRAM_OPERATIONS, "lightbulb_outline", null, null));
        actions.put("Event Forecasting", new LinkInfo("ForecastDispatch.xhtml", "lightbulb_outline", PROGRAM_OPERATIONS, "lightbulb_outline", null, null));
        //Dynamic
        actions.put("Energy Resource Profile", new LinkInfo("ProgramProfile.xhtml?o=", null, null, "lightbulb_outline", null, DynamicLink.ENERGY_PROGRAM));
        actions.put("Program Management", new LinkInfo("ProgramManagement.xhtml?o=", null, null, "lightbulb_outline", null, DynamicLink.ENERGY_PROGRAM));
        actions.put("Eligibility & Enrollment", new LinkInfo("EligibilityEnrollment.xhtml?o=", null, null, "lightbulb_outline", null, DynamicLink.ENERGY_PROGRAM));

    }

    private static void addIsoActions() {
        actions.put("Market Profile", new LinkInfo("IsoProfiles.xhtml?o=", null, null, "account_balance", null, DynamicLink.ISO));
        actions.put("Locations", new LinkInfo("Locations.xhtml?o=", null, null, "account_balance", null, DynamicLink.ISO));
        actions.put("Resource create", new LinkInfo("CreateResource.xhtml?o=", null, null, "account_balance", null, DynamicLink.ISO));
        actions.put("Resource search", new LinkInfo("Resources.xhtml?o=", null, null, "account_balance", null, DynamicLink.ISO));
        actions.put("Resource configuration", new LinkInfo("ResourceModificationEvaluation.xhtml?o=", null, null, "account_balance", null, DynamicLink.ISO));
        actions.put("Bidding", new LinkInfo("Bid.xhtml?o=", null, null, "account_balance", null, DynamicLink.ISO));
        actions.put("Award", new LinkInfo("AwardSummary.xhtml?o=", null, null, "account_balance", null, DynamicLink.ISO));
    }

    private static void addBillingManagement() {
        actions.put("Rate Code Library", new LinkInfo("RateCodeLibrary.xhtml", "attach_money", BILLING_MANAGEMENT, "attach_money", null, null));
        actions.put("Search Rate Plans", new LinkInfo("RatePlans.xhtml", "attach_money", BILLING_MANAGEMENT, "attach_money", null, null));
        actions.put("Rate Plan Eligibility Verification", new LinkInfo("RateEligibilityVerification.xhtml", "attach_money", BILLING_MANAGEMENT, "attach_money", null, null));
        actions.put("Rate Plan Bulk Customer Upload", new LinkInfo("BulkRateUpload.xhtml", "attach_money", BILLING_MANAGEMENT, "attach_money", null, null));
        actions.put("Aged Invoices", new LinkInfo("AgedInvoices.xhtml", "hourglass_empty", BILLING_MANAGEMENT, "attach_money", null, null));
        actions.put("Baseline Allowance", new LinkInfo("BaselineAllowance.xhtml", "show_chart", BILLING_MANAGEMENT, "attach_money", null, null));
        actions.put("Billing Cycle Schedule", new LinkInfo("BillingCycleSchedule.xhtml", "schedule", BILLING_MANAGEMENT, "attach_money", null, null));
        actions.put("Billing Cycle Statistics", new LinkInfo("BillingStatistics.xhtml", "pie_chart", BILLING_MANAGEMENT, "attach_money", null, null));
    }

    private static void addAssetManagementActions(NetworkTypeService networkTypeService) {
        for (CommodityType commodityType : CommodityType.values()) {
        final String param = ParameterEncoderService.encode(commodityType);
            actions.put(String.format("Asset Profiles (%s)", commodityType.getName()), new LinkInfo("AssetTypeProfile.xhtml?n=" + param, "extension", ASSET_MANAGEMENT, "extension", null, null));
            actions.put(String.format("Asset Groups (%s)", commodityType.getName()), new LinkInfo("AssetGroups.xhtml?n=" + param, "extension", ASSET_MANAGEMENT, "extension", null, null));
            actions.put(String.format("Map (%s)", commodityType.getName()), new LinkInfo("PipelineMap.xhtml?n=" + param, "extension", ASSET_MANAGEMENT, "extension", null, null));
        }
        for (NetworkType networkType : networkTypeService.getAll()) {
            final String params = "?n=" + ParameterEncoderService.encode(networkType.getCommodityType()) + "&nt=" + ParameterEncoderService.encode(networkType.getId());
            actions.put(String.format("%s Catalog", networkType.getName()), new LinkInfo("AssetCatalog.xhtml" + params, "list", ASSET_MANAGEMENT, "extension", null, null));
            actions.put(String.format("%s Inventory", networkType.getName()), new LinkInfo("AssetDeviceInventory.xhtml" + params, "apps", ASSET_MANAGEMENT, "extension", null, null));
        }
        actions.put("Catalogs and Inventories Import", new LinkInfo("CatalogAndInventoriesImportExport.xhtml", "compare_arrows", ASSET_MANAGEMENT, "extension", null, null));
    }

    private static void addTasksAndWorkflowActions() {
        actions.put("Monitor Tasks & Alerts", new LinkInfo("MonitorAlerts.xhtml", "vibration", TASKS_AND_WORKFLOW, "polymer", null, null));
        actions.put("Business Owner Config", new LinkInfo("BusinessOwner.xhtml", "people_outline", TASKS_AND_WORKFLOW, "polymer", null, null));
        actions.put("Task Configuration", new LinkInfo("Tasks.xhtml", "format_list_bulleted", TASKS_AND_WORKFLOW, "polymer", null, null));
        actions.put("Work Plan Configuration", new LinkInfo("WorkPlans.xhtml", "linear_scale", TASKS_AND_WORKFLOW, "polymer", null, null));
        actions.put("Alert Configuration", new LinkInfo("NotificationDefinition.xhtml", "report_problem", TASKS_AND_WORKFLOW, "polymer", null, null));
    }

    private static void addAdministrationActions(PropertyResourceBundle propertyResourceBundle) {
        actions.put("Season Calendar", new LinkInfo("SeasonCalendars.xhtml", "event", ADMINISTRATION, "settings", SUPER_USER, null));
        actions.put("Manage Users", new LinkInfo("UserList.xhtml", "group", ADMINISTRATION, "settings", SUPER_USER, null));
        actions.put("File Processor Logs", new LinkInfo("FileProcessorLogs.xhtml", "view_headline", ADMINISTRATION, "settings", SUPER_USER, null));
        for (DataMappingType type : ConstantsProvider.dataMappingTypes) {
            actions.put(propertyResourceBundle.getString(type.getId()) + " Data Mapping", new LinkInfo("DataMapping.xhtml?o=" + ParameterEncoderService.encode(type), "settings", ADMINISTRATION, "settings", SUPER_USER, null));
        }
        actions.put("Vendor Status Mapping", new LinkInfo("VendorStatusMappingList.xhtml", "settings", ADMINISTRATION, "settings", SUPER_USER, null));
        for (MaintenanceClass maintenanceClass : ConstantsProvider.maintenanceClasses) {
            actions.put(maintenanceClass.getName() + " Maintenance Data", new LinkInfo("MaintenanceData.xhtml?o=" + ParameterEncoderService.encode(maintenanceClass.name()), "settings", ADMINISTRATION, "settings", SUPER_USER, null));
        }
        actions.put("Manufacturers", new LinkInfo("Manufacturers.xhtml", "settings", ADMINISTRATION, "settings", SUPER_USER, null));
        actions.put("Pricing Nodes", new LinkInfo("PricingNodes.xhtml", "settings", ADMINISTRATION, "settings", SUPER_USER, null));
        actions.put("PNodes", new LinkInfo("PricingNodes.xhtml", "settings", ADMINISTRATION, "settings", SUPER_USER, null));
        actions.put(propertyResourceBundle.getString("data.mapping.sublaps"), new LinkInfo("Sublaps.xhtml", "settings", ADMINISTRATION, "settings", SUPER_USER, null));
        actions.put("LSEs", new LinkInfo("Lses.xhtml", "settings", ADMINISTRATION, "settings", SUPER_USER, null));
    }

    public List<SearchMatch> getSuggestions(String query) {
        List<SearchMatch> result = new ArrayList<>();
        if (query == null) {
            return result;
        }
        for (Map.Entry<String, LinkInfo> entry : actions.entrySet()) {
            if (entry.getKey().toLowerCase().contains(query.toLowerCase())) {
                addSuggestionToResult(result, entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    public void addSuggestionToResult(List<SearchMatch> result, String key, LinkInfo value) {
        if (value.getDynamicLink() != null) {
            switch (value.getDynamicLink()) {
                case ISO:
                    for (Iso iso : isoService.getIsos()) {
                        result.add(new SearchSuggestion(key, value.getUrl() + ParameterEncoderService.encode(iso.getId()), value.getIcon(), iso.getName(), value.getIconGroup(), value.getPermission()));
                    }
                    break;
                case ENERGY_PROGRAM:
                    for (Program program : programService.getPrograms()) {
                        result.add(new SearchSuggestion(key, value.getUrl() + ParameterEncoderService.encode(program.getId()), value.getIcon(), program.getName(), value.getIconGroup(), value.getPermission()));
                    }
                    break;
            }
        } else {
            result.add(new SearchSuggestion(key, value.getUrl(), value.getIcon(), value.getGroup(), value.getIconGroup(), value.getPermission()));
        }
    }

    public boolean isQueryExactSuggestion(String querySearch) {
        return actions.containsKey(querySearch);
    }

    public String[] getTypesForSearch(String queryForCheck) {
        String query = " " + queryForCheck + " ";
        if (StringUtils.isEmpty(query)) {
            return reservedWordsList.toArray(new String[0]);
        }
        List<String> types = new ArrayList<>();
        for (Map.Entry<String, String> entry : reservedWords.entrySet()) {
            if (queryHasReservedWord(query, entry)) {
                types.add(entry.getValue());
            }
        }
        if (types.size() > 0) {
            return types.stream().distinct().collect(Collectors.toList()).toArray(new String[0]);
        } else {
            return reservedWordsList.toArray(new String[0]);
        }
    }

    public String stripOutReservedWords(String query) {
        String result = query;
        for (Map.Entry<String, String> entry : reservedWords.entrySet()) {
            if (queryHasReservedWord(query, entry)) {
                result = result.replace(entry.getKey(), "");
            }
        }
        result = result.replace("\"", " ");
        return result;
    }

    public boolean queryHasReservedWord(String query, Map.Entry<String, String> entry) {
        return (" " + query + " ").contains(" " + entry.getKey() + " ") && !query.contains("\"" + entry.getKey() + "\"");
    }

    @Data
    @AllArgsConstructor
    private static class LinkInfo {
        private String url;
        private String icon;
        private String group;
        private String iconGroup;
        private String permission;
        private DynamicLink dynamicLink;
    }

    private static enum DynamicLink {
        ISO, ENERGY_PROGRAM
    }
}
