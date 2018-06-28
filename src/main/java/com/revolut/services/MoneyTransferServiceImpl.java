package com.revolut.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.controllers.dto.TransferResponse;
import com.revolut.dao.AccountEntity;
import com.revolut.dao.TransferRepository;
import com.revolut.exceptions.TransferException;

import java.math.BigDecimal;

@Singleton
public class MoneyTransferServiceImpl implements MoneyTransferService {

    @Inject
    private TransferRepository transferRepository;

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

        BigDecimal fromAccountBalance = from.getBalance().subtract(amount);
        if (fromAccountBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new TransferException("The operation is not permissible for account.", fromAccountId);
        }

        from.setBalance(fromAccountBalance);

        BigDecimal toAccountBalance = to.getBalance().add(amount);
        to.setBalance(toAccountBalance);

        save(from);
        save(to);

        return new TransferResponse("The operation was successfully carried out!", fromAccountId, fromAccountBalance, toAccountId, toAccountBalance, amount);
    }

    private void save(AccountEntity accountEntity) {
        transferRepository.save(accountEntity);
    }

}
