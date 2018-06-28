package com.revolut.dao;

import com.revolut.dao.entity.TransactionHistoryEntity;

public interface TransactionRepository {

    TransactionHistoryEntity getByTransactionId(String transactionId);

    void save(TransactionHistoryEntity entity);
}
