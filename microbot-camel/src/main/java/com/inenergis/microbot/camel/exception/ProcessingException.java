package com.inenergis.microbot.camel.exception;


public class ProcessingException extends Exception{

    public ProcessingException(Exception e) {
        super(e);
    }

    public ProcessingException(String s) {
        super(s);
    }
}
