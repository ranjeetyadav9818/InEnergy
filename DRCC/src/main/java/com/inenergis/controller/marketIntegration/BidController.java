package com.inenergis.controller.marketIntegration;

import com.inenergis.controller.general.ConstantsProvider;
import com.inenergis.entity.Event;
import com.inenergis.entity.HourEndObject;
import com.inenergis.entity.ProductType;
import com.inenergis.entity.SubLap;
import com.inenergis.entity.TemperatureForecast;
import com.inenergis.entity.bidding.Bid;
import com.inenergis.entity.bidding.BidHelper;
import com.inenergis.entity.bidding.Segment;
import com.inenergis.entity.genericEnum.BidAction;
import com.inenergis.entity.genericEnum.BidStatus;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import com.inenergis.entity.marketIntegration.Iso;
import com.inenergis.entity.trove.MeterForecast;
import com.inenergis.service.BidService;
import com.inenergis.service.EventService;
import com.inenergis.service.IsoRegistrationService;
import com.inenergis.service.IsoService;
import com.inenergis.service.MeterForecastService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.service.SubLapService;
import com.inenergis.service.TemperatureForecastService;
import com.inenergis.util.BundleAccessor;
import com.inenergis.util.RiskCalculator;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.picketlink.Identity;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.credential.Credentials;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.credential.UsernamePasswordCredentials;
import org.picketlink.idm.model.basic.User;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSeparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.inenergis.util.ConstantsProviderModel.CUSTOMER_TIMEZONE_ID;

@Named
@ViewScoped
@Getter
@Setter
public class BidController implements Serializable {

    @Inject
    private IsoRegistrationService isoRegistrationService;
    @Inject
    private IsoService isoService;

    @Inject
    private SubLapService subLapService;

    @Inject
    private BidService bidService;

    @Inject
    private MeterForecastService meterForecastService;

    @Inject
    private TemperatureForecastService temperatureForecastService;

    @Inject
    private UIMessage uiMessage;

    @Inject
    private Identity identity;

    @Inject
    private IdentityManager identityManager;

    @Inject
    private EventService eventService;

    @Inject
    private BundleAccessor bundleAccessor;

    private Logger log = LoggerFactory.getLogger(BidController.class);

    private Date tradeDate;
    private Double forecastedTemperature;
    private ProductType[] resourceTypes = ProductType.values();
    private ProductType resourceType;
    private List<SubLap> subLapList;
    private SubLap subLap;
    private BidStatus bidStatus;

    private List<HourEndObject> capacityDetails = new ArrayList<>();
    private IsoResource heCapacityDetailsResource;
    private Integer haCapacityNumberOfLocations;
    private Integer haCapacityNumberOfLocationsWithMeterForecast;

    private List<Bid> bids = new ArrayList<>();

    private boolean renderGeneralActions = false;
    private boolean renderFilters = true;
    private boolean renderReviewPanel = true;
    private boolean cancelAction = false;
    private boolean showModal = false;

    private String password = "";
    private String cancelReason = "";

    private Bid selectedBid;
    private String action = "";

    private Map<Bid, DefaultMenuModel> actionButtons;

    private Iso iso;

    @PostConstruct
    public void init() {
        tradeDate = ParameterEncoderService.getTradeDateParameter(uiMessage);
        if (tradeDate == null) {
            tradeDate = bidService.calculateDefaultTradeDate();
        }
        TemperatureForecast forecast = temperatureForecastService.getByDate(tradeDate);
        if (forecast != null) {
            forecastedTemperature = forecast.getDegrees().doubleValue();
        }
        resourceType = ProductType.RDRR; // todo: remove this line when development is finished
        subLapList = subLapService.getAll();
        iso = ParameterEncoderService.retrieveIsoFromParameter(isoService, uiMessage);
        search();
    }

