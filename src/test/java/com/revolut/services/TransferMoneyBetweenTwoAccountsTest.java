package com.revolut.services;

import com.google.inject.Inject;
import com.revolut.controllers.dto.TransferRequest;
import com.revolut.controllers.dto.TransferResponse;
import com.revolut.dao.TransferRepository;
import com.revolut.dao.entity.AccountEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

@RunWith(Parameterized.class)
public class TransferMoneyBetweenTwoAccountsTest extends AbstractMoneyTransferServiceTest {

    @Inject
    private TransferRepository transferRepository;

    @Parameterized.Parameter
    public Long firstAccountId;
    @Parameterized.Parameter(value = 1)
    public Long secondAccountId;
    @Parameterized.Parameter(value = 2)
    public BigDecimal expectedFirstBalance;
    @Parameterized.Parameter(value = 3)
    public BigDecimal expectedSecondBalance;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Collection<Object[]> params = new ArrayList<>();
        params.add(new Object[]{
                123L,
                124L,
                new BigDecimal(19500.22).setScale(2, BigDecimal.ROUND_HALF_UP),
                new BigDecimal(38940.43).setScale(2, BigDecimal.ROUND_HALF_UP)
        });
        return params;
    }


    @Test
    public void transferMoneyBetweenTwoAccountsTest() throws InterruptedException {

        for (Future<TransferResponse> result : transferMoney()) {
            while (!result.isDone()) {
                Thread.sleep(100);
            }
        }

        AccountEntity firstEntity = transferRepository.getEntityById(firstAccountId);
        Assert.assertNotNull(firstEntity);
        Assert.assertEquals(firstEntity.getId(), firstAccountId);
        Assert.assertEquals(firstEntity.getBalance(), expectedFirstBalance);

        AccountEntity secondEntity = transferRepository.getEntityById(secondAccountId);
        Assert.assertNotNull(secondEntity);
        Assert.assertEquals(secondEntity.getId(), secondAccountId);
        Assert.assertEquals(secondEntity.getBalance(), expectedSecondBalance);
    }

    @Override
    public List<TransferRequest> createRequests() {
        List<TransferRequest> requests = new ArrayList<>();
        requests.add(createRequest(firstAccountId, secondAccountId, new BigDecimal(1000)));
        requests.add(createRequest(secondAccountId, firstAccountId, new BigDecimal(2500)));
        requests.add(createRequest(firstAccountId, secondAccountId, new BigDecimal(2000)));
        requests.add(createRequest(secondAccountId, firstAccountId, new BigDecimal(500)));
        requests.add(createRequest(secondAccountId, firstAccountId, new BigDecimal(1500)));
        requests.add(createRequest(secondAccountId, firstAccountId, new BigDecimal(3500)));
        requests.add(createRequest(firstAccountId, secondAccountId, new BigDecimal(1800)));
        requests.add(createRequest(firstAccountId, secondAccountId, new BigDecimal(800)));
        requests.add(createRequest(secondAccountId, firstAccountId, new BigDecimal(4200)));
        return requests;
    }
}
