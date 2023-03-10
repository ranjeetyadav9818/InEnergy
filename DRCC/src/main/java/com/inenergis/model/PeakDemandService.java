package com.inenergis.model;

import com.inenergis.commonServices.PeakDemandServiceCommon;
import com.inenergis.network.pgerestclient.PgeLayer7;
import com.inenergis.util.PropertyAccessor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Properties;

@Named
@RequestScoped
@Getter
@Setter
public class PeakDemandService extends PeakDemandServiceCommon implements Serializable {

    private Logger log = LoggerFactory.getLogger(PeakDemandService.class);

    @Inject
    PropertyAccessor propertyAccessor;

    private PgeLayer7 pgeLayer7;

    private Properties internalProperties;

    @PostConstruct
    public void init() {
        internalProperties = propertyAccessor.getProperties();
    }

    @Override
    protected Properties getProperties() {
        return internalProperties;
    }
}