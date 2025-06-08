package com.alex.web.microservices.songs.lib.author.exception;

/**
 * This exception describes error during create operation.
 */
public class AuthorCreationException extends RuntimeException {
    public AuthorCreationException(String message) {
        super(message);
    }
    public AuthorCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
