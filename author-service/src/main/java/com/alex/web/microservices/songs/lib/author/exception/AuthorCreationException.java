package com.alex.web.microservices.songs.lib.author.exception;

public class AuthorCreationException extends RuntimeException {
    public AuthorCreationException(String message) {
        super(message);
    }
    public AuthorCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
