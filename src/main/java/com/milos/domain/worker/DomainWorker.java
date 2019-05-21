package com.milos.domain.worker;

import com.milos.domain.Message;
import com.milos.domain.MessageStats;
import com.milos.domain.logger.PrimitiveLogger;
import com.milos.server.SharedState;

import java.util.Date;
import java.util.concurrent.BlockingQueue;

/**
 * Domain worker is used to calculate messages stats
 * and publish the result to the result queue.
 */
public class DomainWorker implements Runnable {

    private SharedState sharedState;
    private BlockingQueue<Message> sourceQueue;
    private BlockingQueue<MessageStats> resultQueue;
    private PrimitiveLogger logger;

    public DomainWorker(final BlockingQueue<Message> sourceQueue,
                        final BlockingQueue<MessageStats> resultQueue,
                        final SharedState sharedState,
                        final PrimitiveLogger logger) {
        this.sourceQueue = sourceQueue;
        this.resultQueue = resultQueue;
        this.sharedState = sharedState;
        this.logger = logger;
    }

    protected Date getCurrentDate() {
        return new Date();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Message message = sourceQueue.poll();

            if (message == null) {
                continue;
            }

            long duration = getCurrentDate().getTime() - message.getCreatedAt().getTime();
            long avg = sharedState.updateAndGetCurrentAvgDuration(duration);

            try {
                resultQueue.put(new MessageStats(message.getId(), duration, avg));
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
