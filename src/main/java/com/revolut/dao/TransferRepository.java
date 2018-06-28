package com.revolut.dao;

public interface TransferRepository {

    AccountEntity getEntityById(long id);

    void save(AccountEntity accountEntity);
}
