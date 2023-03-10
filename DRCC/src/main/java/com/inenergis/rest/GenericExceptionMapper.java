package com.inenergis.rest;

import com.inenergis.controller.general.ConstantsProvider;
import com.inenergis.rest.exception.NonExistingResourceException;
import com.inenergis.rest.model.ErrorMessage;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable>{
    @Override
    public Response toResponse(Throwable ex) {

        ErrorMessage errorMessage = new ErrorMessage();
        setHttpStatus(ex, errorMessage);
        errorMessage.setCode(ConstantsProvider.REST_ERROR_CODE);
        errorMessage.setMessage(ex.getMessage());

        return Response.status(errorMessage.getStatus())
                .entity(errorMessage)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private void setHttpStatus(Throwable ex, ErrorMessage errorMessage) {
        if(ex instanceof WebApplicationException ) {
            errorMessage.setStatus(((WebApplicationException)ex).getResponse().getStatus());
        } if(ex.getCause() instanceof NonExistingResourceException) {
            errorMessage.setStatus(Response.Status.NOT_FOUND.getStatusCode());
        } else {
            errorMessage.setStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()); //defaults to internal server error 500
        }
    }



}
