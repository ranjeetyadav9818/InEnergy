package com.inenergis.controller.bid;


import com.inenergis.controller.converter.BidCapacityInMWConverter;
import com.inenergis.controller.converter.LongBigDecimalConverter;
import com.inenergis.controller.converter.MoneyCentsConverter;
import com.inenergis.controller.converter.MoneyConverter;
import com.inenergis.controller.converter.PercentageConverter;
import com.inenergis.entity.bidding.Segment;
import com.inenergis.entity.program.HourEnd;
import com.inenergis.service.BidService;
import lombok.Data;

import javax.faces.convert.Converter;
import java.util.ArrayList;
import java.util.List;

@Data
public class BidScheduleAdapter {

    public static final int CAPACITY_NUM_DECIMALS = 3;
    public static final int DEFAULT_NUM_DECIMALS = 2;
    boolean editableType;

    String schedule;

    List<Long> hourEnds;

    Segment mappedSegment;

    BidService bidService;

    Converter converter;
    Converter converterForEdit;

    public boolean sameTypeAndName(BidScheduleAdapter other) {
        if (this.getType().equals(other.getType()) && this.getMappedSegment().getName().equals(other.getMappedSegment().getName())) {
            return true;
        }
        return false;
    }

    public enum BidType {
        PRICE, CAPACITY, OTHER
    }

    BidType type = BidType.OTHER;

    private static final String DEFAULT_STYLE = "font-size: 11px; padding-left: 3px;";
    private static final String ACTIVE_STYLE = DEFAULT_STYLE + " background-color: lightyellow;";
    private static final String INACTIVE_STYLE = DEFAULT_STYLE;

    private int numDecimals;

    public String getStyle(long i) {
        if (heIsHighlighted(i)) {
            return ACTIVE_STYLE;
        }
        return INACTIVE_STYLE;
    }

    public String getDefaultStyle() {
        return INACTIVE_STYLE;
    }

    private static BidCapacityInMWConverter bidCapacityInMWConverter = new BidCapacityInMWConverter();
    private static PercentageConverter percentageConverter = new PercentageConverter();
    private static LongBigDecimalConverter longBigDecimalConverter = new LongBigDecimalConverter();

    public boolean heIsBiddable(long i) {
        return bidService.isHeEditable(mappedSegment.getBid(), (int) i);
    }

    public boolean heIsHighlighted(long i) {
        int from = 0;
        int to = 0;
        int header = (int) i;

        HourEnd defaultBidHoursFrom = mappedSegment.getProgram().getActiveProfile().getDefaultBidHoursFrom();
        if (defaultBidHoursFrom != null) {
            from = defaultBidHoursFrom.getHourNumber();
        }
        HourEnd defaultBidHoursTo = mappedSegment.getProgram().getActiveProfile().getDefaultBidHoursTo();
        if (defaultBidHoursTo != null) {
            to = defaultBidHoursTo.getHourNumber();
        }
        return header <= to && header >= from;
    }


    public static List<BidScheduleAdapter> build(Segment mappedSegment, BidService bidService, boolean currentDayBid, MoneyConverter moneyConverter, MoneyCentsConverter moneyCentsConverter) {

        ArrayList<BidScheduleAdapter> result = new ArrayList<>();
        if (currentDayBid) {
            BidScheduleAdapter forecastedCapacityRow = new BidScheduleAdapter();
            forecastedCapacityRow.setSchedule(mappedSegment.getName() + ": Forecasted Capacity (MW)");
            forecastedCapacityRow.setEditableType(false);
            forecastedCapacityRow.setHourEnds(mappedSegment.getForecastedCapacitiessAsList());
            forecastedCapacityRow.setMappedSegment(mappedSegment);
            forecastedCapacityRow.setBidService(bidService);
            forecastedCapacityRow.setConverter(bidCapacityInMWConverter);
            forecastedCapacityRow.setConverterForEdit(bidCapacityInMWConverter);
            forecastedCapacityRow.setNumDecimals(CAPACITY_NUM_DECIMALS);
            result.add(forecastedCapacityRow);

            BidScheduleAdapter safetyFactorRow = new BidScheduleAdapter();
            safetyFactorRow.setSchedule(mappedSegment.getName() + ": SafetyFactors (%)");
            safetyFactorRow.setEditableType(false);
            safetyFactorRow.setHourEnds(mappedSegment.getSafetyFactorsAsList());
            safetyFactorRow.setMappedSegment(mappedSegment);
            safetyFactorRow.setBidService(bidService);
            safetyFactorRow.setConverter(percentageConverter);
            safetyFactorRow.setConverterForEdit(percentageConverter);
            safetyFactorRow.setNumDecimals(DEFAULT_NUM_DECIMALS);
            result.add(safetyFactorRow);
        }
        BidScheduleAdapter bidCapacityRow = new BidScheduleAdapter();
        bidCapacityRow.setSchedule(mappedSegment.getName() + ": Bid Capacity (MW)");
        bidCapacityRow.setEditableType(true);
        bidCapacityRow.setHourEnds(mappedSegment.getCapacitiesAsList());
        bidCapacityRow.setMappedSegment(mappedSegment);
        bidCapacityRow.setType(BidType.CAPACITY);
        bidCapacityRow.setBidService(bidService);
        bidCapacityRow.setConverter(bidCapacityInMWConverter);
        bidCapacityRow.setConverterForEdit(bidCapacityInMWConverter);
        bidCapacityRow.setNumDecimals(CAPACITY_NUM_DECIMALS);
        result.add(bidCapacityRow);

        BidScheduleAdapter priceRow = new BidScheduleAdapter();
        priceRow.setSchedule(mappedSegment.getName() + ": Price ($/MWH)");
        priceRow.setEditableType(true);
        priceRow.setHourEnds(mappedSegment.getPricesAsList());
        priceRow.setMappedSegment(mappedSegment);
        priceRow.setType(BidType.PRICE);
        priceRow.setBidService(bidService);
        priceRow.setConverter(moneyConverter);
        priceRow.setConverterForEdit(moneyCentsConverter);
        priceRow.setNumDecimals(DEFAULT_NUM_DECIMALS);
        result.add(priceRow);

        return result;
    }

