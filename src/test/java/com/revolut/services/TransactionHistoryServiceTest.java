package com.revolut.services;

import com.google.inject.Inject;
import com.revolut.dao.entity.TransactionHistoryEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@RunWith(Parameterized.class)
public class TransactionHistoryServiceTest extends BaseTest {

    @Inject
    private TransactionService transactionService;

    @Parameterized.Parameter
    public String transactionId;
    @Parameterized.Parameter(value = 1)
    public Long fromUser;
    @Parameterized.Parameter(value = 2)
    public Long toUser;
    @Parameterized.Parameter(value = 3)
    public BigDecimal previousFromUserBalance;
    @Parameterized.Parameter(value = 4)
    public BigDecimal previousToUserBalance;
    @Parameterized.Parameter(value = 5)
    public BigDecimal currentFromUserBalance;
    @Parameterized.Parameter(value = 6)
    public BigDecimal currentToUserBalance;
    @Parameterized.Parameter(value = 7)
    public BigDecimal amount;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Collection<Object[]> params = new ArrayList<>();
        params.add(new Object[]{UUID.randomUUID().toString(), 123L, 124L, new BigDecimal(2000),
                new BigDecimal(1000), new BigDecimal(1000), new BigDecimal(2000), new BigDecimal(1000)});
        params.add(new Object[]{UUID.randomUUID().toString(), null, null, null, null, null, null, null});
        return params;
    }

    @Test
    public void transactionHistoryServiceTest() {
        transactionService.save(transactionId, fromUser, toUser, previousFromUserBalance,
                previousToUserBalance, currentFromUserBalance, currentToUserBalance, amount);

        TransactionHistoryEntity historyEntity = transactionService.getByTransactionId(transactionId);
        Assert.assertNotNull(historyEntity);
        Assert.assertEquals(transactionId, historyEntity.getTransactionId());
        Assert.assertEquals(fromUser, historyEntity.getFromUser());
        Assert.assertEquals(toUser, historyEntity.getToUser());
        Assert.assertEquals(previousFromUserBalance, historyEntity.getPreviousFromUserBalance());
        Assert.assertEquals(previousToUserBalance, historyEntity.getPreviousToUserBalance());
        Assert.assertEquals(currentFromUserBalance, historyEntity.getCurrentFromUserBalance());
        Assert.assertEquals(currentToUserBalance, historyEntity.getCurrentToUserBalance());
        Assert.assertEquals(amount, historyEntity.getAmount());
    }
}
