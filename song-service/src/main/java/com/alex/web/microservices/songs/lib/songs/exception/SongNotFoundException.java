package com.alex.web.microservices.songs.lib.songs.exception;
/**
 * This exception describes error which can occurs during attempt of search.
 */
public class SongNotFoundException extends RuntimeException{

    public SongNotFoundException(String message) {
        super(message);
    }
    public SongNotFoundException(String message,Throwable cause) {
        super(message,cause);
    }
}
