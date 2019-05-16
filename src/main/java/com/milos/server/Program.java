package com.milos.server;

import com.milos.config.AppConfig;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Program {

    protected AppConfig appConfig;

    public Program(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public void run() throws IOException {
        ServerSocket listener = null;

        try {
            listener = new ServerSocket(appConfig.getServerPort().getValue());
            System.out.println("The server is running...");
            ExecutorService pool = Executors.newFixedThreadPool(20);
            while (true) {
                pool.execute(new MessageProcessor(listener.accept()));
            }
        } finally {
            if (listener != null) {
                listener.close();
            }
        }
    }
}
