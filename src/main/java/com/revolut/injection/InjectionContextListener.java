package com.revolut.injection;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;
import com.revolut.injection.modules.AppInjectorModule;
import com.revolut.injection.modules.AppServletModule;

public class InjectionContextListener extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new AppServletModule(), new AppInjectorModule(), new JpaPersistModule("db-manager"));
    }
}
