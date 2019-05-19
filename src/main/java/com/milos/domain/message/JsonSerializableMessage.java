package com.milos.domain.message;

public interface JsonSerializableMessage<T> {

    public String toJson();

    public T fromJson(String json);
}
