package com.revolut.services;

import com.revolut.controllers.dto.BaseResponse;

import java.math.BigDecimal;

public interface MoneyTransferService{
    BaseResponse transferMoney(Long fromAccountId, Long toAccountId, BigDecimal amount);
}
