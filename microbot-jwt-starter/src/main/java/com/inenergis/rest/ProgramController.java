package com.inenergis.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inenergis.model.EnrollmentParams;
import com.inenergis.security.TokenHelper;
import com.inenergis.service.EligibilityEnrollmentService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Arrays;
import java.util.StringJoiner;

/**
 * Created by egamas on 09/10/2017.
 */
@RestController
@RequestMapping(value = "/programs", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProgramController {

    private static final Logger log = LoggerFactory.getLogger(ProgramController.class);

    @Autowired
    private TokenHelper tokenHelper;

    @Autowired
    private EligibilityEnrollmentService eligibilityEnrollmentService;

    @Autowired
    ObjectMapper objectMapper;

    @CrossOrigin(origins = "${customer.portal.url}")
    @RequestMapping(method = RequestMethod.GET, path = "/userAvailable")
    public ResponseEntity<?> getAvailablePrograms(HttpServletRequest request) {
        final String token = tokenHelper.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            final String user = tokenHelper.getUsernameFromToken(token);
            try {
                return ResponseEntity.ok(eligibilityEnrollmentService.getAvailableProgramsToEnroll(user));
            } catch (JsonProcessingException e) {
                log.error("Error parsing Program from user ", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @CrossOrigin(origins = "${customer.portal.url}")
    @RequestMapping(method = RequestMethod.GET, path = "/userEnrolled")
    public ResponseEntity<?> getUserEnrolled(HttpServletRequest request) {
        final String token = tokenHelper.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            final String user = tokenHelper.getUsernameFromToken(token);
            try {
                return ResponseEntity.ok(eligibilityEnrollmentService.getCurrentEnrolled(user));
            } catch (JsonProcessingException e) {
                log.error("Error parsing Program from user ", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @CrossOrigin(origins = "${customer.portal.url}")
    @RequestMapping(method = RequestMethod.GET, path = "/userPending")
    public ResponseEntity<?> getUserPending(HttpServletRequest request) {
        final String token = tokenHelper.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            final String user = tokenHelper.getUsernameFromToken(token);
            try {
                return ResponseEntity.ok(eligibilityEnrollmentService.getCurrentPending(user));
            } catch (JsonProcessingException e) {
                log.error("Error parsing Program from user ", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @CrossOrigin(origins = "${customer.portal.url}")
    @RequestMapping(method = RequestMethod.GET, path = "/checkEligibility/{programId}")
    public ResponseEntity<?> checkEligibility(HttpServletRequest request, @PathVariable("programId") String programId) {
        final String token = tokenHelper.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            final String user = tokenHelper.getUsernameFromToken(token);
            try {
                return ResponseEntity.ok(eligibilityEnrollmentService.checkEligibility(user, programId));
            } catch (JsonProcessingException e) {
                log.error("Error parsing Program from user ", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @CrossOrigin(origins = "${customer.portal.url}")
    @RequestMapping(method = RequestMethod.GET, path = "/viewEnroll/{programId}/{servicePointId}")
    public ResponseEntity<?> viewEnroll(HttpServletRequest request, @PathVariable("programId") String programId, @PathVariable("servicePointId") String servicePointId) {
        final String token = tokenHelper.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            final String user = tokenHelper.getUsernameFromToken(token);
            try {
                return ResponseEntity.ok(eligibilityEnrollmentService.viewEnroll(user, programId, servicePointId));
            } catch (JsonProcessingException e) {
                log.error("Error parsing Program from user ", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @CrossOrigin(origins = "${customer.portal.url}")
    @RequestMapping(method = RequestMethod.POST, path = "/enroll")
    public ResponseEntity<?> enroll(HttpServletRequest request,
                                    @RequestBody EnrollmentParams body

    ) {
        final String token = tokenHelper.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            final String user = tokenHelper.getUsernameFromToken(token);
            try {
                return ResponseEntity.ok(eligibilityEnrollmentService.enroll(user, body));
            } catch (JsonProcessingException e) {
                log.error("Error parsing Program from user ", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            } catch (ParseException e) {
                log.error("Error enrolling user in Program ", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @CrossOrigin(origins = "${customer.portal.url}")
    @RequestMapping(method = RequestMethod.GET, path = "/{programId}")
    public ResponseEntity<?> getProgramById(HttpServletRequest request, @PathVariable("programId") String programId) {
        final String token = tokenHelper.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            final String user = tokenHelper.getUsernameFromToken(token);
            try {
                final String program = eligibilityEnrollmentService.getProgramById(user, programId);
                if (StringUtils.isEmpty(program)) {
                    return ResponseEntity.notFound().build();
                } else {
                    return ResponseEntity.ok(program);
                }
            } catch (JsonProcessingException e) {
                log.error("Error parsing Program from user ", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }
}
