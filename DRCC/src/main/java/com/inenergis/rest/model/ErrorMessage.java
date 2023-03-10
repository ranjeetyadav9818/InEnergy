package com.inenergis.rest.model;

import lombok.Data;

@Data
public class ErrorMessage {
    private String code;
    private String message;
    private int status;
}
