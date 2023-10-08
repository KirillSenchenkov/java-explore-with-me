package ru.practicum.ewm.stats.server.exception;

public class WrongTimestampException extends RuntimeException {

    public WrongTimestampException(String message) {
        super(message);
    }
}
