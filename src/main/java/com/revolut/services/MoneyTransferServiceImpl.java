package com.revolut.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import com.revolut.controllers.dto.TransferResponse;
import com.revolut.dao.AccountEntity;
import com.revolut.dao.TransferRepository;

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
            synchronized (toAccountIdLock){
                synchronized (fromAccountIdLock) {
                    return doWork(fromAccountId, toAccountId, amount);
                }
            }
        }
    }

    private TransferResponse doWork(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        AccountEntity from = transferRepository.getEntityById(fromAccountId);
        if (from == null) {
            return new TransferResponse("Can't find account with id: " + fromAccountId);
        }

        BigDecimal accountBalance = from.getAmountOfMoney().subtract(amount);
        if (accountBalance.compareTo(BigDecimal.ZERO) < 0) {
            return new TransferResponse("The operation is not permissible for account: " + fromAccountId);
        }

        from.setAmountOfMoney(accountBalance);

        AccountEntity to = transferRepository.getEntityById(toAccountId);
        to.setAmountOfMoney(to.getAmountOfMoney().add(amount));

        save(from);
        save(to);

        return new TransferResponse("The operation was successfully carried out! From Balance: " + accountBalance + ", To balance: " + to.getAmountOfMoney());
    }

    @Transactional(rollbackOn = Exception.class)
    private void save(AccountEntity accountEntity) {
        transferRepository.save(accountEntity);
    }

}
