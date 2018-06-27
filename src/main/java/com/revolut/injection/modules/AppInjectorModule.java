package com.revolut.injection.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.revolut.controllers.MoneyTransferController;
import com.revolut.dao.MoneyTransferDAO;
import com.revolut.dao.MoneyTransferDAOImpl;
import com.revolut.exceptions.AppExceptionMapper;
import com.revolut.services.MoneyTransferService;
import com.revolut.services.MoneyTransferServiceImpl;

public class AppInjectorModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MoneyTransferController.class);
        bind(MoneyTransferDAO.class).to(MoneyTransferDAOImpl.class);
        bind(MoneyTransferService.class).to(MoneyTransferServiceImpl.class);
        bind(AppExceptionMapper.class).in(Singleton.class);
    }
}
