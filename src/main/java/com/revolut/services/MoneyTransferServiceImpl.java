package com.revolut.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import com.revolut.controllers.dto.BaseResponse;
import com.revolut.controllers.dto.TransferResponse;
import com.revolut.dao.AccountEntity;
import com.revolut.dao.TransferRepository;

import java.math.BigDecimal;

@Singleton
public class MoneyTransferServiceImpl implements MoneyTransferService {

    @Inject
    private TransferRepository transferRepository;

    @Override
    public BaseResponse transferMoney(Long fromAccountId, Long toAccountId, BigDecimal amount) {

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

    private BaseResponse doWork(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        AccountEntity from = transferRepository.getEntityById(fromAccountId);
        if (from == null) {
            return new BaseResponse("Can't find account.", fromAccountId);
        }
        AccountEntity to = transferRepository.getEntityById(toAccountId);
        if (to == null) {
            return new BaseResponse("Can't find account.", toAccountId);
        }

        BigDecimal fromAccountBalance = from.getBalance().subtract(amount);
        if (fromAccountBalance.compareTo(BigDecimal.ZERO) < 0) {
            return new BaseResponse("The operation is not permissible for account.", fromAccountId);
        }

        from.setBalance(fromAccountBalance);

        BigDecimal toAccountBalance = to.getBalance().add(amount);
        to.setBalance(toAccountBalance);

        save(from);
        save(to);

        return new TransferResponse("The operation was successfully carried out!", fromAccountId, fromAccountBalance, toAccountId, toAccountBalance, amount);
    }

    @Transactional(rollbackOn = Exception.class)
    private void save(AccountEntity accountEntity) {
        transferRepository.save(accountEntity);
    }

}
