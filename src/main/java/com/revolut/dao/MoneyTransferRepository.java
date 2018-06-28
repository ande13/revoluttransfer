package com.revolut.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.hibernate.Session;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;

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
        Session unwrap = entityManager.unwrap(Session.class);
        unwrap.getTransaction().begin();
        try {
            entityManager.persist(accountEntity);
            unwrap.getTransaction().commit();
        } catch (EntityExistsException | IllegalArgumentException| TransactionRequiredException ex) {
            unwrap.getTransaction().rollback();
        }
    }
}
