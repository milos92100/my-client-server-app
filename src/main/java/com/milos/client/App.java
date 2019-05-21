package com.milos.client;

import com.milos.config.AppConfig;
import com.milos.domain.logger.StreamLogger;
import com.milos.domain.Message;

import java.net.Socket;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class App {

    public static void main(String[] args) throws Exception {
        AppConfig appConfig = AppConfig.fromResourceBundle(ResourceBundle.getBundle("config"));
        StreamLogger logger = new StreamLogger(System.out, "client");

        final BlockingQueue<Message> accountMessagesQueue = new ArrayBlockingQueue<Message>(100);
        final BlockingQueue<Message> userMessagesQueue = new ArrayBlockingQueue<Message>(100);

        Socket accountSocket = null;
        Socket userSocket = null;

        try {
            accountSocket = SocketHelper.createFromAppConfig(appConfig);
            logger.info("Account socket connected:" + accountSocket);

            userSocket = SocketHelper.createFromAppConfig(appConfig);
            logger.info("User socket connected:" + userSocket);

            /* This is used only for demonstration of the system,
             * and should be treated or imagined as some integration layer
             * with some other system that provides changes and messages that will
             * be synchronized
             */
            new Thread(new Demo(accountMessagesQueue, userMessagesQueue, 10000)).start();


            /* Sync is used to await and dispatch messages that are created by the
             * Demo layer in this case just a new thread, and check if the server acknowledged
             * every message that was sent, it should do that asynchronously and not block
             * the output stream.
             */

            // Sync account messages form domain logic
            final Socket finalAccountSocket = accountSocket;
            new Thread(
                    new Sync(
                            "AccountSync",
                            accountSocket.getOutputStream(),
                            accountSocket.getInputStream(),
                            accountMessagesQueue,
                            new Sync.LifeCycle() {
                                public void finished() {
                                    SocketHelper.closeWithRetry(finalAccountSocket);

                                }
                            }, logger)).start();

            // Sync user messages form domain logic
            final Socket userAccountSocket = userSocket;
            new Thread(
                    new Sync(
                            "UserSync",
                            userAccountSocket.getOutputStream(),
                            userAccountSocket.getInputStream(),
                            userMessagesQueue,
                            new Sync.LifeCycle() {
                                public void finished() {
                                    SocketHelper.closeWithRetry(userAccountSocket);

                                }
                            }, logger)).start();

        } catch (Exception e) {
            logger.error(e.getMessage());

            SocketHelper.closeWithRetry(accountSocket);
            SocketHelper.closeWithRetry(userSocket);

            logger.close();
        }
    }
}
