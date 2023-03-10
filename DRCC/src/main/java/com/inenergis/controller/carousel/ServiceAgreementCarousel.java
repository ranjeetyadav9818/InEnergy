package com.inenergis.controller.carousel;

import com.inenergis.controller.converter.PhoneConverter;
import com.inenergis.controller.customerData.DataBean;
import com.inenergis.controller.customerData.DataBeanList;
import com.inenergis.controller.general.ConstantsProvider;
import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.ServicePoint;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ServiceAgreementCarousel {

    private static final Logger log = LoggerFactory.getLogger(ServiceAgreementCarousel.class);

    private static final DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

    private PhoneConverter phoneConverter = new PhoneConverter();

    public List<DataBeanList> printServiceAgreementCarousel(AgreementPointMap sa_sp) { //todo icons to properties
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        List<DataBeanList> serviceAgreementDetails = new ArrayList<>();
        ServiceAgreement serviceAgreement = (ServiceAgreement) sa_sp.getServiceAgreement();
        ServicePoint servicePoint = (ServicePoint) sa_sp.getServicePoint();
        // first build customer data
        List<DataBean> cBeans = new ArrayList<>();
        cBeans.add(new DataBean("Customer Name", serviceAgreement.getAccount().getPerson().getCustomerName()));
        cBeans.add(new DataBean("DBA Name", serviceAgreement.getAccount().getPerson().getBusinessName()));
        cBeans.add(new DataBean("Person Id", serviceAgreement.getAccount().getPerson().getPersonId()));
        cBeans.add(new DataBean("Account ID", serviceAgreement.getAccount().getAccountId()));
        cBeans.add(new DataBean("Phone", getPhoneFullValue(serviceAgreement)));

        DataBeanList customerData = new DataBeanList("Customer Overview", "icon-account_box2", cBeans);
        serviceAgreementDetails.add(customerData);

        // SA overview
        List<DataBean> saBeans = new ArrayList<>();
        saBeans.add(new DataBean("SA ID", serviceAgreement.getServiceAgreementId()));
        saBeans.add(new DataBean("SA Status", serviceAgreement.translateSAStatusLabel()));
        if (serviceAgreement.getStartDate() != null) {
            saBeans.add(new DataBean("SA Start Date", df.format(serviceAgreement.getStartDate())));
        } else {
            saBeans.add(new DataBean("SA Start Date", ""));
        }
        if (serviceAgreement.getEndDate() != null) {
            saBeans.add(new DataBean("SA End Date", df.format(serviceAgreement.getEndDate())));
        } else {
            saBeans.add(new DataBean("SA End Date", ""));
        }
        saBeans.add(new DataBean("UUID", serviceAgreement.getSaUuid()));

        DataBeanList saData = new DataBeanList("SA Overview", ConstantsProvider.ICON_DOC_TEXT, saBeans);
        serviceAgreementDetails.add(saData);

        // SP overview
        if (servicePoint != null) {
            List<DataBean> spBeans = new ArrayList<>();
            spBeans.add(new DataBean("Service Address 1", servicePoint.getPremise().getServiceAddress1()));
            spBeans.add(new DataBean("Service Address 2", servicePoint.getPremise().getServiceAddress2()));
            spBeans.add(new DataBean("Service City", servicePoint.getPremise().getServiceCityUpr()));
            spBeans.add(new DataBean("Service State", servicePoint.getPremise().getServiceState()));
            spBeans.add(new DataBean("Service ZIP", servicePoint.getPremise().getServicePostal()));
            spBeans.add(new DataBean("County", servicePoint.getPremise().getCounty()));

            DataBeanList spData = new DataBeanList("Premise Overview", ConstantsProvider.ICON_LOCATION_OUTLINE, spBeans);
            serviceAgreementDetails.add(spData);
        }

        //Unique Agreement Indicators
        List<DataBean> uaiBeans = new ArrayList<>();

        uaiBeans.add(new DataBean("Unique SA", serviceAgreement.getUniqSaId()));
        if (serviceAgreement.getUniqSaIdCreateDate() != null) {
            uaiBeans.add(new DataBean("Unique SA Date", df.format(serviceAgreement.getUniqSaIdCreateDate())));
        } else {
            uaiBeans.add(new DataBean("Unique SA Date", ""));
        }
        uaiBeans.add(new DataBean("Unique SA Indicator", serviceAgreement.getUniqSaIdWarnFlag()));

        DataBeanList uaiData = new DataBeanList("Unique Agreement Indicators", ConstantsProvider.ICON_LINK, uaiBeans);
        serviceAgreementDetails.add(uaiData);


        // mailing overview
        List<DataBean> mailBeans = new ArrayList<>();
        mailBeans.add(new DataBean("Billing Address 1", serviceAgreement.getMailingAddress1()));
        mailBeans.add(new DataBean("Billing Address 2", serviceAgreement.getMailingAddress2()));
        mailBeans.add(new DataBean("Billing City", serviceAgreement.getMailingCityUpr()));
        mailBeans.add(new DataBean("Billing State", serviceAgreement.getMailingState()));
        mailBeans.add(new DataBean("Billing ZIP", serviceAgreement.getMailingPostal()));
        DataBeanList mailData = new DataBeanList("Mailing Overview", ConstantsProvider.ICON_MAIL, mailBeans);
        serviceAgreementDetails.add(mailData);

        // billing overview
        List<DataBean> billBeans = new ArrayList<>();
        billBeans.add(new DataBean("Revenue Protected", serviceAgreement.getTypeCd()));
        billBeans.add(new DataBean("Revenue Classification", serviceAgreement.getRevenueClassDesc()));
        billBeans.add(new DataBean("Bill Cycle Code", serviceAgreement.getBillCycleCd()));
        billBeans.add(new DataBean("Rate Schedule", serviceAgreement.getRateSchedule()));
        if (serviceAgreement.getRateCodeEffectiveDate() != null) {
            billBeans.add(new DataBean("Rate Schedule Effective", df.format(serviceAgreement.getRateCodeEffectiveDate())));
        } else {
            billBeans.add(new DataBean("Rate Schedule Effective", ""));
        }
        billBeans.add(new DataBean("Billing System", serviceAgreement.getBillSystem()));
        DataBeanList billData = new DataBeanList("Billing Overview", ConstantsProvider.ICON_DOLLAR, billBeans);
        serviceAgreementDetails.add(billData);

        // SA indicators
        List<DataBean> saiBeans = new ArrayList<>();
        saiBeans.add(new DataBean("CARE", yesNoBooleanTranslator(serviceAgreement.isCareFlag())));
        saiBeans.add(new DataBean("Life Support", yesNoBooleanTranslator(serviceAgreement.isLifeSupportInd())));
        saiBeans.add(new DataBean("Medical Baseline", yesNoBooleanTranslator(serviceAgreement.isMedicalBaselineInd())));
        saiBeans.add(new DataBean("FERA", yesNoBooleanTranslator(serviceAgreement.isFeraFlag())));
        if (servicePoint != null) {
            saiBeans.add(new DataBean("Electric Usage", servicePoint.getElecUsageNonres()));
        }
        saiBeans.add(new DataBean("Essential Customer", serviceAgreement.getEssentialCustomerFlag()));
        DataBeanList saIndicatorData = new DataBeanList("SA Indicators", ConstantsProvider.ICON_FLAG_1, saiBeans);
        serviceAgreementDetails.add(saIndicatorData);


        // ISO Indicators
        List<DataBean> isoBeans = new ArrayList<>();
        isoBeans.add(new DataBean("3rd Party DRP", serviceAgreement.getHas3rdPartyDrp()));
        isoBeans.add(new DataBean("Company is DRP", yesNoBooleanTranslator(serviceAgreement.isSupplierIsDrp())));
        isoBeans.add(new DataBean("LSE Code", serviceAgreement.getCustomerLseCode()));
        isoBeans.add(new DataBean("LSE Name", serviceAgreement.getCustomerLSECompanyName()));

        DataBeanList isoData = new DataBeanList("Market Indicators", ConstantsProvider.ICON_BELL_2, isoBeans);
        serviceAgreementDetails.add(isoData);

        // Business Indicators
        List<DataBean> busBeans = new ArrayList<>();
        busBeans.add(new DataBean("Business Type", serviceAgreement.getAccount().getPerson().getBusinessOwner()));
        busBeans.add(new DataBean("Division", serviceAgreement.getDivisionCode19()));
        busBeans.add(new DataBean("ESS Division", serviceAgreement.getEssDivisionCode()));
        busBeans.add(new DataBean("Description", serviceAgreement.getBusinessActivityDescription()));

        DataBeanList busData = new DataBeanList("Business Indicators", ConstantsProvider.ICON_BUILDING, busBeans);
        serviceAgreementDetails.add(busData);

        // Segmentation
        List<DataBean> segBeans = new ArrayList<>();
        segBeans.add(new DataBean("Customer Type", serviceAgreement.isResInd() ? "Res" : "Non-Res"));
        segBeans.add(new DataBean("Customer Class", serviceAgreement.getCustClassCd()));
        segBeans.add(new DataBean("NAICS", serviceAgreement.getNaics()));
        segBeans.add(new DataBean("Customer Size", serviceAgreement.getCust_size()));
        segBeans.add(new DataBean("Market Segment", serviceAgreement.getMarketSegment()));

        DataBeanList segData = new DataBeanList("Segmentation", ConstantsProvider.ICON_TAGS, segBeans);
        serviceAgreementDetails.add(segData);

        return serviceAgreementDetails;
    }

    public List<DataBeanList> printCustomerCarousel(AgreementPointMap sa_sp){
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        List<DataBeanList> serviceAgreementDetails = new ArrayList<>();
        ServiceAgreement serviceAgreement = (ServiceAgreement) sa_sp.getServiceAgreement();
        ServicePoint servicePoint = (ServicePoint) sa_sp.getServicePoint();
        // first build customer data
        List<DataBean> cBeans = new ArrayList<>();
        cBeans.add(new DataBean("Customer Name", serviceAgreement.getAccount().getPerson().getCustomerName()));
        cBeans.add(new DataBean("DBA Name", serviceAgreement.getAccount().getPerson().getBusinessName()));
        cBeans.add(new DataBean("Person Id", serviceAgreement.getAccount().getPerson().getPersonId()));
        cBeans.add(new DataBean("Account ID", serviceAgreement.getAccount().getAccountId()));
        cBeans.add(new DataBean("Phone", getPhoneFullValue(serviceAgreement)));

        DataBeanList customerData = new DataBeanList("Customer Overview", "icon-account_box2", cBeans);
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

    private String getPhoneFullValue(ServiceAgreement serviceAgreement) {
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