package com.alex.web.microservices.songs.lib.songs.exception;

public class SongNotFoundException extends RuntimeException{

    public SongNotFoundException(String message) {
        super(message);
    }
    public SongNotFoundException(String message,Throwable cause) {
        super(message,cause);
    }
}
