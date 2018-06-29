package com.revolut.services;

import com.google.inject.Inject;
import com.revolut.exceptions.TransferException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

@RunWith(Parameterized.class)
public class MoneyTransferServiceExceptionsTest extends BaseTest {

    @Inject
    private MoneyTransferService moneyTransferService;

    @Parameterized.Parameter
    public Long fromAccountId;
    @Parameterized.Parameter(value = 1)
    public Long toAccountId;
    @Parameterized.Parameter(value = 2)
    public BigDecimal amount;
    @Parameterized.Parameter(value = 3)
    public String expectedMessage;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Collection<Object[]> params = new ArrayList<>();
        params.add(new Object[]{123L, null, new BigDecimal(2000), "To account is not set."});
        params.add(new Object[]{null, 124L, new BigDecimal(2000), "From account is not set."});
        params.add(new Object[]{123L, 124L, null, "Amount is not set."});
        params.add(new Object[]{123L, 124L, new BigDecimal(0), "Amount is not set."});
        params.add(new Object[]{null, null, null, "From account is not set."});
        params.add(new Object[]{123L, 2000L, new BigDecimal(1000), "Can't find account."});
        params.add(new Object[]{1000L, 124L, new BigDecimal(1000), "Can't find account."});
        params.add(new Object[]{123L, 124L, new BigDecimal(100000), "The operation is not permissible for account."});
        params.add(new Object[]{123L, 123L, new BigDecimal(1000), "Transfer within your account is not supported."});
        return params;
    }

    @Test
    public void moneyTransferExceptionTest() {
        try {
            moneyTransferService.transferMoney(fromAccountId, toAccountId, amount);
        } catch (TransferException ex) {
            Assert.assertEquals(ex.getMessage(), expectedMessage);
        }
    }
}
