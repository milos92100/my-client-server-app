package com.milos.domain;

import com.google.gson.Gson;


import java.util.Date;
import java.util.UUID;

public class Message implements JsonSerializable {

    public static class Type {
        public static final String ACCOUNT_CHANGE = "ACCOUNT_CHANGE";
        public static final String USER_CHANGE = "USER_CHANGE";
    }

    public static Message fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Message.class);
    }

    private UUID id;
    private Date createdAt;
    private String type;
    private String payload;

    public Message(final UUID id, final Date createdAt, final String type, final String payload) {
        this.id = id;
        this.createdAt = createdAt;
        this.type = type;
        this.payload = payload;
    }

    public UUID getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getType() {
        return type;
    }

    public String getPayload() {
        return payload;
    }

    public boolean typeOf(String type) {
        return this.type.equals(type);
    }


    @Override
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", type='" + type + '\'' +
                ", payload='" + payload + '\'' +
                '}';
    }
}
