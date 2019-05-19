package com.milos.domain.message;

import com.google.gson.Gson;

import java.util.Date;
import java.util.UUID;

public class Message implements JsonSerializableMessage {
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

    @Override
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    @Override
    public Message fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, getClass());
    }
}
