package com.milos.server;

import com.milos.config.AppConfig;
import com.milos.domain.MessageStats;
import com.milos.domain.logger.PrimitiveLogger;
import com.milos.domain.logger.StreamLogger;
import com.milos.domain.worker.DomainWorker;
import com.milos.domain.worker.StatsWorker;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {

    public static void main(String[] args) {
        AppConfig appConfig = AppConfig.fromResourceBundle(ResourceBundle.getBundle("config"));
        PrimitiveLogger logger = new StreamLogger(System.out, "server");

        MessageStore messageStore = new MessageStore();
        SharedState sharedState = new SharedState();

        /*
         * messageStatsQueue is used to store information about the
         * message, for example duration of processing the message and
         * average duration of all messages that was captured at that time
         */
        final BlockingQueue<MessageStats> messageStatsQueue = new ArrayBlockingQueue<MessageStats>(100);

        MessageProcessor messageProcessor = new MessageProcessor(logger, messageStore);

        /**
         * Domain workers are responsible for processing incoming messages to the queue.
         * Each worker is responsible for one type of messages there for separate queues.
         */
        DomainWorker accountWorker = new DomainWorker(messageStore.getAccountMessagesQueue(), messageStatsQueue, sharedState, logger);
        DomainWorker userWorker = new DomainWorker(messageStore.getUserMessagesQueue(), messageStatsQueue, sharedState, logger);

        /**
         * Stats worker polls messages form his queue and displays in the console
         * as formatted text with information about processed messages.
         */
        StatsWorker statsWorker = new StatsWorker(messageStatsQueue, System.out);

        ServerSocket server = null;
        ExecutorService pool = null;

        try {
            server = new ServerSocket(appConfig.getServerPort().getValue());
            logger.info("The server is running...");

            new Thread(accountWorker).start();
            new Thread(userWorker).start();

            new Thread(statsWorker).start();

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
