package com.milos.client;

import com.milos.domain.Message;

import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

/**
 * This class is used only for demonstration of the system,
 * and should be treated or imagined as some integration layer
 * with some other system that provides changes and messages that will
 * be synchronized
 */
public class Demo implements Runnable {

    private BlockingQueue<Message> accountMessagesQueue;
    private BlockingQueue<Message> userMessagesQueue;
    private Random rnd;
    private int msgCountPerType;

    public Demo(final BlockingQueue<Message> accountMessagesQueue, final BlockingQueue<Message> userMessagesQueue, final int msgCountPerType) {
        this.accountMessagesQueue = accountMessagesQueue;
        this.userMessagesQueue = userMessagesQueue;
        this.msgCountPerType = msgCountPerType;
        this.rnd = new Random();
    }

    private Message generateRandomMsg(String type) {
        return new Message(UUID.randomUUID(), new Date(), type, "Some " + type + " changes.");
    }

    @Override
    public void run() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < msgCountPerType; i++) {
                    try {
                        accountMessagesQueue.put(generateRandomMsg(Message.Type.ACCOUNT_CHANGE));
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
                        userMessagesQueue.put(generateRandomMsg(Message.Type.USER_CHANGE));
                    } catch (InterruptedException e) {
                        //TODO investigate when this exception can be raised and take proper actions
                    }
                }
            }
        }).start();
    }
}
