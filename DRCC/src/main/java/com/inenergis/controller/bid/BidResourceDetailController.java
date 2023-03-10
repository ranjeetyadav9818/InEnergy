package com.inenergis.controller.bid;

import com.inenergis.controller.converter.MoneyCentsConverter;
import com.inenergis.controller.converter.MoneyConverter;
import com.inenergis.controller.general.ConstantsProvider;
import com.inenergis.entity.bidding.Bid;
import com.inenergis.entity.bidding.Segment;
import com.inenergis.entity.genericEnum.BidStatus;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.program.HourEnd;
import com.inenergis.model.TransposedStatus;
import com.inenergis.service.BidService;
import com.inenergis.service.CurrencyConfigService;
import com.inenergis.service.IsoResourceService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named
@ViewScoped
@Getter
@Setter
public class BidResourceDetailController implements Serializable {

    @Inject
    EntityManager entityManager;
    @Inject
    UIMessage uiMessage;
    @Inject
    BidService bidService;

    @Inject
    IsoResourceService isoResourceService;

    List<Segment> currentDaySegments;
    List<Segment> recentBidSegments;
    List<BidScheduleAdapter> currentDayRows;
    List<BidScheduleAdapter> recentBidRows;

    Date recentBidDate;
    Date tradeDate;
    boolean existsHeEditable = false;

    @Inject
    protected CurrencyConfigService currencyConfigService;

    private MoneyConverter moneyConverter;
    private MoneyCentsConverter moneyCentsConverter;

    public String getTradeDateFormatted() {
        if (tradeDate != null) {
            return ConstantsProvider.DATE_FORMAT.format(tradeDate);
        }
        return StringUtils.EMPTY;
    }

    Logger log = LoggerFactory.getLogger(BidResourceDetailController.class);
    Long resourceId;
    Bid bid;
    Bid recentBid;

    @PostConstruct
    public void init() {
        getResourceIdParameter();
        tradeDate = ParameterEncoderService.getTradeDateParameter(uiMessage);
        moneyConverter = new MoneyConverter();
        moneyConverter.setCurrencyConfigService(currencyConfigService);
        moneyCentsConverter = new MoneyCentsConverter();
        moneyCentsConverter.setCurrencyConfigService(currencyConfigService);
        if (resourceId != null && tradeDate != null) {
            buildBid();
            recentBidDate = bidService.getSameDayFromPreviousWeek(tradeDate);
            buildRecentBid(recentBidDate);
        }
        existsHeEditable = existsHeEditable();
    }

    private void buildBid() {
        bid = bidService.getByResourceIdTradeDateCreated(resourceId, tradeDate);
        currentDaySegments = bid.getSegments();
        currentDayRows = new BidScheduleAdapter().build(currentDaySegments, bidService, true, moneyConverter, moneyCentsConverter);
    }

    private void buildRecentBid(Date date) {
        recentBid = bidService.getRecentBidResource(resourceId, date);
        if (recentBid != null) {
            recentBidSegments = recentBid.getSegments();
        } else {
            recentBidSegments = new ArrayList<>();
        }
        recentBidRows = new BidScheduleAdapter().build(recentBidSegments, bidService, false, moneyConverter, moneyCentsConverter);
    }


    private Long getResourceIdParameter() {
        resourceId = ParameterEncoderService.getDefaultDecodedParameterAsLong();
        if (resourceId == null) {
            uiMessage.addMessage("Resource Id required");
        } else {
            IsoResource resource = isoResourceService.getById(resourceId);
            if (resource == null) {
                uiMessage.addMessage("Resource Id not found");
            }
        }
        return resourceId;
    }

    public void onSegmentDrop(DragDropEvent ddEvent) {
        BidScheduleAdapter source = ((BidScheduleAdapter) ddEvent.getData());
        for (BidScheduleAdapter currentDayRow : currentDayRows) {
            if (source.sameTypeAndName(currentDayRow)) {
                List<HourEnd> hourEndsSkipped = validateHourEnds(source, currentDayRow, true, false);
                if (hourEndsSkipped.isEmpty()) {
                    for (int i = 0; i < currentDayRow.getHourEnds().size(); i++) {
                        if (bidService.isHeEditable(bid, i + 1)) {
                            currentDayRow.getHourEnds().set(i, source.getHourEnds().get(i));
                        }
                    }
                    currentDayRow.updateSegment();
                    saveSegment(currentDayRow);
                } else {
                    uiMessage.addMessage("Drop failed. This segment does not contain editable rows: ", FacesMessage.SEVERITY_ERROR);
                }
                return;
            }
        }
    }

