package com.alex.web.microservices.songs.lib.author.dto;

import com.alex.web.microservices.songs.lib.author.validator.CreateGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record WriteDto(
        @NotBlank(message = "The firstname can't be empty or null",groups = CreateGroup.class)
        String firstname,
        @NotBlank(message = "The lastname can't be empty or null",groups = CreateGroup.class)
        String lastname,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @NotNull(message = "The birthdate can't be null",groups = CreateGroup.class)
        LocalDate birthdate){
}
