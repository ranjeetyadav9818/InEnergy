package com.inenergis.service;

import com.inenergis.dao.IsoDao;
import com.inenergis.dao.IsoProfileDao;
import com.inenergis.entity.genericEnum.BidSubmissionIsoInterval;
import com.inenergis.entity.genericEnum.BidSubmissionTradeTime;
import com.inenergis.entity.genericEnum.BidSubmissionTradeTimeHours;
import com.inenergis.entity.marketIntegration.Iso;
import com.inenergis.entity.marketIntegration.IsoProduct;
import com.inenergis.entity.marketIntegration.IsoProfile;
import com.inenergis.model.ElasticISO;
import com.inenergis.model.ElasticIsoConverter;
import com.inenergis.util.ElasticActionsUtil;
import com.inenergis.util.ElasticConnectionPool;
import com.inenergis.util.UIMessage;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static com.inenergis.util.ElasticActionsUtil.ENERGY_ARRAY_INDEX;

@Stateless
public class IsoService {

    public static final String BID_SUBMISSION_DUE_TO_ISO_AND_ITS_INTERVAL_ARE_MANDATORY = "Bid Submission Due To Market and its interval are mandatory";
    public static final String BID_SUBMISSION_DUE_TO_ISO_IF_A_FIXED_MINUTES_INTERVAL_IS_SELECTED_TRADE_HOUR_MUST_BE_SELECTED_AS_WELL = "Bid Submission Due To Iso: If a fixed minutes interval is selected, Trade Hour must be selected as well";
    public static final String IF_AUTO_BID_LOW_RISK_IS_SELECTED_LOW_RISK_SUBMISSION_TIME_AND_ITS_INTERVAL_ARE_MANDATORY = "If Auto Bid Low Risk is selected, Low Risk Submission Time and its interval are mandatory";
    public static final String IF_AUTO_BID_LOW_RISK_IS_NOT_SELECTED_BID_SUBMISSION_DUE_TO_SC_AND_ITS_INTERVAL_ARE_MANDATORY = "If Auto Bid Low Risk is not selected, Bid Submission due to SC and its interval are mandatory";
    public static final String AT_LEAST_ONE_DAY_SHOULD_BE_SELECTED_FOR_BID = "At least one day should be selected for bid";
    public static final String AUTO_BID_LOW_RISK_OR_SC_CUTTING_WINDOWS_CAN_NOT_TAKE_PLACE_BEFORE_ISO_CUTTING_WINDOW = "Auto Bid Low Risk or SC cutting windows can not take place before Market cutting window";
    @Inject
    IsoDao isoDao;

    @Inject
    IsoProfileDao isoProfileDao;

    @Inject
    ElasticConnectionPool elasticConnectionPool;

    @Inject
    ElasticActionsUtil elasticActionsUtil;

    @Transactional
    public void saveOrUpdateIso(Iso iso) throws IOException {
        isoDao.saveOrUpdate(iso);
//        elasticActionsUtil.indexDocument(iso.getId().toString(), ElasticIsoConverter.convert(iso), ENERGY_ARRAY_INDEX, ElasticISO.ELASTIC_TYPE);
    }

    @Transactional
    public void saveOrUpdateProfile(IsoProfile profile) throws IOException {
        //This should be called automatically but it is not;
        profile.onUpdate();
        profile = isoProfileDao.saveOrUpdate(profile);
        profile.getIso().getProfiles().remove(profile);
        profile.getIso().getProfiles().add(profile);
//        elasticActionsUtil.indexDocument(profile.getIso().getId().toString(), ElasticIsoConverter.convert(profile.getIso()), ENERGY_ARRAY_INDEX, ElasticISO.ELASTIC_TYPE);
    }

    public List<Iso> getIsos() {
        return isoDao.getAll();
    }

    public Iso getIso(Long isoId) {
        return isoDao.getById(isoId);
    }

    public IsoProfile getProfile(Long id) {
        return isoProfileDao.getById(id);
    }

    public boolean validateProfile(IsoProfile isoProfile, UIMessage uiMessage) {
        boolean result = true;
        for (IsoProduct isoProduct : isoProfile.getProducts()) {
            result &= validateProduct(isoProduct, uiMessage);
        }
        return result;
    }

