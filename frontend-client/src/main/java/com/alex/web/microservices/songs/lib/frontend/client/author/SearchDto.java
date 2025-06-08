package com.alex.web.microservices.songs.lib.frontend.client.author;


import java.time.LocalDate;

/**
 * This class is dto for search.It is used for search data in author-service.
 * @param id id of author.
 * @param firstname firstname of author.
 * @param lastname lastname of author.
 * @param birthdate birthdate of author.
 * @param page page number.
 * @param size size of page.
 */
public record SearchDto(Long id,
                        String firstname,
                        String lastname,
                        LocalDate birthdate,
                        Integer page,
                        Integer size) {
}

