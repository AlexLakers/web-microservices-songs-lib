package com.alex.web.microservices.songs.lib.author.search;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

/**
 * This class as an input-dto for search page of author.
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
                        @Past(message = "The birthdate should be more early then present")
                        LocalDate birthdate,
                        @PositiveOrZero(message = "The page number should be positive or zero")
                        Integer page,
                        @Positive(message = "The page size should be positive")
                        Integer size) {
}
