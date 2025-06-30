package com.alex.web.microservices.songs.lib.songs.client.model;

import com.alex.web.microservices.songs.lib.songs.client.AuthorClient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is used as a model for interaction with another service  with {@link AuthorClient client}.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Name {
    private String firstname;
    private String lastname;
}
