package com.inenergis.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inenergis.security.TokenHelper;
import com.inenergis.service.PortalUserService;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping(value = "/serviceAgreement", produces = MediaType.APPLICATION_JSON_VALUE)
public class APIController {


    private static final Logger log = LoggerFactory.getLogger(APIController.class);

    @Autowired
    TokenHelper tokenHelper;

    @Autowired
    private PortalUserService portalUserService;

    @CrossOrigin(origins = "${customer.portal.url}")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getServiceAgreement(HttpServletRequest request) {
        final String token = tokenHelper.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            final String user = tokenHelper.getUsernameFromToken(token);
            try {
                return ResponseEntity.ok(portalUserService.getServiceAgreementByEmail(user));
            } catch (JsonProcessingException e) {
                log.error("Error parsing Service Agreement from user ", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @CrossOrigin(origins = "${customer.portal.url}")
    @RequestMapping(value = "/ratePlans", method = RequestMethod.GET)
    public ResponseEntity<?> getRatePlans(HttpServletRequest request) {
        final String token = tokenHelper.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            final String user = tokenHelper.getUsernameFromToken(token);
            try {
                return ResponseEntity.ok(portalUserService.getRatePlans(user));
            } catch (JsonProcessingException e) {
                log.error("Error parsing Service Agreement from user ", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        return ResponseEntity.notFound().build();
    }

}