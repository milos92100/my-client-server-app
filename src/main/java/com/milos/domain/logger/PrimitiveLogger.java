package com.milos.domain.logger;

public interface PrimitiveLogger {

    public void debug(String message);

    public void info(String message);

    public void warn(String message);

    public void error(String message);
}
