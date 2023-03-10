package com.inenergis.controller.carousel;

import com.inenergis.controller.converter.PhoneConverter;
import com.inenergis.controller.customerData.GasDataBean;
import com.inenergis.controller.customerData.GasDataBeanList;
import com.inenergis.controller.general.ConstantsProvider;
import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.GasServiceAgreement;
import com.inenergis.entity.GasServicePoint;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class GasServiceAgreementCarousel {

    private static final Logger log = LoggerFactory.getLogger(GasServiceAgreementCarousel.class);

    private static final DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

    private PhoneConverter phoneConverter = new PhoneConverter();
    public List<GasDataBeanList> printServiceAgreementCarousel(AgreementPointMap sa_sp) { //todo icons to properties
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        List<GasDataBeanList> serviceAgreementDetails = new ArrayList<>();
        GasServiceAgreement serviceAgreement = (GasServiceAgreement) sa_sp.getServiceAgreement();
        GasServicePoint servicePoint = (GasServicePoint) sa_sp.getServicePoint();
        // first build customer data
        List<GasDataBean> cBeans = new ArrayList<>();
        cBeans.add(new GasDataBean("Customer Name", serviceAgreement.getAccount().getPerson().getCustomerName()));
        cBeans.add(new GasDataBean("DBA Name", serviceAgreement.getAccount().getPerson().getBusinessName()));
        cBeans.add(new GasDataBean("Person Id", serviceAgreement.getAccount().getPerson().getPersonId()));
        cBeans.add(new GasDataBean("Account ID", serviceAgreement.getAccount().getAccountId()));
        cBeans.add(new GasDataBean("Phone", getPhoneFullValue(serviceAgreement)));

        GasDataBeanList customerData = new GasDataBeanList("Customer Overview", "icon-account_box2", cBeans);
        serviceAgreementDetails.add(customerData);

        // SA overview
        List<GasDataBean> saBeans = new ArrayList<>();
        saBeans.add(new GasDataBean("SA ID", serviceAgreement.getServiceAgreementId()));
        saBeans.add(new GasDataBean("SA Status", serviceAgreement.translateSAStatusLabel()));
        if (serviceAgreement.getStartDate() != null) {
            saBeans.add(new GasDataBean("SA Start Date", df.format(serviceAgreement.getStartDate())));
        } else {
            saBeans.add(new GasDataBean("SA Start Date", ""));
        }
        if (serviceAgreement.getEndDate() != null) {
            saBeans.add(new GasDataBean("SA End Date", df.format(serviceAgreement.getEndDate())));
        } else {
            saBeans.add(new GasDataBean("SA End Date", ""));
        }
        saBeans.add(new GasDataBean("UUID", serviceAgreement.getSaUuid()));

        GasDataBeanList saData = new GasDataBeanList("SA Overview", ConstantsProvider.ICON_DOC_TEXT, saBeans);
        serviceAgreementDetails.add(saData);

        // SP overview
        if (servicePoint != null) {
            List<GasDataBean> spBeans = new ArrayList<>();
            spBeans.add(new GasDataBean("Service Address 1", servicePoint.getPremise().getServiceAddress1()));
            spBeans.add(new GasDataBean("Service Address 2", servicePoint.getPremise().getServiceAddress2()));
            spBeans.add(new GasDataBean("Service City", servicePoint.getPremise().getServiceCityUpr()));
            spBeans.add(new GasDataBean("Service State", servicePoint.getPremise().getServiceState()));
            spBeans.add(new GasDataBean("Service ZIP", servicePoint.getPremise().getServicePostal()));
            spBeans.add(new GasDataBean("County", servicePoint.getPremise().getCounty()));

            GasDataBeanList spData = new GasDataBeanList("Premise Overview", ConstantsProvider.ICON_LOCATION_OUTLINE, spBeans);
            serviceAgreementDetails.add(spData);
        }

        //Unique Agreement Indicators
        List<GasDataBean> uaiBeans = new ArrayList<>();

        uaiBeans.add(new GasDataBean("Unique SA", serviceAgreement.getUniqSaId()));
        if (serviceAgreement.getUniqSaIdCreateDate() != null) {
            uaiBeans.add(new GasDataBean("Unique SA Date", df.format(serviceAgreement.getUniqSaIdCreateDate())));
        } else {
            uaiBeans.add(new GasDataBean("Unique SA Date", ""));
        }
        uaiBeans.add(new GasDataBean("Unique SA Indicator", serviceAgreement.getUniqSaIdWarnFlag()));

        GasDataBeanList uaiData = new GasDataBeanList("Unique Agreement Indicators", ConstantsProvider.ICON_LINK, uaiBeans);
        serviceAgreementDetails.add(uaiData);


        // mailing overview
        List<GasDataBean> mailBeans = new ArrayList<>();
        mailBeans.add(new GasDataBean("Billing Address 1", serviceAgreement.getMailingAddress1()));
        mailBeans.add(new GasDataBean("Billing Address 2", serviceAgreement.getMailingAddress2()));
        mailBeans.add(new GasDataBean("Billing City", serviceAgreement.getMailingCityUpr()));
        mailBeans.add(new GasDataBean("Billing State", serviceAgreement.getMailingState()));
        mailBeans.add(new GasDataBean("Billing ZIP", serviceAgreement.getMailingPostal()));
        GasDataBeanList mailData = new GasDataBeanList("Mailing Overview", ConstantsProvider.ICON_MAIL, mailBeans);
        serviceAgreementDetails.add(mailData);

        // billing overview
        List<GasDataBean> billBeans = new ArrayList<>();
        billBeans.add(new GasDataBean("Revenue Protected", serviceAgreement.getTypeCd()));
        billBeans.add(new GasDataBean("Revenue Classification", serviceAgreement.getRevenueClassDesc()));
        billBeans.add(new GasDataBean("Bill Cycle Code", serviceAgreement.getBillCycleCd()));
        billBeans.add(new GasDataBean("Rate Schedule", serviceAgreement.getRateSchedule()));
        if (serviceAgreement.getRateCodeEffectiveDate() != null) {
            billBeans.add(new GasDataBean("Rate Schedule Effective", df.format(serviceAgreement.getRateCodeEffectiveDate())));
        } else {
            billBeans.add(new GasDataBean("Rate Schedule Effective", ""));
        }
        billBeans.add(new GasDataBean("Billing System", serviceAgreement.getBillSystem()));
        GasDataBeanList billData = new GasDataBeanList("Billing Overview", ConstantsProvider.ICON_DOLLAR, billBeans);
        serviceAgreementDetails.add(billData);

        // SA indicators
        List<GasDataBean> saiBeans = new ArrayList<>();
        saiBeans.add(new GasDataBean("CARE", yesNoBooleanTranslator(serviceAgreement.isCareFlag())));
        saiBeans.add(new GasDataBean("Life Support", yesNoBooleanTranslator(serviceAgreement.isLifeSupportInd())));
        saiBeans.add(new GasDataBean("Medical Baseline", yesNoBooleanTranslator(serviceAgreement.isMedicalBaselineInd())));
        saiBeans.add(new GasDataBean("FERA", yesNoBooleanTranslator(serviceAgreement.isFeraFlag())));
//  todogas      if (servicePoint != null) {
//            saiBeans.add(new GasDataBean("Electric Usage", servicePoint.getElecUsageNonres()));
//        }
        saiBeans.add(new GasDataBean("Essential Customer", serviceAgreement.getEssentialCustomerFlag()));
        GasDataBeanList saIndicatorData = new GasDataBeanList("SA Indicators", ConstantsProvider.ICON_FLAG_1, saiBeans);
        serviceAgreementDetails.add(saIndicatorData);


        // ISO Indicators
        List<GasDataBean> isoBeans = new ArrayList<>();
        isoBeans.add(new GasDataBean("3rd Party DRP", serviceAgreement.getHas3rdPartyDrp()));
        isoBeans.add(new GasDataBean("Company is DRP", yesNoBooleanTranslator(serviceAgreement.isSupplierIsDrp())));
        isoBeans.add(new GasDataBean("LSE Code", serviceAgreement.getCustomerLseCode()));
        isoBeans.add(new GasDataBean("LSE Name", serviceAgreement.getCustomerLSECompanyName()));

        GasDataBeanList isoData = new GasDataBeanList("Market Indicators", ConstantsProvider.ICON_BELL_2, isoBeans);
        serviceAgreementDetails.add(isoData);

        // Business Indicators
        List<GasDataBean> busBeans = new ArrayList<>();
        busBeans.add(new GasDataBean("Business Type", serviceAgreement.getAccount().getPerson().getBusinessOwner()));
        busBeans.add(new GasDataBean("Division", serviceAgreement.getDivisionCode19()));
        busBeans.add(new GasDataBean("ESS Division", serviceAgreement.getEssDivisionCode()));
        busBeans.add(new GasDataBean("Description", serviceAgreement.getBusinessActivityDescription()));

        GasDataBeanList busData = new GasDataBeanList("Business Indicators", ConstantsProvider.ICON_BUILDING, busBeans);
        serviceAgreementDetails.add(busData);

        // Segmentation
        List<GasDataBean> segBeans = new ArrayList<>();
        segBeans.add(new GasDataBean("Customer Type", serviceAgreement.isResInd() ? "Res" : "Non-Res"));
        segBeans.add(new GasDataBean("Customer Class", serviceAgreement.getCustClassCd()));
        segBeans.add(new GasDataBean("NAICS", serviceAgreement.getNaics()));
        segBeans.add(new GasDataBean("Customer Size", serviceAgreement.getCust_size()));
        segBeans.add(new GasDataBean("Market Segment", serviceAgreement.getMarketSegment()));

        GasDataBeanList segData = new GasDataBeanList("Segmentation", ConstantsProvider.ICON_TAGS, segBeans);
        serviceAgreementDetails.add(segData);

        return serviceAgreementDetails;
    }
    public List<GasDataBeanList> printCustomerCarousel(AgreementPointMap sa_sp){
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        List<GasDataBeanList> serviceAgreementDetails = new ArrayList<>();
        GasServiceAgreement serviceAgreement = (GasServiceAgreement) sa_sp.getServiceAgreement();
        GasServicePoint servicePoint = (GasServicePoint) sa_sp.getServicePoint();
        // first build customer data
        List<GasDataBean> cBeans = new ArrayList<>();
        cBeans.add(new GasDataBean("Customer Name", serviceAgreement.getAccount().getPerson().getCustomerName()));
        cBeans.add(new GasDataBean("DBA Name", serviceAgreement.getAccount().getPerson().getBusinessName()));
        cBeans.add(new GasDataBean("Person Id", serviceAgreement.getAccount().getPerson().getPersonId()));
        cBeans.add(new GasDataBean("Account ID", serviceAgreement.getAccount().getAccountId()));
        cBeans.add(new GasDataBean("Phone", getPhoneFullValue(serviceAgreement)));

        GasDataBeanList customerData = new GasDataBeanList("Customer Overview", "icon-account_box2", cBeans);
        serviceAgreementDetails.add(customerData);

        // SA overview
//        List<DataBean> saBeans = new ArrayList<>();
//        saBeans.add(new DataBean("SA ID", serviceAgreement.getServiceAgreementId()));
//        saBeans.add(new DataBean("SA Status", serviceAgreement.translateSAStatusLabel()));
//        if (serviceAgreement.getStartDate() != null) {
//            saBeans.add(new DataBean("SA Start Date", df.format(serviceAgreement.getStartDate())));
//        } else {
//            saBeans.add(new DataBean("SA Start Date", ""));
//        }
//        if (serviceAgreement.getEndDate() != null) {
//            saBeans.add(new DataBean("SA End Date", df.format(serviceAgreement.getEndDate())));
//        } else {
//            saBeans.add(new DataBean("SA End Date", ""));
//        }
//        saBeans.add(new DataBean("UUID", serviceAgreement.getSaUuid()));
//
//        DataBeanList saData = new DataBeanList("SA Overview", ConstantsProvider.ICON_DOC_TEXT, saBeans);
//        serviceAgreementDetails.add(saData);

        return serviceAgreementDetails;
    }

    private String yesNoBooleanTranslator(boolean b) {
        return b ? "Yes" : "No";
    }

    private String getPhoneFullValue(GasServiceAgreement serviceAgreement) {
        if (StringUtils.isNotEmpty(serviceAgreement.getPhone())) {
            String phoneNumber = phoneConverter.convertToAmericanMaskIfPhoneNumber(serviceAgreement.getPhone());
            if (StringUtils.isNotEmpty(serviceAgreement.getAccount().getPerson().getPhoneExtension())) {
                phoneNumber += " ext: " + serviceAgreement.getAccount().getPerson().getPhoneExtension();
            }
            return phoneNumber;
        }
        return StringUtils.EMPTY;
    }
}