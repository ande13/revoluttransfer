package com.revolut.controllers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.controllers.dto.BaseResponse;
import com.revolut.controllers.dto.TransferRequest;
import com.revolut.services.MoneyTransferService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Singleton
@Path("/revolut")
public class MoneyTransferController {

    @Inject
    private MoneyTransferService moneyTransferService;

    @POST
    @Path("/transfermoney")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse transferMoney(TransferRequest transferRequest) {
        return moneyTransferService.transferMoney(transferRequest.getFromAccountId(), transferRequest.getToAccountId(), transferRequest.getAmount());
    }
}
