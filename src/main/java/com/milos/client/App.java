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

        Socket socket = null;

        try {
            socket = new Socket(appConfig.getServerIp().getValue(), appConfig.getServerPort().getValue());
            logger.info("Connected:" + socket);

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
            new Thread(new Sync(socket, accountMessagesQueue, logger)).start();

            // Sync user messages form domain logic
            new Thread(new Sync(socket, userMessagesQueue, logger)).start();

        } catch (Exception e) {
            logger.error(e.getMessage());

            if (socket != null) {
                socket.close();
            }

            logger.close();
        }
    }
}
