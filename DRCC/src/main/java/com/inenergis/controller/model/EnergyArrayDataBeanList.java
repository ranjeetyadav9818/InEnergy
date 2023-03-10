package com.inenergis.controller.model;

import com.inenergis.controller.customerData.DataBean;
import com.inenergis.controller.customerData.DataBeanList;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EnergyArrayDataBeanList extends DataBeanList {

    private Object object;

    public EnergyArrayDataBeanList(String entity, String s, List<DataBean> cBeans) {
        super(entity,s,cBeans);
    }
}
