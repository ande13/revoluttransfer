package com.revolut.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.dao.TransactionRepository;
import com.revolut.dao.entity.TransactionHistoryEntity;
import org.hibernate.Transaction;

import javax.persistence.EntityExistsException;
import javax.persistence.TransactionRequiredException;
import java.math.BigDecimal;

@Singleton
public class TransactionHistoryService extends AbstractTransferService implements TransactionService {

    @Inject
    private TransactionRepository transactionRepository;

    @Override
    public TransactionHistoryEntity getByTransactionId(String transactionId) {
        return transactionRepository.getByTransactionId(transactionId);
    }

    @Override
    public void save(String transactionId, Long fromUser, Long toUser, BigDecimal previousFromUserBalance,
                     BigDecimal previousToUserBalance, BigDecimal currentFromUserBalance, BigDecimal currentToUserBalance, BigDecimal amount) {

        TransactionHistoryEntity transactionHistoryEntity = new TransactionHistoryEntity();
        transactionHistoryEntity.setTransactionId(transactionId);
        transactionHistoryEntity.setFromUser(fromUser);
        transactionHistoryEntity.setToUser(toUser);
        transactionHistoryEntity.setPreviousFromUserBalance(previousFromUserBalance);
        transactionHistoryEntity.setCurrentFromUserBalance(currentFromUserBalance);
        transactionHistoryEntity.setPreviousToUserBalance(previousToUserBalance);
        transactionHistoryEntity.setCurrentToUserBalance(currentToUserBalance);
        transactionHistoryEntity.setAmount(amount);

        Transaction transaction = getSession().getTransaction();

        try {
            transaction.begin();
            transactionRepository.save(transactionHistoryEntity);
            transaction.commit();
        } catch (EntityExistsException | IllegalArgumentException| TransactionRequiredException ex) {
            transaction.rollback();
        }
    }
}
