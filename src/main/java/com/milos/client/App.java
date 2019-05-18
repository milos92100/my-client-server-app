package com.milos.client;

import com.milos.config.AppConfig;
import com.milos.domain.logger.PrimitiveLogger;
import com.milos.domain.logger.StreamLogger;

import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.ResourceBundle;

import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class App {

    public static Random rnd = new Random();

    public static String generateRandomMsg() {
//        byte[] array = new byte[7]; // length is bounded by 7
////        rnd.nextBytes(array);
////        return new String(array, Charset.forName("UTF-8"));
        return UUID.randomUUID().toString();
    }

    public static void main(String[] args) throws Exception {
        AppConfig appConfig = AppConfig.fromResourceBundle(ResourceBundle.getBundle("config"));
        PrimitiveLogger logger = new StreamLogger(System.out, "client");

        final BlockingQueue<String> messagesToSend = new ArrayBlockingQueue<String>(50);
        Socket socket = null;

        try {
            socket = new Socket(appConfig.getServerIp().getValue(), appConfig.getServerPort().getValue());
            logger.info("Connected:" + socket);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 10; i++) {
                        try {

                            messagesToSend.put(generateRandomMsg());
                            Thread.sleep(0);
                        } catch (InterruptedException e) { /* NOP */ }
                    }
                }
            }).start();

            Sender sender = new Sender(socket.getOutputStream(), messagesToSend, logger);
            Receiver receiver = new Receiver(socket.getInputStream(), logger);

            receiver.start();
            sender.start();

        } catch (Exception e) {
            logger.error(e.getMessage());

            if (socket != null) {
                socket.close();
            }
        }
    }


}
