package com.jazzkuh.m0nitor;

import lombok.SneakyThrows;

public class Bootstrap {
    @SneakyThrows
    public static void main(String[] args) {
        MacCaffeinate.start();

        Deamon deamon = new Deamon();
        Runtime.getRuntime().addShutdownHook(new Thread(deamon::onShutdown));
    }
}
