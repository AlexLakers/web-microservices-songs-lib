package com.alex.web.microservices.songs.lib.songs.search;

import com.alex.web.microservices.songs.lib.songs.annoataion.OrderColumnValid;
import com.alex.web.microservices.songs.lib.songs.annoataion.OrderDirectionValid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record SearchDto(@PositiveOrZero(message = "The page should be positive or zero")
                        Integer page,
                        @Positive(message = "The size should be positive")
                        Integer size,
                        @OrderColumnValid(message = "The sort column should be empty or available in the database.")
                        String sortColumn,
                        @OrderDirectionValid(message = "The direction of sort should be 'ASC or 'DESC'")
                        String sortDirection) {
}
