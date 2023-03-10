package com.inenergis.controller.observers;



import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.picketlink.config.SecurityConfigurationBuilder;
import org.picketlink.event.SecurityConfigurationEvent;

public class HttpSecurityConfiguration {

	
	  @Inject
	  private Logger log;

	
    public void onInit(@Observes SecurityConfigurationEvent event) {
        SecurityConfigurationBuilder builder = event.getBuilder();
        log.warn("Building Security conf");
        builder
            .http()
                .allPaths()
                    .authenticateWith()
                        .form()
                            .authenticationUri("/login.jsf")
                            .loginPage("/login.jsf")
                            .errorPage("/error.jsf")
                            .restoreOriginalRequest()
                .forPath("/javax.faces.resource/*")
                    .unprotected()
                .forPath("/logout")
                    .logout()
                    .redirectTo("/login.jsf")
                .forPath("/index.jsf")
                    .unprotected()
                .forPath("/recoverPassword.jsf")
                    .unprotected()
                .forPath("/rest/*")
                    .unprotected();
    }

}