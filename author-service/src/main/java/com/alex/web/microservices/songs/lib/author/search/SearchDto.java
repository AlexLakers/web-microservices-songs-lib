package com.alex.web.microservices.songs.lib.author.search;

import java.time.LocalDate;

public record SearchDto(Long id, String firstname, String lastname, LocalDate birthdate,Integer page,Integer size) {
}
