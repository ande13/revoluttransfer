package com.revolut.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.controllers.dto.TransferResponse;
import com.revolut.dao.TransferRepository;
import com.revolut.dao.entity.AccountEntity;
import com.revolut.exceptions.TransferException;
import org.hibernate.Session;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;
import java.math.BigDecimal;
import java.util.UUID;

@Singleton
public class MoneyTransferServiceImpl implements MoneyTransferService {

    @Inject
    private TransferRepository transferRepository;
    @Inject
    private TransactionService transactionService;

    private Session session;

    @Inject
    public void setSession(EntityManager entityManager) {
        this.session = entityManager.unwrap(Session.class);
    }

    @Override
    public TransferResponse transferMoney(Long fromAccountId, Long toAccountId, BigDecimal amount) {

        Object fromAccountIdLock = AccountLockFactory.getLock(fromAccountId);
        Object toAccountIdLock = AccountLockFactory.getLock(toAccountId);

        if (fromAccountId > toAccountId) {
            synchronized (fromAccountIdLock) {
                synchronized (toAccountIdLock) {
                    return doWork(fromAccountId, toAccountId, amount);
                }
            }
        } else {
            synchronized (toAccountIdLock) {
                synchronized (fromAccountIdLock) {
                    return doWork(fromAccountId, toAccountId, amount);
                }
            }
        }
    }

    private TransferResponse doWork(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        AccountEntity from = transferRepository.getEntityById(fromAccountId);
        if (from == null) {
            throw new TransferException("Can't find account.", fromAccountId);
        }
        AccountEntity to = transferRepository.getEntityById(toAccountId);
        if (to == null) {
            throw new TransferException("Can't find account.", toAccountId);
        }
        BigDecimal previousFromAccountBalance = from.getBalance();
        BigDecimal currentFromAccountBalance = previousFromAccountBalance.subtract(amount);
        if (currentFromAccountBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new TransferException("The operation is not permissible for account.", fromAccountId);
        }

        from.setBalance(currentFromAccountBalance);

        BigDecimal previousToAccountBalance = to.getBalance();
        BigDecimal currentToAccountBalance = previousToAccountBalance.add(amount);
        to.setBalance(currentToAccountBalance);

        String transactionId = UUID.randomUUID().toString();

        try {
            session.getTransaction().begin();
            transferRepository.save(from);
            transferRepository.save(to);
            session.getTransaction().commit();
        } catch (EntityExistsException | IllegalArgumentException| TransactionRequiredException ex) {
            session.getTransaction().rollback();
        }

        transactionService.save(transactionId, fromAccountId, toAccountId, previousFromAccountBalance, previousToAccountBalance, currentFromAccountBalance, currentToAccountBalance, amount);

        return new TransferResponse("The operation was successfully carried out!", fromAccountId, currentFromAccountBalance, toAccountId, currentToAccountBalance, amount, transactionId);
    }

}
