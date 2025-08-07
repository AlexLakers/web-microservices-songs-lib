package com.alex.web.microservices.songs.lib.songs.exception;

/**
 * This exception describes error during create operation.
 */
public class SongCreationException extends RuntimeException {
    public SongCreationException(String message) {
        super(message);
    }
    public SongCreationException(String message,Throwable cause) {
        super(message,cause);
    }
}
