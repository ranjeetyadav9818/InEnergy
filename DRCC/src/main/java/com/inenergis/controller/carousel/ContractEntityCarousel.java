package com.inenergis.controller.carousel;

import com.inenergis.controller.customerData.DataBean;
import com.inenergis.controller.customerData.DataBeanList;
import com.inenergis.controller.general.ConstantsProvider;
import com.inenergis.controller.model.EnergyArrayDataBeanList;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.contract.ContractAddress;
import com.inenergis.entity.contract.ContractEntity;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import org.apache.commons.collections.CollectionUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ContractEntityCarousel {

    public void generateContractEntityCarousel(List<EnergyArrayDataBeanList> entityDetails, ContractEntity entity) {
        List<DataBean> cBeans = new ArrayList<>();
        cBeans.add(new DataBean("Business Name", entity.getBusinessName()));
        cBeans.add(new DataBean("DBA Name", entity.getDba()));
        cBeans.add(new DataBean("Sector", entity.getSector() == null ? "" : entity.getSector().getLabel()));
        cBeans.add(new DataBean("Tax Id", entity.getTaxId()));
        if (entity.getParentCompany() != null) {
            cBeans.add(new DataBean("Parent", entity.getParentCompany().getBusinessName()));
        }

        EnergyArrayDataBeanList customerData = new EnergyArrayDataBeanList("Entity", "icon-account_box2", cBeans);
        customerData.setObject(entity);
        entityDetails.add(customerData);

        if (CollectionUtils.isNotEmpty(entity.getContractAddresses())) {
            for (ContractAddress address : entity.getContractAddresses()) {
                List<DataBean> aBeans = new ArrayList<>();
                aBeans.add(new DataBean("Address Type", address.getAddressType() == null ? "" : address.getAddressType().getLabel()));
                aBeans.add(new DataBean("Address 1", address.getAddress1()));
                aBeans.add(new DataBean("Address 2", address.getAddress2()));
                aBeans.add(new DataBean("City", address.getCity()));
                aBeans.add(new DataBean("State", address.getState() == null ? "" : address.getState().getLabel()));
                aBeans.add(new DataBean("Post Code", address.getPostCode()));


                EnergyArrayDataBeanList addressData = new EnergyArrayDataBeanList("Address", ConstantsProvider.ICON_LOCATION_OUTLINE, aBeans);
                entityDetails.add(addressData);
            }
        }
    }
}