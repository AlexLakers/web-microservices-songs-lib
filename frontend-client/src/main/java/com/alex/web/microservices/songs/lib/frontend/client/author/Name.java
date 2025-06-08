package com.alex.web.microservices.songs.lib.frontend.client.author;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is a part of model for visualisation in dashboard.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Name {

    private String firstname;
    private String lastname;
}
