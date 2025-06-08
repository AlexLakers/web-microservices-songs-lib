package com.alex.web.microservices.songs.lib.author.search;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

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
