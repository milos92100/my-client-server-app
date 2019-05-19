package com.milos.domain;

import java.util.UUID;

public class MessageStats {

    private UUID messageId;
    private long processDurationInMillis;
    private long capturedAvgDurationInMillis;

    public MessageStats(UUID messageId, long processDurationInMillis, long capturedAvgDurationInMillis) {
        this.messageId = messageId;
        this.processDurationInMillis = processDurationInMillis;
        this.capturedAvgDurationInMillis = capturedAvgDurationInMillis;
    }

    public UUID getMessageId() {
        return messageId;
    }

    public long getProcessDurationInMillis() {
        return processDurationInMillis;
    }

    public long getCapturedAvgDurationInMillis() {
        return capturedAvgDurationInMillis;
    }
}
