package com.milos.domain;

import com.google.gson.Gson;

import java.util.UUID;

/**
 * Answer represents the response form the server for a received message.
 */
public class Answer implements JsonSerializable {

    public static class Type {
        public static final String ACKNOWLEDGED = "ACKNOWLEDGED";
        public static final String NOT_PROCESSED_DUE_FAILURE = "NOT_PROCESSED_DUE_FAILURE";
        public static final String REJECTED = "REJECTED";
    }

    public static Answer fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Answer.class);
    }

    private UUID messageId;
    private String type;

    public Answer(UUID messageId, String type) {
        this.messageId = messageId;
        this.type = type;
    }

    public UUID getMessageId() {
        return messageId;
    }

    public String getType() {
        return type;
    }

    public boolean messageWas(String type) {
        return this.type.equals(type);
    }

    @Override
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "messageId=" + messageId +
                ", type='" + type + '\'' +
                '}';
    }
}
