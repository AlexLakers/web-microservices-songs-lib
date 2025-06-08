package com.alex.web.microservices.songs.lib.frontend.client.author;


import java.time.LocalDate;

/**
 * This is input-dto for update author using dashboard.
 * @param firstname firstname of author.
 * @param lastname lastname of author.
 * @param birthdate birthdate of author.
 */
public record WriteDto(
        String firstname,
        String lastname,
        LocalDate birthdate) {
}
