package com.revolut.services;

import com.revolut.controllers.dto.TransferResponse;

import java.math.BigDecimal;

public interface MoneyTransferService{
    TransferResponse transferMoney(Long fromAccountId, Long toAccountId, BigDecimal amount);
}
