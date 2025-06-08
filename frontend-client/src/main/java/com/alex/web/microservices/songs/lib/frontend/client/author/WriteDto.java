package com.alex.web.microservices.songs.lib.frontend.client.author;


import java.time.LocalDate;

public record WriteDto(
        String firstname,
        String lastname,
        LocalDate birthdate) {
}
