package com.milos.server;

import com.milos.config.AppConfig;
import com.milos.domain.logger.PrimitiveLogger;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Program {

    protected AppConfig appConfig;
    protected PrimitiveLogger logger;

    public Program(AppConfig appConfig, PrimitiveLogger logger) {
        this.appConfig = appConfig;
        this.logger = logger;
    }

    public void run() throws IOException, InterruptedException {
        ServerSocket listener = null;
        ExecutorService pool = null;

        try {
            listener = new ServerSocket(appConfig.getServerPort().getValue());
            logger.info("The server is running...");
            pool = Executors.newFixedThreadPool(20);

            while (true) {
                pool.execute(new ClientHandler(listener.accept()));
            }
        } finally {
            if (listener != null) {
                listener.close();
            }

            if (pool != null) {
                pool.shutdown();
                if (pool.awaitTermination(5, TimeUnit.SECONDS)) {

                }
            }
        }
    }
}