    private boolean validateProduct(IsoProduct isoProduct, UIMessage uiMessage) {
        boolean result = true;
        LocalTime cuttingWindowNoIso = null;
        BidSubmissionTradeTime intervalNoIso = null;
        if (isoProduct.getBidSubmissionIsoMinute() != null && isoProduct.getBidSubmissionIsoOn() != null) {
            if ((BidSubmissionIsoInterval.HourBid_75.getMinutes() == isoProduct.getBidSubmissionIsoMinute()
                            && !BidSubmissionTradeTimeHours.TRADE_HOUR.equals(isoProduct.getBidSubmissionIsoOn()))
                            || (BidSubmissionIsoInterval.HourBid_75.getMinutes() != isoProduct.getBidSubmissionIsoMinute()
                            && BidSubmissionTradeTimeHours.TRADE_HOUR.equals(isoProduct.getBidSubmissionIsoOn()))) {
                uiMessage.addMessage(BID_SUBMISSION_DUE_TO_ISO_IF_A_FIXED_MINUTES_INTERVAL_IS_SELECTED_TRADE_HOUR_MUST_BE_SELECTED_AS_WELL, FacesMessage.SEVERITY_ERROR);
                result = false;
            }
        } else {
            if (isoProduct.getBidSubmissionIsoMinute() != null && isoProduct.getBidSubmissionIsoOn() == null) {
                uiMessage.addMessage(BID_SUBMISSION_DUE_TO_ISO_AND_ITS_INTERVAL_ARE_MANDATORY, FacesMessage.SEVERITY_ERROR);
                result = false;
            } else if (isoProduct.getBidSubmissionIsoMinute() == null && isoProduct.getBidSubmissionIsoOn() != null) {
                uiMessage.addMessage(BID_SUBMISSION_DUE_TO_ISO_AND_ITS_INTERVAL_ARE_MANDATORY, FacesMessage.SEVERITY_ERROR);
                result = false;
            } else if (isoProduct.getBidSubmissionIsoMinute() == null || isoProduct.getBidSubmissionIsoOn() == null){
                uiMessage.addMessage(BID_SUBMISSION_DUE_TO_ISO_AND_ITS_INTERVAL_ARE_MANDATORY, FacesMessage.SEVERITY_ERROR);
                result = false;
            }
        }
        if (isoProduct.isAutoBidLowRisk()) {
            if (isoProduct.getAutoBidLowRiskHour() == null || isoProduct.getAutoBidLowRiskMinute() == null || isoProduct.getAutoBidLowRiskOn() == null) {
                uiMessage.addMessage(IF_AUTO_BID_LOW_RISK_IS_SELECTED_LOW_RISK_SUBMISSION_TIME_AND_ITS_INTERVAL_ARE_MANDATORY, FacesMessage.SEVERITY_ERROR);
                result = false;
            } else {
                cuttingWindowNoIso = isoProduct.getAutoBidLowRiskLT();
                intervalNoIso = isoProduct.getAutoBidLowRiskOn();
            }
        } else {
            if (isoProduct.getBidSubmissionScHour() == null || isoProduct.getBidSubmissionScMinute() == null || isoProduct.getBidSubmissionScOn() == null) {
                uiMessage.addMessage(IF_AUTO_BID_LOW_RISK_IS_NOT_SELECTED_BID_SUBMISSION_DUE_TO_SC_AND_ITS_INTERVAL_ARE_MANDATORY, FacesMessage.SEVERITY_ERROR);
                result = false;
            } else {
                cuttingWindowNoIso = isoProduct.getBidSubmissionSc();
                intervalNoIso = isoProduct.getBidSubmissionScOn();
            }
        }
        boolean oneDaySelected = isoProduct.isBidOnMonday() || isoProduct.isBidOnTuesday() || isoProduct.isBidOnWednesday() || isoProduct.isBidOnThursday() || isoProduct.isBidOnFriday()
                || isoProduct.isBidOnSaturday() || isoProduct.isBidOnSunday();
        if (!oneDaySelected) {
            uiMessage.addMessage(AT_LEAST_ONE_DAY_SHOULD_BE_SELECTED_FOR_BID, FacesMessage.SEVERITY_ERROR);
            result = false;
        }
        if (BidSubmissionIsoInterval.HourBid_75.getMinutes() != isoProduct.getBidSubmissionIsoMinute()) {
            if (cuttingWindowNoIso != null && isoProduct.getBidSubmissionIso() != null) {
                if (compareCuttingWindow(cuttingWindowNoIso, intervalNoIso, isoProduct.getBidSubmissionIso(), isoProduct.getBidSubmissionIsoOn()) == 1) {
                    uiMessage.addMessage(AUTO_BID_LOW_RISK_OR_SC_CUTTING_WINDOWS_CAN_NOT_TAKE_PLACE_BEFORE_ISO_CUTTING_WINDOW, FacesMessage.SEVERITY_ERROR);
                    result = false;
                }
            }
        }
        return result;

    }

    private int compareCuttingWindow(LocalTime cuttingWindowNoIso, BidSubmissionTradeTime tradeTimeNoIso, LocalTime bidSubmissionIso, BidSubmissionTradeTimeHours tradeTimeIso) {
        int intervalNoIso = -1;
        if (BidSubmissionTradeTime.TRADE_DAY.equals(tradeTimeNoIso)) {
            intervalNoIso = 0;
        } else if (BidSubmissionTradeTime.TRADE_DAY_MINUS_ONE.equals(tradeTimeNoIso)) {
            intervalNoIso = -1;
        }
        int intervalIso = -1;
        if (BidSubmissionTradeTimeHours.TRADE_DAY.equals(tradeTimeIso)) {
            intervalIso = 0;
        } else if (BidSubmissionTradeTimeHours.TRADE_DAY_MINUS_ONE.equals(tradeTimeIso)) {
            intervalIso = -1;
        }
        int result = Integer.compare(intervalNoIso, intervalIso);
        if (result == 0) {
            return cuttingWindowNoIso.compareTo(bidSubmissionIso);
        }
        return result;
    }

}