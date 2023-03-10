package com.inenergis.service;

import com.inenergis.commonServices.PeakDemandServiceCommon;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Created by egamas on 12/10/2017.
 */
@Getter
@Setter
@Component
public class PeakDemandService extends PeakDemandServiceCommon {

    private static final Logger log = LoggerFactory.getLogger(PeakDemandService.class);

    @Autowired
    @Qualifier("appProperties")
    private Properties properties;

    @Override
    protected Properties getProperties(){
        return properties;
    }
}
