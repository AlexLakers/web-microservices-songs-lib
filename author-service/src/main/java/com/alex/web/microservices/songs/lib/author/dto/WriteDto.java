package com.alex.web.microservices.songs.lib.author.dto;

import com.alex.web.microservices.songs.lib.author.validator.groups.CreateGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

/**
 * This class as an input-dto for save and update operations.
 * @param firstname firstname of author.
 * @param lastname lastname of author.
 * @param birthdate birthdate of author.
 */
public record WriteDto(
        @NotBlank(message = "The firstname can't be empty or null", groups = CreateGroup.class)
        String firstname,

        @NotBlank(message = "The lastname can't be empty or null", groups = CreateGroup.class)
        String lastname,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @NotNull(message = "The birthdate can't be null", groups = CreateGroup.class)
        @Past(message = "The birthdate can't be present or future", groups = CreateGroup.class)
        LocalDate birthdate) {
}
