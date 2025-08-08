package com.alex.web.microservices.songs.lib.songs.dto;

import com.alex.web.microservices.songs.lib.songs.annoataion.AuthorIdValid;
import com.alex.web.microservices.songs.lib.songs.validator.CreateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * This clas is data transfer object for INSERT or UPDATE operations.
 * @param authorId
 * @param name
 * @param album
 */
public record WriteDto(
                        @Positive(message = "The id if author should be positive")
                       Long authorId,
                       @NotBlank(groups = CreateGroup.class, message = "The name should be filling")
                       String name,
                       String album) {
}