    public void onRowEdit(RowEditEvent event) {
        //TODO to let the validations occur the cell editor should have been rendered and the rows become uneditable after that.
        BidScheduleAdapter adapter = (BidScheduleAdapter) event.getObject();
        for (BidScheduleAdapter currentDayRow : currentDayRows) {
            if (adapter.sameTypeAndName(currentDayRow)) {
                if (existsHeEditable()) {
                    adapter.updateSegment();
                    saveSegment(adapter);
                    return;
                } else {
                    uiMessage.addMessage("Edit failed. This segment does not contain editable rows: ", FacesMessage.SEVERITY_ERROR);
                }
            }
        }
    }

    private List<HourEnd> validateHourEnds(BidScheduleAdapter newRow, BidScheduleAdapter currentRow, boolean stopAtFirstInvalid, boolean compareValues) {
        List<HourEnd> hourEndsSkipped = new ArrayList<>();
        for (HourEnd hourEnd : HourEnd.values()) {
            Segment segment = currentRow.getMappedSegment();
            int index = hourEnd.getHourNumber() - 1;
            long newNumber = newRow.hourEnds.get(index);
            BidScheduleAdapter.BidType type = newRow.getType();
            long oldNumberSegment = BidScheduleAdapter.BidType.CAPACITY.equals(type) ? segment.getCapacitiesAsList().get(index) :
                    segment.getPricesAsList().get(index);
            if (compareValues) {
                if (oldNumberSegment != newNumber && !newRow.heIsBiddable(hourEnd.getHourNumber()))
                    hourEndsSkipped.add(hourEnd);
                if (stopAtFirstInvalid) {
                    return hourEndsSkipped;
                }
            } else if (!newRow.heIsBiddable(hourEnd.getHourNumber())) {
                hourEndsSkipped.add(hourEnd);
                if (stopAtFirstInvalid) {
                    return hourEndsSkipped;
                }
            }
        }
        return hourEndsSkipped;
    }

    private void saveSegment(BidScheduleAdapter adapter) {
        Segment segment = adapter.getMappedSegment();
        segment.getBid().setScheduleModified(true);
        segment.getBid().setDefaultSchedule(false);
        bidService.save(segment.getBid());
    }

    public void handleDateSelect(SelectEvent event) {
        Date newSearchDate = (Date) event.getObject();
        buildRecentBid(newSearchDate);
    }

    public void onRowCancel(RowEditEvent event) {
    }

    public void onBreadCrumbClick() throws IOException {
        String redirectString = "Bid.xhtml?";
        if (tradeDate != null) {
            redirectString += "&d=" + ParameterEncoderService.encode(ConstantsProvider.DATE_FORMAT.format(tradeDate));
        }
        if (bid != null && bid.getIsoResource() != null) {
            redirectString += "&o=" + ParameterEncoderService.encode(bid.getIsoResource().getIsoProduct().getProfile().getIso().getId());
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect(redirectString);
        FacesContext.getCurrentInstance().responseComplete();
    }

    private boolean existsHeEditable() {
        return bidService.existsHeEditable(bid);
    }

    public List<TransposedStatus> getTransposedStatuses() {
        List<TransposedStatus> result = new ArrayList<>();
        for (Segment currentDaySegment : currentDaySegments) {
            for (int i = 0; i < 24; i++) {
                TransposedStatus transposedStatus = new TransposedStatus();
                transposedStatus.setSegment(currentDaySegment.getName());
                transposedStatus.setHe(i + 1);
                transposedStatus.setStatus(currentDaySegment.getStatusesHe().get(i));
                transposedStatus.setDescription(currentDaySegment.getIsoMessagesHe().get(i));
                result.add(transposedStatus);
            }
        }
        return result;
    }

    public void resetBid() {
        Bid bid = bidService.createBid(this.bid.getIsoResource().getId(), this.bid.getTradeDate());
        bid.setId(this.bid.getId());
        bid.setLastReset(new Date());
        bidService.save(bid);
        buildBid();
    }

    public boolean isBidResettable() {
        boolean correctStatus = bid.getStatus().equals(BidStatus.AUTO_BID) || bid.getStatus().equals(BidStatus.ACTION_REQUIRED);
        return correctStatus && bidService.allHesEditable(bid);
    }
}