package com.revolut;

import com.revolut.server.JettyServer;

public class MoneyTransferApplication {

    public static void main(String[] args) throws Exception {
        new JettyServer().start();
    }
}
