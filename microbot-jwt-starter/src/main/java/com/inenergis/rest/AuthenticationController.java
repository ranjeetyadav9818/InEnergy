package com.inenergis.rest;

import com.inenergis.entity.PortalUser;
import com.inenergis.model.UserTokenState;
import com.inenergis.security.TokenHelper;
import com.inenergis.service.PortalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    @Autowired
    TokenHelper tokenHelper;

    @Autowired
    private PortalUserService portalUserService;

    @Value("${jwt.expires_in}")
    private Long EXPIRES_IN;

    @CrossOrigin(origins = "${customer.portal.url}")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity createToken(@RequestParam String username, @RequestParam String password) {

        final PortalUser user = portalUserService.getByEmail(username);
        if (user != null && user.getPassword().equals(password)) {
            String token = tokenHelper.generateToken(username);
            UserTokenState userTokenState = new UserTokenState(token, EXPIRES_IN, user.getEmail(), user.getName(), user.getPhone(), user.getServiceAgreement().getServiceAgreementId());
            return ResponseEntity.ok(userTokenState);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}