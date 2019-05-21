package com.milos.domain.worker;


import com.milos.domain.MessageStats;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

/**
 * StatsWorker polls message stats form source queue,
 * formats the data to a readable string and sends it to
 * console output stream.
 */
public class StatsWorker implements Runnable {

    private BlockingQueue<MessageStats> sourceQueue;
    private PrintWriter outputStream;

    public StatsWorker(final BlockingQueue<MessageStats> sourceQueue, final OutputStream outputStream) {
        this.sourceQueue = sourceQueue;
        this.outputStream = new PrintWriter(outputStream, true);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            MessageStats stats = sourceQueue.poll();

            if (stats == null) {
                continue;
            }

            String content = new StringBuilder()
                    .append("message: ").append(stats.getMessageId()).append("; ")
                    .append("duration: ").append(stats.getProcessDurationInMillis()).append(" ms ; ")
                    .append("avg: ").append(stats.getCapturedAvgDurationInMillis()).append(" ms").toString();

            outputStream.println(content);
        }
    }
}
