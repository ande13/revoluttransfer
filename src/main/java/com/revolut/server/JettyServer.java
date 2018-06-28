package com.revolut.server;

import com.google.inject.servlet.GuiceFilter;
import com.revolut.injection.InjectionContextListener;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServlet;
import java.util.EnumSet;

public class JettyServer {

    private int port;

    public JettyServer() {
        this.port = 8080;
    }

    public JettyServer(int port) {
        this.port = port <= 0 ? 8080 : port;
    }

    public void start() throws Exception {
        Server server = new Server(port);
        ServletHolder servletHolder = new ServletHolder("revolutServlet", new HttpServlet() {});
        servletHolder.setAsyncSupported(false);
        servletHolder.setInitOrder(1);

        WebAppContext webAppContext = new WebAppContext();
        webAppContext.addEventListener(new InjectionContextListener());
        webAppContext.addServlet(servletHolder, "/*");
        webAppContext.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        webAppContext.setResourceBase("resources");

        server.setHandler(webAppContext);

        server.start();
    }
}
