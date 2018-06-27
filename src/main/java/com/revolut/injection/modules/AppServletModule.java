package com.revolut.injection.modules;

import com.google.inject.persist.PersistFilter;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class AppServletModule extends ServletModule {

    @Override
    protected void configureServlets() {
        serve("*").with(GuiceContainer.class);
        filter("/*").through(PersistFilter.class);
    }
}
