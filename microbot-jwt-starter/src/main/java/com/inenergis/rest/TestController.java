package com.inenergis.rest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestController {

    @CrossOrigin(origins = "${customer.portal.url}")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> test(HttpServletRequest request) {
        return ResponseEntity.ok("{\"success\":true}");
    }
}