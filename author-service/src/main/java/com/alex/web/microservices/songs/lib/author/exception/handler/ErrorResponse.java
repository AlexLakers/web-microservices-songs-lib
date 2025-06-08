package com.alex.web.microservices.songs.lib.author.exception.handler;

/**
 * This class describes common error in app.
 * @param code code of error.
 * @param message message of error.
 */
public record ErrorResponse(int code,String message){
}
