package com.revolut.services;

import com.google.inject.*;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.revolut.dao.MoneyTransferRepository;
import com.revolut.dao.TransferRepository;
import org.junit.After;
import org.junit.Before;

public class BaseTest {

    private Injector injector;

    @Before
    public void setUp() {
        injector = Guice.createInjector(new AbstractModule() {

            @Override
            protected void configure() {
                bind(TransferRepository.class).to(MoneyTransferRepository.class);
                bind(MoneyTransferService.class).to(MoneyTransferServiceImpl.class);
                install(new JpaPersistModule("db-manager"));
                bind(JPAInitializer.class).asEagerSingleton();
            }
        });

        injector.injectMembers(this);
    }

    @After
    public void tearDown() {
        injector = null;
    }

    @Singleton
    public static class JPAInitializer {

        @Inject
        public JPAInitializer(final PersistService service) {
            service.start();
        }

    }
}
