package com.alex.web.microservices.songs.lib.frontend.client.author;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * This class is model of 'author-service' for visualisation in dashboard.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    private Long id;
    private Name name;
    private LocalDate birthdate;
}
