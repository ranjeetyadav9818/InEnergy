package com.inenergis.controller.carousel;

import com.inenergis.controller.customerData.DataBean;
import com.inenergis.controller.customerData.DataBeanList;
import com.inenergis.controller.general.ConstantsProvider;
import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.Meter;
import com.inenergis.entity.Premise;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.ServicePoint;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


import javax.ejb.Stateless;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Stateless
public class ServicePointCarousel {

    private static final Logger log = LoggerFactory.getLogger(ServicePointCarousel.class);

    private static final DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

    public List<DataBeanList> printServicePointCarousel(AgreementPointMap sa_sp) {
        List<DataBeanList> servicePointDetails = new ArrayList<>();
        ServicePoint servicePoint = (ServicePoint) sa_sp.getServicePoint();
        Premise prem = servicePoint.getPremise();
        ServiceAgreement serviceAgreement = (ServiceAgreement) sa_sp.getServiceAgreement();
        Meter meter = (Meter) servicePoint.getMeter();


        // service point
        List<DataBean> spBeans = new ArrayList<DataBean>();
        spBeans.add(new DataBean("Service Point ID", servicePoint.getServicePointId()));
//        spBeans.add(new DataBean("Premise ID", prem.getPremiseId()));
        spBeans.add(new DataBean("Service Agreement", serviceAgreement.getServiceAgreementId()));
        spBeans.add(new DataBean("SA-SP ID", sa_sp.getSaSpId()));
        if(sa_sp.getStartDate()!=null){
            spBeans.add(new DataBean("SA-SP Start", df.format(sa_sp.getStartDate())));
        }else{
            spBeans.add(new DataBean("SA-SP Start", null));
        }
        if(sa_sp.getEndDate()!=null){
            spBeans.add(new DataBean("SA-SP End", df.format(sa_sp.getEndDate())));
        }else{
            spBeans.add(new DataBean("SA-SP End", null));
        }
        DataBeanList saData = new DataBeanList("Service Point", "custom-icon-service_point", spBeans);
        servicePointDetails.add(saData);

        // premise overview
        List<DataBean> cBeans = new ArrayList<DataBean>();
        cBeans.add(new DataBean("Service Address 1", prem.getServiceAddress1()));
        cBeans.add(new DataBean("Service Address 2", prem.getServiceAddress2()));
        cBeans.add(new DataBean("City", prem.getServiceCityUpr()));
        cBeans.add(new DataBean("State", prem.getServiceState()));
        cBeans.add(new DataBean("Zip", prem.getServicePostal()));
        cBeans.add(new DataBean("County", prem.getCounty()));
        String iconNameClassPlusGenericIconClass = new StringJoiner(" ").add(ConstantsProvider.ICON_LOCATION_OUTLINE).add("material-icons").toString();
        DataBeanList customerData = new DataBeanList("Premise Overview", iconNameClassPlusGenericIconClass, cBeans);
        servicePointDetails.add(customerData);

        // Meter
        List<DataBean> mBeans = new ArrayList<DataBean>();
        mBeans.add(new DataBean("Meter ID", meter.getMeterId()));
        mBeans.add(new DataBean("Badge", meter.getBadgeNumber()));
        if(meter.getInstallDate()!=null){
            mBeans.add(new DataBean("Meter Install", df.format(meter.getInstallDate())));
        }else{
            mBeans.add(new DataBean("Meter Install", null));
        }
        if(meter.getUninstallDate()!=null){
            mBeans.add(new DataBean("Meter Uninstall", df.format(meter.getUninstallDate())));
        }else{
            mBeans.add(new DataBean("Meter Uninstall", null));
        }
        DataBeanList meterData = new DataBeanList("Meter", "custom-icon-meter", mBeans);
        servicePointDetails.add(meterData);

        // Meter type
        List<DataBean> mtBeans = new ArrayList<DataBean>();
        mtBeans.add(new DataBean("Manufacturer", meter.getMfg()));
        mtBeans.add(new DataBean("Config Type", meter.getConfigType()));
        mtBeans.add(new DataBean("Read Frequency", meter.getReadFreq()));
        mtBeans.add(new DataBean("Read cycle", servicePoint.getMeterReadCycle12()));
        DataBeanList meterTypeData = new DataBeanList("Meter type", "custom-icon-meter_type", mtBeans);
        servicePointDetails.add(meterTypeData);

        // Smart meter
        List<DataBean> smBeans = new ArrayList<DataBean>();
        smBeans.add(new DataBean("Smart Meter Status", meter.getSmStatus()));
        smBeans.add(new DataBean("Manufacturer", meter.getSmModuleMfr()));
        DataBeanList smartMeterData = new DataBeanList("Smart Meter", "custom-icon-smart_meter", smBeans);
        servicePointDetails.add(smartMeterData);

        // Distribution
        List<DataBean> dBeans = new ArrayList<DataBean>();
        dBeans.add(new DataBean("Transformer", servicePoint.getTrfmrNumber()));
        dBeans.add(new DataBean("Transformer Badge", servicePoint.getTrfmrBadgeNumber()));
        dBeans.add(new DataBean("Substation", servicePoint.getSubStationNumber()));
        dBeans.add(new DataBean("Feeder", servicePoint.getFeeder()));
        dBeans.add(new DataBean("Circuit", servicePoint.getCircuitNumber()));
        dBeans.add(new DataBean("ROB", servicePoint.getRobCode()));
        DataBeanList distributionData = new DataBeanList("Distribution", "custom-icon-distribution", dBeans);
        servicePointDetails.add(distributionData);

        // Premise Identifiers
        List<DataBean> piBeans = new ArrayList<DataBean>();
        piBeans.add(new DataBean("Premise Type", prem.getPremiseType()));
        piBeans.add(new DataBean("Service Type", servicePoint.getServiceType()));
        piBeans.add(new DataBean("Source Side Device", servicePoint.getSourceSideDeviceNumber()));
        piBeans.add(new DataBean("Voltage Class", servicePoint.getServiceVoltageClass()));
        piBeans.add(new DataBean("PremiseBaseline", prem.getBaseLineChar()));
        DataBeanList premiseIdentifiersData = new DataBeanList("Premise Identifiers", "custom-icon-premise_identifies", piBeans);
        servicePointDetails.add(premiseIdentifiersData);

        // Location
        List<DataBean> lBeans = new ArrayList<DataBean>();
        lBeans.add(new DataBean("Latitude", servicePoint.getLatitude()));
        lBeans.add(new DataBean("Longitude", servicePoint.getLongitude()));
        lBeans.add(new DataBean("Operational Area", servicePoint.getOperationArea()));
        DataBeanList locationData = new DataBeanList("Location", "custom-icon-custom_location", lBeans);
        servicePointDetails.add(locationData);

        // Location
        List<DataBean> paBeans = new ArrayList<DataBean>();
        paBeans.add(new DataBean("MDMA", servicePoint.getCustomerMdmaCompanyName()));
        paBeans.add(new DataBean("MDMA Code", "Pending"));
        paBeans.add(new DataBean("MSP", servicePoint.getCustomerMspCompanyName()));
        paBeans.add(new DataBean("MSP Code", "Pending"));
        DataBeanList providerAgentData = new DataBeanList("Provider & Agent", "custom-icon-provider_agent", paBeans);
        servicePointDetails.add(providerAgentData); 

        return servicePointDetails;
    }
}