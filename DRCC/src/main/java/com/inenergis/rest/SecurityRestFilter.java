package com.inenergis.rest;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;

@Provider
@PreMatching
public class SecurityRestFilter implements ContainerRequestFilter {

    Logger log = LoggerFactory.getLogger(SecurityRestFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        return;
//        try{
//            final String authorisation = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
//            String base64Credentials = authorisation.substring("Basic".length()).trim();
//            String credentials = new String(Base64.getDecoder().decode(base64Credentials), Charset.forName("UTF-8"));
//            // credentials = username:password
//            final String[] values = credentials.split(":",2);
//            final String username = values[0];
//            final String password = values[1];
//            if(username!=null && password !=null){
//                if(username.equals("pge") && password.equals("oXdu8sU7hzAtZe6")){
//                    return;
//                }
//            }
//        } catch (Exception e){
//            log.info("exception thrown in rest authentication filter", e);
//        }
//        requestContext.abortWith(Response
//                .status(Response.Status.UNAUTHORIZED)
//                .entity("User cannot access the resource.")
//                .build());
    }
}