    public List<BidScheduleAdapter> build(List<Segment> mappedSegments, BidService bidService, boolean currentDayBid, MoneyConverter moneyConverter, MoneyCentsConverter moneyCentsConverter) {
        ArrayList<BidScheduleAdapter> result = new ArrayList<>();
        boolean isSegmentEditable = false;
        for (Segment mappedSegment : mappedSegments) {
            mappedSegment.setIsEditable(isSegmentEditable);
            result.addAll(build(mappedSegment, bidService, currentDayBid,moneyConverter,moneyCentsConverter));
            isSegmentEditable = true;
        }
        return result;
    }

    public void updateSegment() {
        if (BidType.CAPACITY.equals(getType())) {
            updateCapacities();
        } else if (BidType.PRICE.equals(getType())) {
            updatePrices();
        }
    }

    private void updatePrices() {
        if (bidService.isHeEditable(mappedSegment.getBid(), 1)) {
            mappedSegment.setPriceHe1(hourEnds.get(0));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 2)) {
            mappedSegment.setPriceHe2(hourEnds.get(1));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 3)) {
            mappedSegment.setPriceHe3(hourEnds.get(2));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 4)) {
            mappedSegment.setPriceHe4(hourEnds.get(3));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 5)) {
            mappedSegment.setPriceHe5(hourEnds.get(4));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 6)) {
            mappedSegment.setPriceHe6(hourEnds.get(5));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 7)) {
            mappedSegment.setPriceHe7(hourEnds.get(6));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 8)) {
            mappedSegment.setPriceHe8(hourEnds.get(7));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 9)) {
            mappedSegment.setPriceHe9(hourEnds.get(8));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 10)) {
            mappedSegment.setPriceHe10(hourEnds.get(9));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 11)) {
            mappedSegment.setPriceHe11(hourEnds.get(10));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 12)) {
            mappedSegment.setPriceHe12(hourEnds.get(11));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 13)) {
            mappedSegment.setPriceHe13(hourEnds.get(12));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 14)) {
            mappedSegment.setPriceHe14(hourEnds.get(13));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 15)) {
            mappedSegment.setPriceHe15(hourEnds.get(14));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 16)) {
            mappedSegment.setPriceHe16(hourEnds.get(15));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 17)) {
            mappedSegment.setPriceHe17(hourEnds.get(16));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 18)) {
            mappedSegment.setPriceHe18(hourEnds.get(17));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 19)) {
            mappedSegment.setPriceHe19(hourEnds.get(18));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 20)) {
            mappedSegment.setPriceHe20(hourEnds.get(19));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 21)) {
            mappedSegment.setPriceHe21(hourEnds.get(20));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 22)) {
            mappedSegment.setPriceHe22(hourEnds.get(21));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 23)) {
            mappedSegment.setPriceHe23(hourEnds.get(22));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 24)) {
            mappedSegment.setPriceHe24(hourEnds.get(23));
        }
    }

    private void updateCapacities() {
        if (bidService.isHeEditable(mappedSegment.getBid(), 1)) {
            mappedSegment.setCapacityHe1(hourEnds.get(0));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 2)) {
            mappedSegment.setCapacityHe2(hourEnds.get(1));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 3)) {
            mappedSegment.setCapacityHe3(hourEnds.get(2));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 4)) {
            mappedSegment.setCapacityHe4(hourEnds.get(3));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 5)) {
            mappedSegment.setCapacityHe5(hourEnds.get(4));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 6)) {
            mappedSegment.setCapacityHe6(hourEnds.get(5));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 7)) {
            mappedSegment.setCapacityHe7(hourEnds.get(6));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 8)) {
            mappedSegment.setCapacityHe8(hourEnds.get(7));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 9)) {
            mappedSegment.setCapacityHe9(hourEnds.get(8));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 10)) {
            mappedSegment.setCapacityHe10(hourEnds.get(9));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 11)) {
            mappedSegment.setCapacityHe11(hourEnds.get(10));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 12)) {
            mappedSegment.setCapacityHe12(hourEnds.get(11));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 13)) {
            mappedSegment.setCapacityHe13(hourEnds.get(12));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 14)) {
            mappedSegment.setCapacityHe14(hourEnds.get(13));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 15)) {
            mappedSegment.setCapacityHe15(hourEnds.get(14));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 16)) {
            mappedSegment.setCapacityHe16(hourEnds.get(15));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 17)) {
            mappedSegment.setCapacityHe17(hourEnds.get(16));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 18)) {
            mappedSegment.setCapacityHe18(hourEnds.get(17));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 19)) {
            mappedSegment.setCapacityHe19(hourEnds.get(18));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 20)) {
            mappedSegment.setCapacityHe20(hourEnds.get(19));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 21)) {
            mappedSegment.setCapacityHe21(hourEnds.get(20));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 22)) {
            mappedSegment.setCapacityHe22(hourEnds.get(21));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 23)) {
            mappedSegment.setCapacityHe23(hourEnds.get(22));
        }
        if (bidService.isHeEditable(mappedSegment.getBid(), 24)) {
            mappedSegment.setCapacityHe24(hourEnds.get(23));
        }
    }
}