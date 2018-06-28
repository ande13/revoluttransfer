package com.revolut.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.persistence.EntityManager;

@Singleton
public class MoneyTransferRepository implements TransferRepository {

    private EntityManager entityManager;

    @Inject
    public MoneyTransferRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public AccountEntity getEntityById(long id) {
        return entityManager.find(AccountEntity.class, id);
    }

    @Override
    public void save(AccountEntity accountEntity) {
        entityManager.refresh(accountEntity);
        entityManager.persist(accountEntity);
    }
}
