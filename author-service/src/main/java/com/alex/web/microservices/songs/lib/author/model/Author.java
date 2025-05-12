package com.alex.web.microservices.songs.lib.author.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(schema = "public", name = "author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "firstname", column = @Column(name = "first_name"))
    @AttributeOverride(name = "lastname", column = @Column(name = "last_name"))
    private Name name;

    @Column(name = "birth_date")
    private LocalDate birthdate;
}
