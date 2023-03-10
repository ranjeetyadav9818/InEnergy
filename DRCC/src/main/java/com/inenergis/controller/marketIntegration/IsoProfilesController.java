package com.inenergis.controller.marketIntegration;

import com.inenergis.entity.DRCCBeanUtils;
import com.inenergis.entity.bidding.RiskCondition;
import com.inenergis.entity.genericEnum.RiskCategory;
import com.inenergis.entity.genericEnum.RiskSource;
import com.inenergis.entity.genericEnum.RiskTarget;
import com.inenergis.entity.genericEnum.ElectricalUnit;
import com.inenergis.entity.genericEnum.IsoApplicableContractEnum;
import com.inenergis.entity.History;
import com.inenergis.entity.genericEnum.MarketType;
import com.inenergis.entity.genericEnum.MeterIntervalType;
import com.inenergis.entity.ProductType;
import com.inenergis.entity.genericEnum.RelationalOperator;
import com.inenergis.entity.marketIntegration.Iso;
import com.inenergis.entity.marketIntegration.IsoHoliday;
import com.inenergis.entity.marketIntegration.IsoProduct;
import com.inenergis.entity.marketIntegration.IsoProfile;
import com.inenergis.entity.program.*;
import com.inenergis.service.HistoryService;
import com.inenergis.service.IsoService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.omnifaces.util.Messages;
import org.picketlink.Identity;
import org.picketlink.idm.model.basic.User;
import org.primefaces.model.DualListModel;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

@Named
@ViewScoped
@Getter
@Setter
public class IsoProfilesController implements Serializable {

    @Inject
    IsoService isoService;
    @Inject
    HistoryService historyService;
    @Inject
    UIMessage uiMessage;
    @Inject
    Identity identity;

    Logger log = LoggerFactory.getLogger(IsoProfilesController.class);

    private Iso iso;

    private IsoProfile profile;
    private boolean newProfile = false;

    private Map<String, DualListModel<Program>> programPickListModel = new HashMap<>();
    private  IsoApplicableContractEnum[] isoApplicableContractEnums= IsoApplicableContractEnum.values();

    private ProductType[] productTypes = ProductType.values();
    private MarketType[] marketTypes = MarketType.values();
    private ElectricalUnit[] electricalUnits = ElectricalUnit.values();
    private MeterIntervalType[] meterIntervalTypes = MeterIntervalType.values();
    private RiskCategory[] riskCategories = RiskCategory.values();
    private RiskSource[] riskSources = RiskSource.values();
    private RiskTarget[] riskTargets = RiskTarget.values();
    private RelationalOperator[] relationalOperators = RelationalOperator.values();

    @PostConstruct
    public void init() {
        iso = isoService.getIso(Long.valueOf(ParameterEncoderService.getDefaultDecodedParameterAsLong()));
    }

    /**
     * We just want to save these 3 fields, if we want to save it all saveWholeProfile would have been called
     */
    @Transactional
    public void saveProfile() {
        if (profile.getId() != null) {
            IsoProfile profileFromDB = isoService.getProfile(profile.getId());
            profileFromDB.setName(profile.getName());
            profileFromDB.setEffectiveStartDate(profile.getEffectiveStartDate());
            profileFromDB.setEffectiveEndDate(profile.getEffectiveEndDate());
            profile = profileFromDB;
        }
        saveProfileAndClear();
    }

    @Transactional
    public void saveWholeProfile() {

        IsoProfile profileFromDB;
        if (profile.getProducts() != null) {

            for (IsoProduct isoProduct : profile.getProducts()) {
                isoProduct.onCreate();
            }
        }
        if (! isoService.validateProfile(profile,uiMessage)){
            return;
        }
        if (profile.isMarketEligible()) {

            profileFromDB = isoService.getProfile(profile.getId());
            if (!profileFromDB.isMarketEligible()) {
                profile.setMarketEligibleDate(new Date());
            }
        }
        try {

            isoService.saveOrUpdateIso(iso);
            saveProfileAndClear();
        } catch (IOException e) {
            uiMessage.addMessage("Error trying to save the ISO, please try again later and contact your administrator if you keep having this problem");
            log.warn("Error saving a Market", e);
        }
    }

    private void saveProfileAndClear() {
        if (profile.getId() != null) {
            final IsoProfile profileFromDatabase = isoService.getProfile(this.profile.getId());
            try {
                String author = ((User) identity.getAccount()).getEmail();
                final List<History> histories = DRCCBeanUtils.generateHistoryFromDifferences(profileFromDatabase, profile, null, Collections.singletonList("iso"), "id", author, true);
                historyService.saveHistory(histories);
            } catch (Exception e) {
                log.error("exception generating/saving historical changes in profile", e);
                throw new IllegalStateException("exception generating/saving historical changes in profile", e);
            }
        }

        try {
            isoService.saveOrUpdateProfile(profile);
            clearProfile();
            uiMessage.addMessage("Market Profile saved");
        } catch (IOException e) {
            uiMessage.addMessage("Error trying to save the ISO, please try again later and contact your administrator if you keep having this problem");
            log.warn("Error saving a Market", e);
        }
    }

    public void cancelProfile() {
        clearProfile();
    }

    private void clearProfile() {
        profile = null;
        newProfile = false;
        iso = isoService.getIso(iso.getId());
    }

    public void add() {
        profile = new IsoProfile();
        newProfile = true;
        profile.setIso(iso);
    }

    public void addHoliday() {
        if (profile.getHolidays() == null) {
            profile.setHolidays(new ArrayList<IsoHoliday>());
        }
        final IsoHoliday holiday = new IsoHoliday();
        holiday.setProfile(profile);
        profile.getHolidays().add(holiday);
    }

    public void addProduct() {
        if (profile.getProducts() == null) {
            profile.setProducts(new ArrayList<IsoProduct>());
        }
        final IsoProduct product = new IsoProduct();
        product.setProfile(profile);
        profile.getProducts().add(product);
    }

    public void addRiskCondition() {
        if (profile.getRiskConditions() == null) {
            profile.setRiskConditions(new ArrayList<>());
        }
        final RiskCondition riskCondition = new RiskCondition();
        riskCondition.setProfile(profile);
        profile.getRiskConditions().add(riskCondition);
    }

    public void removeHoliday(IsoHoliday holiday) {
        profile.getHolidays().remove(holiday);
    }

    public void removeProduct(IsoProduct product) {
        profile.getProducts().remove(product);
    }

    public void removeRiskCondition(RiskCondition riskCondition) {
        profile.getRiskConditions().remove(riskCondition);
    }

    public void editProfile(IsoProfile profile) {
        clearProfile();
        this.profile = isoService.getProfile(profile.getId());
    }

    public List<History> getHistory() {
        return historyService.getHistory(profile);
    }
}