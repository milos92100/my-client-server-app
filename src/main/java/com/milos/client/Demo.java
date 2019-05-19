package com.milos.client;

import java.util.Random;
import java.util.UUID;

/**
 * This class is used only for demonstration of the system,
 * and should be treated or imagined as some integration layer
 * with some other system that provides changes and messages that will
 * be synchronized
 */
public class Demo implements Runnable {

    private ClientInMemoryStore store;
    private Random rnd;
    private int msgCountPerType;

    public Demo(final ClientInMemoryStore store, final int msgCountPerType) {
        this.store = store;
        this.msgCountPerType = msgCountPerType;
        this.rnd = new Random();
    }

    private String generateRandomMsg() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void run() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < msgCountPerType; i++) {
                    try {
                        store.putMessageToSend(generateRandomMsg());
                    } catch (InterruptedException e) {
                        //TODO investigate when this exception can be raised and take proper actions
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < msgCountPerType; i++) {
                    try {
                        store.putMessageToSend(generateRandomMsg());
                    } catch (InterruptedException e) {
                        //TODO investigate when this exception can be raised and take proper actions
                    }
                }
            }
        }).start();
    }
}
