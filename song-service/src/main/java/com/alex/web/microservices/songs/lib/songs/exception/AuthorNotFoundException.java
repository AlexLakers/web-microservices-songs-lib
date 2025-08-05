package com.alex.web.microservices.songs.lib.songs.exception;

/**
 * This exception describes error which can occurs during attempt of search.
 */
public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(String message) {
        super(message);
    }

    public AuthorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
