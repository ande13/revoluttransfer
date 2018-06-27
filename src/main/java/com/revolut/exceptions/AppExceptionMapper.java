package com.revolut.exceptions;

import com.revolut.controllers.dto.TransferResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AppExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception e) {
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setMessage(e.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(transferResponse).type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
