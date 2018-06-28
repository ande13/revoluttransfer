package com.revolut.dao;

import com.revolut.dao.entity.AccountEntity;

public interface TransferRepository {

    AccountEntity getEntityById(long id);

    void save(AccountEntity accountEntity);
}
