package com.inenergis.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inenergis.dao.bigdata.ConsumptionByMonth;
import com.inenergis.entity.MeterDataUsage;
import com.inenergis.entity.ServicePoint;
import com.inenergis.model.meter.MeterData;
import com.inenergis.security.TokenHelper;
import com.inenergis.service.ConsumptionService;
import com.inenergis.service.PortalUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by fan.jin on 2017-05-10.
 */

@RestController
@RequestMapping(value = "/consumption", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConsumptionController {


    private static final Logger log = LoggerFactory.getLogger(ConsumptionController.class);

    @Autowired
    TokenHelper tokenHelper;

    @Autowired
    private PortalUserService portalUserService;
    @Autowired
    private ConsumptionService consumptionService;

    @CrossOrigin(origins = "${customer.portal.url}")
    @RequestMapping(method = RequestMethod.GET, path = "monthly")
    public ResponseEntity<?> getPayments(HttpServletRequest request) {
        final String token = tokenHelper.getToken(request);
        final String user = tokenHelper.getUsernameFromToken(token);
        List<ServicePoint> servicePointsByEmail = portalUserService.getServicePointsByEmail(user);
        List<Object> meterData = consumptionService.getConsumptionByMonth(servicePointsByEmail);
        return ResponseEntity.ok(meterData);
    }
}