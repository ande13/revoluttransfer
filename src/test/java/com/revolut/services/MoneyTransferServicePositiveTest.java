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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MoneyTransferServicePositiveTest extends BaseTest {

    @Inject
    private MoneyTransferService moneyTransferService;
    @Inject
    private TransactionService transactionService;

    @Test
    public void moneyTransferTest() throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        List<Future<TransferResponse>> results = new ArrayList<>();

        List<TransferRequest> requests = createRequests();

        requests.forEach(r -> results.add(executorService.submit(() -> moneyTransferService.transferMoney(r.getFromAccountId(), r.getToAccountId(), r.getAmount()))));

        for (Future<TransferResponse> result : results) {

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

    private List<TransferRequest> createRequests() {
        List<TransferRequest> requests = new ArrayList<>();
        requests.add(createRequest(123L, 124L, new BigDecimal(1000)));
        requests.add(createRequest(124L, 123L, new BigDecimal(2500)));
        requests.add(createRequest(125L, 123L, new BigDecimal(1455)));
        requests.add(createRequest(125L, 123L, new BigDecimal(1455)));
        requests.add(createRequest(125L, 123L, new BigDecimal(1455)));
        return requests;
    }

    private TransferRequest createRequest(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        TransferRequest request = new TransferRequest();
        request.setFromAccountId(fromAccountId);
        request.setToAccountId(toAccountId);
        request.setAmount(amount);
        return request;
    }
}
