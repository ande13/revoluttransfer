package com.revolut.services;

import com.google.inject.Inject;
import com.revolut.controllers.dto.TransferRequest;
import com.revolut.controllers.dto.TransferResponse;
import com.revolut.dao.entity.TransactionHistoryEntity;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MoneyTransferServicePositiveTest extends AbstractMoneyTransferServiceTest {

    @Inject
    private TransactionService transactionService;

    @Test
    public void moneyTransferTest() throws ExecutionException, InterruptedException {

        for (Future<TransferResponse> result : transferMoney()) {

            while (!result.isDone()) {
                Thread.sleep(100);
            }

            TransferResponse response = result.get();

            TransactionHistoryEntity historyEntity = transactionService.getByTransactionId(response.getTransactionId());
            assertNotNull(historyEntity);
            assertEquals(historyEntity.getFromUser(), response.getFromUserId());
            assertEquals(historyEntity.getToUser(), response.getToUserId());
            assertEquals(historyEntity.getPreviousFromUserBalance().subtract(response.getTransferredAmount()), response.getFromUserBalance());
            assertEquals(historyEntity.getPreviousToUserBalance().add(response.getTransferredAmount()), response.getToUserBalance());
            assertEquals(historyEntity.getAmount(), response.getTransferredAmount());
            assertEquals(historyEntity.getCurrentFromUserBalance(), response.getFromUserBalance());
            assertEquals(historyEntity.getCurrentToUserBalance(), response.getToUserBalance());
        }

    }

    public List<TransferRequest> createRequests() {
        List<TransferRequest> requests = new ArrayList<>();
        requests.add(createRequest(123L, 124L, new BigDecimal(1000)));
        requests.add(createRequest(124L, 123L, new BigDecimal(2500)));
        requests.add(createRequest(125L, 123L, new BigDecimal(1455)));
        requests.add(createRequest(125L, 123L, new BigDecimal(1455)));
        requests.add(createRequest(125L, 123L, new BigDecimal(1455)));
        return requests;
    }
}
