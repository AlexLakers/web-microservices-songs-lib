package com.alex.web.microservices.songs.lib.author.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *It as a path of the model{@link Author author.}.
 *
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Name {
    private String firstname;
    private String lastname;
}
