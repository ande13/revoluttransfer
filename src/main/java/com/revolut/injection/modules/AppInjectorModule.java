package com.revolut.injection.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.revolut.controllers.MoneyTransferController;
import com.revolut.dao.MoneyTransferRepository;
import com.revolut.dao.TransactionHistoryRepository;
import com.revolut.dao.TransactionRepository;
import com.revolut.dao.TransferRepository;
import com.revolut.exceptions.AppExceptionMapper;
import com.revolut.exceptions.TransferExceptionMapper;
import com.revolut.services.MoneyTransferService;
import com.revolut.services.MoneyTransferServiceImpl;
import com.revolut.services.TransactionHistoryService;
import com.revolut.services.TransactionService;

public class AppInjectorModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MoneyTransferController.class);
        bind(TransferRepository.class).to(MoneyTransferRepository.class);
        bind(TransactionRepository.class).to(TransactionHistoryRepository.class);
        bind(MoneyTransferService.class).to(MoneyTransferServiceImpl.class);
        bind(AppExceptionMapper.class).in(Singleton.class);
        bind(TransferExceptionMapper.class).in(Singleton.class);
        bind(TransactionService.class).to(TransactionHistoryService.class);
    }
}
