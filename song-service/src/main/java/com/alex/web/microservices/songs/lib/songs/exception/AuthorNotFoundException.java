package com.alex.web.microservices.songs.lib.songs.exception;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(String message) {
        super(message);
    }

    public AuthorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
