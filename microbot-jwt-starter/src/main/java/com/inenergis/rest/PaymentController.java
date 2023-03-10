package com.inenergis.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inenergis.security.TokenHelper;
import com.inenergis.service.PortalUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by fan.jin on 2017-05-10.
 */

@RestController
@RequestMapping(value = "/payments", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {


    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);
    public static final String ERROR_PARSING_SERVICE_AGREEMENT_FROM_USER = "Error parsing Service Agreement from user ";

    @Autowired
    TokenHelper tokenHelper;

    @Autowired
    private PortalUserService portalUserService;

    @CrossOrigin(origins = "${customer.portal.url}")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getPayments(HttpServletRequest request) {
        final String token = tokenHelper.getToken(request);
        final String user = tokenHelper.getUsernameFromToken(token);
        try {
            String payments = portalUserService.getPaymentsByEmail(user);
            return ResponseEntity.ok(payments);
        } catch (JsonProcessingException e) {
            log.error(ERROR_PARSING_SERVICE_AGREEMENT_FROM_USER, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @CrossOrigin(origins = "${customer.portal.url}")
    @RequestMapping(value = "/last", method = RequestMethod.GET)
    public ResponseEntity<?> getLastPayment(HttpServletRequest request) {
        final String token = tokenHelper.getToken(request);
        final String user = tokenHelper.getUsernameFromToken(token);
        try {
            String payments = portalUserService.getLastPayment(user);
            return ResponseEntity.ok(payments);
        } catch (JsonProcessingException e) {
            log.error(ERROR_PARSING_SERVICE_AGREEMENT_FROM_USER, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}