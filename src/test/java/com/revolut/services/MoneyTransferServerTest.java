package com.revolut.services;

import com.google.inject.Inject;
import com.revolut.controllers.dto.TransferRequest;
import com.revolut.controllers.dto.TransferResponse;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MoneyTransferServerTest extends BaseTest {

    @Inject
    private MoneyTransferService moneyTransferService;

    @Test
    public void moneyTransferTest() throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        List<Future<TransferResponse>> futures = new ArrayList<>();

        List<TransferRequest> requests = createRequests();

        requests.forEach(r -> futures.add(executorService.submit(
                () -> moneyTransferService.transferMoney(r.getFromAccountId(), r.getToAccountId(), r.getAmount())
        )));

        List<TransferResponse> responses = new ArrayList<>();

        for (Future<TransferResponse> future : futures) {
            while (!future.isDone()) {
                Thread.sleep(100);
            }
            responses.add(future.get());
        }

        System.out.println(responses);
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
