package com.milos.server;

import com.milos.config.AppConfig;
import com.milos.domain.logger.PrimitiveLogger;
import com.milos.domain.logger.StreamLogger;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {

    public static void main(String[] args) throws IOException, InterruptedException {
        AppConfig appConfig = AppConfig.fromResourceBundle(ResourceBundle.getBundle("config"));
        PrimitiveLogger logger = new StreamLogger(System.out, "server");
        MessageStore messageStore = new MessageStore();

        ServerSocket server = null;
        ExecutorService pool = null;


        MessageProcessor messageProcessor = new MessageProcessor(logger, messageStore);

        try {
            server = new ServerSocket(appConfig.getServerPort().getValue());
            logger.info("The server is running...");

            pool = Executors.newFixedThreadPool(20);

            while (true) {
                pool.execute(new Receiver(server.accept(), messageProcessor));
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }

            if (pool != null) {
                pool.shutdown();
            }
        }
    }
}
