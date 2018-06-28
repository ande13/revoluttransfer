package com.revolut.exceptions;

import com.revolut.controllers.dto.ErrorResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class TransferExceptionMapper implements ExceptionMapper<TransferException> {

    @Override
    public Response toResponse(TransferException e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse(e.getMessage(), e.getUserId())).type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
