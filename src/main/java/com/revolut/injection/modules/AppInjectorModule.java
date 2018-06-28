package com.revolut.injection.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.revolut.controllers.MoneyTransferController;
import com.revolut.dao.MoneyTransferRepository;
import com.revolut.dao.TransferRepository;
import com.revolut.exceptions.AppExceptionMapper;
import com.revolut.exceptions.TransferExceptionMapper;
import com.revolut.services.MoneyTransferService;
import com.revolut.services.MoneyTransferServiceImpl;

public class AppInjectorModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MoneyTransferController.class);
        bind(TransferRepository.class).to(MoneyTransferRepository.class);
        bind(MoneyTransferService.class).to(MoneyTransferServiceImpl.class);
        bind(AppExceptionMapper.class).in(Singleton.class);
        bind(TransferExceptionMapper.class).in(Singleton.class);
    }
}
