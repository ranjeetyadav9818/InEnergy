package com.inenergis.microbot.camel.exception;

public class LocationProcessingException extends Exception {

    public LocationProcessingException(Exception e) {
        super(e);
    }

    public LocationProcessingException(String s) {
        super(s);
    }
}
