package com.revolut.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.persistence.EntityManager;

@Singleton
public class MoneyTransferDAOImpl implements MoneyTransferDAO {

    private EntityManager entityManager;

    @Inject
    public MoneyTransferDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public AccountEntity getAccountEntityById(long id) {
        return entityManager.find(AccountEntity.class, id);
    }
}
