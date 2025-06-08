package com.alex.web.microservices.songs.lib.songs.client.model;


import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Author {
    private Long id;
    private Name name;
    private LocalDate birthdate;
}
