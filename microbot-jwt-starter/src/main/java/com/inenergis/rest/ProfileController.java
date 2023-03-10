package com.inenergis.rest;

import com.inenergis.entity.PortalUser;
import com.inenergis.model.PersonalData;
import com.inenergis.model.WebResult;
import com.inenergis.security.TokenHelper;
import com.inenergis.service.PortalUserService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController {

    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private TokenHelper tokenHelper;

    @Autowired
    private PortalUserService portalUserService;

    @CrossOrigin(origins = "${customer.portal.url}")
    @RequestMapping(method = RequestMethod.PUT, path = "/data")
    public ResponseEntity<?> updatePersonalData(HttpServletRequest request, @RequestBody PersonalData personalData) {
        final String token = tokenHelper.getToken(request);
        final String email = tokenHelper.getUsernameFromToken(token);
        PortalUser user = portalUserService.getByEmail(email);
        log.info("portal user before being saved: {}", user);
        if (personalData.getName() != null) {
            user.setName(personalData.getName());
        }
        if (personalData.getPhone() != null) {
            user.setPhone(personalData.getPhone());
        }
        if (personalData.getPassword() != null) {
            if (user.getPassword().equals(personalData.getOldPassword())){
                user.setPassword(personalData.getPassword());
            } else {
                return ResponseEntity.badRequest().body(WebResult.builder().result("wrong password").build());
            }
        }
        portalUserService.save(user);
        log.info("portal user after being saved: {}", user);
        return ResponseEntity.ok().build();
    }


}