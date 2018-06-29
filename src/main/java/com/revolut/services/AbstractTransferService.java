package com.revolut.services;

import com.google.inject.Inject;
import org.hibernate.Session;

import javax.persistence.EntityManager;

public abstract class AbstractTransferService {

    private Session session;

    @Inject
    public void setSession(EntityManager entityManager) {
        this.session = entityManager.unwrap(Session.class);
    }

    protected Session getSession() {
        return session;
    }
}
