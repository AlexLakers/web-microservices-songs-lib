package com.alex.web.microservices.songs.lib.songs.exception;

public class SaveOperationException extends RuntimeException{
    public SaveOperationException(String message) {
        super(message);
    }
    public SaveOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
