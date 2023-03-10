package com.inenergis.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inenergis.security.TokenHelper;
import com.inenergis.service.InvoiceService;
import com.inenergis.service.PortalUserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
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

/**
 * Created by fan.jin on 2017-05-10.
 */

@RestController
@RequestMapping(value = "/invoices", produces = MediaType.APPLICATION_JSON_VALUE)
public class InvoiceController {


    private static final Logger log = LoggerFactory.getLogger(InvoiceController.class);
    public static final String ERROR_PARSING_SERVICE_AGREEMENT_FROM_USER = "Error parsing Service Agreement from user ";

    @Autowired
    TokenHelper tokenHelper;

    @Autowired
    private PortalUserService portalUserService;

    @Autowired
    private InvoiceService invoiceService;

    @CrossOrigin(origins = "${customer.portal.url}")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getInvoices(HttpServletRequest request) {
        final String token = tokenHelper.getToken(request);
        final String user = tokenHelper.getUsernameFromToken(token);
        try {
            String invoices = portalUserService.getInvoicesByEmail(user);
            return ResponseEntity.ok(invoices);
        } catch (JsonProcessingException e) {
            log.error(ERROR_PARSING_SERVICE_AGREEMENT_FROM_USER, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @CrossOrigin(origins = "${customer.portal.url}")
    @RequestMapping(method = RequestMethod.GET, path = "/balance")
    public ResponseEntity<?> getBalance(HttpServletRequest request) {
        final String token = tokenHelper.getToken(request);
        final String user = tokenHelper.getUsernameFromToken(token);
        try {
            Long balance = portalUserService.getBalanceByEmail(user);
            return ResponseEntity.ok(new Balance(balance));
        } catch (JsonProcessingException e) {
            log.error(ERROR_PARSING_SERVICE_AGREEMENT_FROM_USER, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @CrossOrigin(origins = "${customer.portal.url}")
    @RequestMapping(method = RequestMethod.GET, path = "/last")
    public ResponseEntity<?> getLastInvoice(HttpServletRequest request) {
        final String token = tokenHelper.getToken(request);
        final String user = tokenHelper.getUsernameFromToken(token);
        try {
            String invoice = invoiceService.getLastInvoiceByEmail(user);
            return ResponseEntity.ok(invoice);
        } catch (JsonProcessingException e) {
            log.error(ERROR_PARSING_SERVICE_AGREEMENT_FROM_USER, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private class Balance{
        private Long balance;
    }
}