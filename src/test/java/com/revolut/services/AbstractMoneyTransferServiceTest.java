package com.revolut.services;

import com.google.inject.Inject;
import com.revolut.controllers.dto.TransferRequest;
import com.revolut.controllers.dto.TransferResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public abstract class AbstractMoneyTransferServiceTest extends BaseTest {

    @Inject
    private MoneyTransferService moneyTransferService;

    public abstract List<TransferRequest> createRequests();

    public List<Future<TransferResponse>> transferMoney() {

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        List<TransferRequest> requests = createRequests();

        return requests.stream().map(r -> executorService.submit(
                () -> moneyTransferService.transferMoney(r.getFromAccountId(), r.getToAccountId(), r.getAmount())
        )).collect(Collectors.toList());
    }

    protected TransferRequest createRequest(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        TransferRequest request = new TransferRequest();
        request.setFromAccountId(fromAccountId);
        request.setToAccountId(toAccountId);
        request.setAmount(amount);
        return request;
    }
}
