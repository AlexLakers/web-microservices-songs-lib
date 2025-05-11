package com.alex.web.microservices.songs.lib.author.exception;

public class AuthorNotFoundException extends RuntimeException{
    public AuthorNotFoundException(String message) {
        super(message);
    }
    public AuthorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
