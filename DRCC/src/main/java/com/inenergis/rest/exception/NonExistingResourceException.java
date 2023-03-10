package com.inenergis.rest.exception;

/**
 * Created by Antonio on 06/03/2017.
 */
public class NonExistingResourceException extends RuntimeException {
    public NonExistingResourceException(String s) {
        super(s);
    }
}
