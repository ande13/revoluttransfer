package com.revolut.services;

import com.google.inject.Inject;
import com.revolut.controllers.dto.TransferRequest;
import com.revolut.controllers.dto.TransferResponse;
import com.revolut.dao.AccountEntity;
import com.revolut.dao.TransferRepository;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MoneyTransferServicePositiveTest extends BaseTest {

    @Inject
    private MoneyTransferService moneyTransferService;
    @Inject
    private TransferRepository transferRepository;

    @Test
    public void moneyTransferTest() throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        List<ResultWrapper> results = new ArrayList<>();

        List<TransferRequest> requests = createRequests();

        requests.forEach(r -> {
            AccountEntity from = transferRepository.getEntityById(r.getFromAccountId());
            AccountEntity to = transferRepository.getEntityById(r.getToAccountId());
            Future<TransferResponse> future = executorService.submit(
                    () -> moneyTransferService.transferMoney(r.getFromAccountId(), r.getToAccountId(), r.getAmount()));
            results.add(new ResultWrapper(from, to, future));
        });

        for (ResultWrapper wrapper : results) {

            while (!wrapper.currentResponse.isDone()) {
                Thread.sleep(100);
            }

            TransferResponse response = wrapper.currentResponse.get();

            AccountEntity previousFromData = wrapper.previousFromData;
            AccountEntity previousToData = wrapper.previousToData;

            Assert.assertEquals(previousFromData.getId(), response.getFromUserId());
            Assert.assertEquals(previousToData.getId(), response.getToUserId());
            Assert.assertEquals(previousFromData.getBalance().subtract(response.getTransferredAmount()), response.getFromUserBalance());
            Assert.assertEquals(previousToData.getBalance().add(response.getTransferredAmount()), response.getToUserBalance());

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

    private static class ResultWrapper {
        private AccountEntity previousFromData;
        private AccountEntity previousToData;
        private Future<TransferResponse> currentResponse;

        ResultWrapper(AccountEntity previousFromData, AccountEntity previousToData, Future<TransferResponse> currentResponse) {
            this.previousFromData = previousFromData;
            this.previousToData = previousToData;
            this.currentResponse = currentResponse;
        }
    }
}
