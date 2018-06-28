package com.revolut.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.hibernate.Session;

import javax.persistence.EntityManager;

@Singleton
public class MoneyTransferRepository implements TransferRepository {

    private Session session;

    @Inject
    public void setSession(EntityManager entityManager) {
        this.session = entityManager.unwrap(Session.class);
    }

    @Override
    public AccountEntity getEntityById(long id) {
        return session.get(AccountEntity.class, id);
    }

    @Override
    public void save(AccountEntity accountEntity) {
        session.update(accountEntity);
    }
}
