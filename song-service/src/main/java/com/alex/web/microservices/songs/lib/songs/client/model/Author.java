package com.alex.web.microservices.songs.lib.songs.client.model;


import com.alex.web.microservices.songs.lib.songs.client.AuthorClient;
import lombok.*;

import java.time.LocalDate;

/**
 * This class is used as a model for interaction with another service  with {@link AuthorClient client}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class Author {
    private Long id;
    private Name name;
    private LocalDate birthdate;
}
