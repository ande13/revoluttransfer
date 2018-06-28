package com.revolut.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.dao.entity.TransactionHistoryEntity;
import org.hibernate.Session;

import javax.persistence.EntityManager;

@Singleton
public class TransactionHistoryRepository implements TransactionRepository {

    private Session session;

    @Inject
    public void setSession(EntityManager entityManager) {
        this.session = entityManager.unwrap(Session.class);
    }

    @Override
    public TransactionHistoryEntity getByTransactionId(String transactionId) {
        return session.get(TransactionHistoryEntity.class, transactionId);
    }

    @Override
    public void save(TransactionHistoryEntity entity) {
        session.save(entity);
    }
}