    public void search() {
        boolean isEveryBidBiddable = true;
        TemperatureForecast forecast = temperatureForecastService.getByDate(tradeDate);
        if (forecast != null) {
            forecastedTemperature = forecast.getDegrees().doubleValue();
        } else {
            forecastedTemperature = null;
        }
        bids.clear();
        LocalDateTime localTradeDate = LocalDateTime.from(tradeDate.toInstant().atZone(ZoneId.systemDefault())).toLocalDate().atStartOfDay();

        if (isTradeDateInThePast(localTradeDate)) {
            bids = bidService.searchBy(tradeDate, resourceType, subLap, bidStatus, iso);
            updateLinks();
            renderGeneralActions = false;
            return;
        }
        final String sublapStr = bundleAccessor.getPropertyResourceBundle().getString("data.mapping.sublap");
        List<RegistrationSubmissionStatus> allActive = isoRegistrationService.getBiddableRegistrations(resourceType, subLap, localTradeDate, iso);
        log.info("allActive " + allActive + " resourceType " + resourceType + " " + sublapStr + " "+ subLap);
        if (CollectionUtils.isNotEmpty(allActive)) {
            List<IsoResource> list = allActive.stream().map(RegistrationSubmissionStatus::getIsoResource).collect(Collectors.toList());
            log.info("list " + list);
            bids = bidService.searchByResourcesAndTradeDate(list, tradeDate);
            log.info("bids from database " + bids);
            List<Long> resourceIdsWithBid = bids.stream().map(bid -> bid.getIsoResource().getId()).collect(Collectors.toList());
            log.info("resourceIdsWithBid " + resourceIdsWithBid);
            if (!isTradeDateInThePast(localTradeDate)) {
                List<Bid> newBids = allActive.stream().filter(registration -> !resourceIdsWithBid.contains(registration.getIsoResource().getId()))
                        .map(reg -> new Bid(reg.getIsoResource())).collect(Collectors.toList());
                log.info("newBids " + newBids);
                for (Bid newBid : newBids) {
                    if (newBid.isBiddableOnDayOfWeek(localTradeDate.getDayOfWeek())) {
                        bids.add(bidService.getByResourceIdTradeDateCreated(newBid.getIsoResource().getId(), tradeDate));
                    } else {
                        isEveryBidBiddable = false;
                        continue;
                    }

                    if (!isModifiableBid(newBid)) {
                        isEveryBidBiddable = false;
                    }
                }

                for (Bid bid : bids) {
                    List<Event> eventsThisYear = eventService.getAllForYearInDate(bid.getSegments().stream().map(Segment::getProgram).distinct().collect(Collectors.toList()), bid.getTradeDate());
                    BidHelper.assignRisks(bid, eventsThisYear);
                    if (bid.getStatus().equals(BidStatus.AUTO_BID) || bid.getStatus().equals(BidStatus.ACTION_REQUIRED)) {
                        BidHelper.assignStatus(bid.getStatus().equals(BidStatus.OUTAGE), bid);
                    }

                    if (!bid.isBiddableOnDayOfWeek(localTradeDate.getDayOfWeek()) || !isModifiableBid(bid)) {
                        isEveryBidBiddable = false;
                    }
                }
            }
            bids.removeIf(bid -> bidStatus != null && !bid.getStatus().equals(bidStatus));

            updateActionButtons();
            updateLinks();

            renderGeneralActions = !bids.isEmpty() && !isTradeDateInThePast(localTradeDate) && isEveryBidBiddable;
        }
    }

    private boolean isTradeDateInThePast(LocalDateTime localTradeDate) {
        LocalDateTime localDateTime = LocalDateTime.now(CUSTOMER_TIMEZONE_ID).toLocalDate().atStartOfDay();
        return localTradeDate.isBefore(localDateTime);
    }

    public void clear() {
        bids.clear();
        tradeDate = bidService.calculateDefaultTradeDate();
        subLap = null;
        bidStatus = null;
        resourceType = null;
        resourceType = ProductType.RDRR; // todo: remove this line when development is finished
        renderGeneralActions = false;
    }


    public void setAction(Bid bid, String action) {
        selectedBid = bid;
        this.action = action;
        if (BidAction.valueOf(action).equals(BidAction.CANCEL) || BidAction.valueOf(action).equals(BidAction.NO_BID)) {
            cancelAction = true;
        } else {
            cancelAction = false;
        }
        showModal = true;
    }

