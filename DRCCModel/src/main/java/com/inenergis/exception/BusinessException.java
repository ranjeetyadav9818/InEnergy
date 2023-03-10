package com.inenergis.exception;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

@Data
public class BusinessException extends Exception {

    private ExceptionCode code;
    private String businessMessage;
    private List<String> businessInfo;
    private Throwable wrappedException;

    public enum ExceptionCode {
        ALREADY_ENROLLED,
        RELATED_ENTITY_MISSING,
        CAN_NOT_CONNECT_TO_REMOTE_SERVICE;
    }

    public BusinessException(ExceptionCode code, String businessMessage, List<String> businessInfo) {
        super(MessageFormat.format(StringUtils.defaultIfEmpty(businessMessage, StringUtils.EMPTY), businessInfo == null ? Collections.EMPTY_LIST : businessInfo));
        this.code = code;
        this.businessMessage = businessMessage;
        this.businessInfo = businessInfo;
    }
}

