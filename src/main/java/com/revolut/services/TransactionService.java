package com.revolut.services;

import com.revolut.dao.entity.TransactionHistoryEntity;

import java.math.BigDecimal;

public interface TransactionService {

    TransactionHistoryEntity getByTransactionId(String transactionId);

    void save(String transactionId, Long fromUser, Long toUser, BigDecimal previousFromUserBalance,
              BigDecimal previousToUserBalance, BigDecimal currentFromUserBalance, BigDecimal currentToUserBalance, BigDecimal amount);
}