    public void modifySchedule(Bid bid) throws IOException {
        String redirectString = "BidSchedule.xhtml?o=" + ParameterEncoderService.encode(bid.getIsoResource().getId());
        if (tradeDate != null) {
            redirectString += "&d=" + ParameterEncoderService.encode(ConstantsProvider.DATE_FORMAT.format(tradeDate));
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect(redirectString);
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void save() {
        String userName = ((User) identity.getAccount()).getLoginName();
        Credentials credentials = new UsernamePasswordCredentials(userName, new Password(password));
        identityManager.validateCredentials(credentials);
        if (!Credentials.Status.VALID.equals(credentials.getStatus())) {
            uiMessage.addMessage("Invalid Password", FacesMessage.SEVERITY_ERROR);
            return;
        }
        switch (BidAction.valueOf(action)) {
            case NO_BID:
            case CANCEL:
                if (StringUtils.isBlank(cancelReason)) {
                    uiMessage.addMessage("Cancel reason is a mandatory field fot this action", FacesMessage.SEVERITY_ERROR);
                    return;
                }
        }
        if (selectedBid != null) {
            saveBid(selectedBid);
        } else {
            if (CollectionUtils.isNotEmpty(bids)) {
                for (Bid bid : bids) {
                    switch (BidAction.valueOf(action)) {
                        case BID:
                            switch (bid.getStatus()) {
                                case ACTION_REQUIRED:
                                case AUTO_BID:
                                case NO_BID:
                                    saveBid(bid);
                            }
                            break;
                        case NO_BID:
                            switch (bid.getStatus()) {
                                case ACTION_REQUIRED:
                                case AUTO_BID:
                                    saveBid(bid);
                            }
                            break;
                        case CANCEL:
                            switch (bid.getStatus()) {
                                case ACCEPTED:
                                case EXCEPTIONS:
                                    saveBid(bid);
                            }
                            break;

                    }
                }
            }
        }
        updateActionButtons();
        updateLinks();
        password = null;
        cancelReason = null;
        showModal = false;
    }

    private void saveBid(Bid bidToSave) {
        bidToSave.setCancelReason(cancelReason);
        bidToSave.setSubmittedBy(((User) identity.getAccount()).getEmail());

        switch (BidAction.valueOf(action)) {
            case BID:
                bidToSave.setStatus(BidStatus.READY_TO_SUBMIT);
                break;
            case NO_BID:
            case CANCEL:
                bidToSave.setStatus(BidStatus.NO_BID);
                break;
        }
        bidToSave.setSubmittedTime(null);
        bidService.save(bidToSave);
    }

    public boolean isModifiableBid(Bid bid) {
        return bidService.existsHeEditable(bid);
    }

    private void updateActionButtons() {
        // dynamic submenu for Modify button
        actionButtons = new HashMap<>();

        for (Bid bid : bids) {
            DefaultMenuModel menuModel = new DefaultMenuModel();
            for (BidAction action : bid.getActions()) {
                if (action == null) {
                    menuModel.addElement(new DefaultSeparator());
                    continue;
                }
                DefaultMenuItem one = new DefaultMenuItem(action.getName());
                one.setCommand("#{controller.setAction(bid, '" + action.toString() + "')}");
                one.setUpdate("@form");
                menuModel.addElement(one);
            }

            actionButtons.put(bid, menuModel);
        }
    }

    private void updateLinks() {
        for (Bid bid : bids) {
            String defaultOrCustom = bid.isDefaultSchedule() ? "DEFAULT" : "CUSTOM";
            bid.getProperties().setScheduleType(defaultOrCustom);
        }
    }

    public void calculateCapacityDetails(Bid bid) {
        capacityDetails.clear();
        capacityDetails.add(new RiskCalculator().validateCapacity(bid));

        heCapacityDetailsResource = bid.getIsoResource();

        MeterForecast group = new MeterForecast();

        List<String> saIds = bid.getRegistration().getLocations().stream()
                .map(l -> l.getProgramServiceAgreementEnrollment().getServiceAgreement().getServiceAgreementId())
                .collect(Collectors.toList());

        haCapacityNumberOfLocations = saIds.size();

        if (saIds.isEmpty()) {
            return;
        }

        Segment segment = bid.getSegments().get(0);
        for (int i = 1; i <= 24; i++) {
            group.setHour(i, ((Long) group.getHourEnd(i)) + segment.getForecastedCapacityHe(i));
        }

        haCapacityNumberOfLocationsWithMeterForecast = bid.getRegistration().getLocations().size();

        capacityDetails.add(group);
    }

}