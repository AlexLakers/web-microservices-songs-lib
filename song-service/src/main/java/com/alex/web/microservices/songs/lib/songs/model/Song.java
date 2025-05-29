package com.alex.web.microservices.songs.lib.songs.model;

import lombok.*;
import lombok.experimental.FieldNameConstants;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldNameConstants
public class Song {
    private Long id;
    private String name;
    private Long authorId;
    private String album;
}
