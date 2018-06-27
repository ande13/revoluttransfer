package com.revolut.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.controllers.dto.TransferResponse;
import com.revolut.dao.MoneyTransferDAO;
import com.revolut.exceptions.TransferException;

import java.math.BigDecimal;

@Singleton
public class MoneyTransferServiceImpl implements MoneyTransferService {

    @Inject
    private MoneyTransferDAO moneyTransferDAO;

    @Override
    public TransferResponse transferMoney(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        throw new TransferException("Some error occured.");
//        return new TransferResponse();
    }


}
