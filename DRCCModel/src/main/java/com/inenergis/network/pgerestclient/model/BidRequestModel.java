package com.inenergis.network.pgerestclient.model;

import com.google.gson.annotations.SerializedName;
import com.inenergis.entity.bidding.Bid;
import com.inenergis.entity.bidding.BidHelper;
import com.inenergis.entity.bidding.Segment;
import com.inenergis.entity.genericEnum.BidSubmissionTradeTimeHours;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import com.inenergis.entity.marketIntegration.IsoProduct;
import com.inenergis.util.ConstantsProviderModel;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class BidRequestModel extends RequestModel {

    private final static String DEFAULT_PRODUCT = "ENGY";
    private final static String DEFAULT_MARKET = "RTM";
    private final static String URL_TOKEN = "pge.api.bid.url";

    @SerializedName("BidPackages")
    Map<String, List<BidPackage>> bidPackages = new HashMap<>();

    @SuppressWarnings("unused")
    private class BidPackage {
        private String resourceId;
        private String bidDate;
        private String market;
        private String product;
        private Integer he;
        private BigDecimal mw;
        private BigDecimal price;
        private String segment;
    }

    public BidRequestModel(Bid bid, String tradeDateString) throws IOException {
        urlToken = URL_TOKEN;

        List<BidPackage> bidPackageList = new ArrayList<>();

        for (Segment segment : bid.getSegments()) {
            for (int i = 1; i <= 24; i++) {
                if (segment.getPriceHe(i) != null && heIsStillSendable(i, bid)) {
                    BidPackage bidPackage = new BidPackage();
                    bidPackage.resourceId = bid.getIsoResource().getName();
                    bidPackage.bidDate = tradeDateString;
                    bidPackage.market = DEFAULT_MARKET;
                    bidPackage.product = DEFAULT_PRODUCT;
                    bidPackage.he = i;
                    bidPackage.mw = new BigDecimal(segment.getCapacityHe(i)).divide(new BigDecimal(ConstantsProviderModel.MW_PRECISION), 3, BigDecimal.ROUND_CEILING);
                    bidPackage.price = new BigDecimal(segment.getPriceHe(i)).divide(ConstantsProviderModel.ONE_HUNDRED_BIG_DECIMAL, 2, BigDecimal.ROUND_CEILING);
                    bidPackage.segment = segment.getName().replace(BidHelper.SEGMENT_NAME, "");

                    bidPackageList.add(bidPackage);
                }
            }
        }

        bidPackages.put("bidPackage", bidPackageList);
    }

    private boolean heIsStillSendable(int he, Bid bid) {
        LocalDate today = LocalDate.now(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID);
        if (today.equals(bid.getTradeLocalDate())) {
            IsoProduct isoProduct = bid.getIsoResource().getIsoProduct();
            if (isoProduct.getBidSubmissionIsoOn().equals(BidSubmissionTradeTimeHours.TRADE_HOUR)) {
                if (he <= (isoProduct.getBidSubmissionIsoMinute() / 60) + 1) {
                    return false;
                }
                LocalTime heLocalTime = LocalTime.of(he - 1, 0);
                LocalTime now = LocalTime.now(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID);
                return now.isBefore(heLocalTime.minusMinutes(isoProduct.getBidSubmissionIsoMinute()));
            }
        }
        return true;
    }

    public static void main(String... args) {
        Bid bid = new Bid();
        bid.setTradeDate(new Date());
        RegistrationSubmissionStatus registrationSubmissionStatus = new RegistrationSubmissionStatus();
        IsoResource isoResource = new IsoResource();
        IsoProduct isoProduct = new IsoProduct();
        isoProduct.setBidSubmissionIsoOn(BidSubmissionTradeTimeHours.TRADE_HOUR);
        isoProduct.setBidSubmissionIsoMinute(61);
        isoResource.setIsoProduct(isoProduct);
        registrationSubmissionStatus.setIsoResource(isoResource);
        bid.setIsoResource(isoResource);
        bid.setSegments(new ArrayList<>());

        try {
            (new BidRequestModel(bid, "")).heIsStillSendable(5, bid);
            for (int i = 1; i < 25; i++) {
                boolean b = (new BidRequestModel(bid, "")).heIsStillSendable(i, bid);
                System.out.println(i + ": " + b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}