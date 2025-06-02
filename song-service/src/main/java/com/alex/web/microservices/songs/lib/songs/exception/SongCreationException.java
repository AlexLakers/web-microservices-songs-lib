package com.alex.web.microservices.songs.lib.songs.exception;

public class SongCreationException extends RuntimeException {
    public SongCreationException(String message) {
        super(message);
    }
    public SongCreationException(String message,Throwable cause) {
        super(message,cause);
    }
}
