package com.revolut.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.controllers.dto.TransferResponse;
import com.revolut.dao.TransferRepository;
import com.revolut.dao.entity.AccountEntity;
import com.revolut.exceptions.TransferException;
import org.hibernate.Transaction;

import javax.persistence.EntityExistsException;
import javax.persistence.TransactionRequiredException;
import java.math.BigDecimal;
import java.util.UUID;

@Singleton
public class MoneyTransferServiceImpl extends AbstractTransferService implements MoneyTransferService {

    @Inject
    private TransferRepository transferRepository;
    @Inject
    private TransactionService transactionService;

    @Override
    public TransferResponse transferMoney(Long fromAccountId, Long toAccountId, BigDecimal amount) {

        if(fromAccountId == null) {
            throw new TransferException("From account is not set.", null);
        }

        if(toAccountId == null) {
            throw new TransferException("To account is not set.", null);
        }

        if(amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
            throw new TransferException("Amount is not set.", fromAccountId);
        }

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

        Transaction transaction = getSession().getTransaction();

        try {
            transaction.begin();
            transferRepository.save(from);
            transferRepository.save(to);
            transaction.commit();
        } catch (EntityExistsException | IllegalArgumentException| TransactionRequiredException ex) {
            transaction.rollback();
        }

        transactionService.save(transactionId, fromAccountId, toAccountId, previousFromAccountBalance, previousToAccountBalance, currentFromAccountBalance, currentToAccountBalance, amount);

        return new TransferResponse("The operation was successfully carried out!", fromAccountId, currentFromAccountBalance, toAccountId, currentToAccountBalance, amount, transactionId);
    }

}
