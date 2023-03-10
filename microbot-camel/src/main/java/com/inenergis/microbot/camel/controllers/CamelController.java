package com.inenergis.microbot.camel.controllers;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CamelController {
    @Autowired
    private ProducerTemplate producerTemplate;

    @RequestMapping(value = "/")
    public void startCamel() {
        producerTemplate.sendBody("direct:firstRoute", "Calling from Spring Boot Rest Controller");
    }

    @RequestMapping(value = "/pgeForecast")
    public void pgeForecast() {
        producerTemplate.sendBody("direct:forecastRouteTest", "Calling from Spring Boot Rest Controller direct:forecastRouteTest");
    }
}