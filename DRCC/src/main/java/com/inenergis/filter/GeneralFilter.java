package com.inenergis.filter;

import org.picketlink.Identity;
import org.picketlink.idm.model.basic.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Antonio on 15/08/2017.
 */
@WebFilter("/*")
public class GeneralFilter implements Filter {

    @Inject
    Identity identity;

    private static final Logger log = LoggerFactory.getLogger(GeneralFilter.class);

    @Override
    public void init(FilterConfig config) {
        // Initialize global variables if necessary.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (identity.getAccount() != null) {
            MDC.put("username", ((User) identity.getAccount()).getEmail());
        }
        MDC.put("requestUID", UUID.randomUUID().toString());
        log.debug("request received to {}", ((HttpServletRequest) request).getRequestURL().toString());
        chain.doFilter(request, response);
        log.debug("response dispatched from "+ ((HttpServletRequest)request).getRequestURL().toString());
        MDC.clear();
    }

    @Override
    public void destroy() {
        // Cleanup global variables if necessary.
        MDC.clear();
    }
}