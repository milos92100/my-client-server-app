package com.milos.server;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SharedState {
    ReadWriteLock lock = new ReentrantReadWriteLock();
    Lock writeLock = lock.writeLock();

    private long messageCount;
    private long durationSumInMillis;

    public SharedState() {
        messageCount = 0;
        durationSumInMillis = 0;
    }

    public long updateAndGetCurrentAvgDuration(long newDuration) {
        try {
            writeLock.lock();

            durationSumInMillis += newDuration;
            messageCount++;

            return durationSumInMillis / messageCount;

        } finally {
            writeLock.unlock();
        }
    }
}
