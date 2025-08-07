package com.alex.web.microservices.songs.lib.songs.model;

import lombok.*;
import lombok.experimental.FieldNameConstants;

/**
 * This class a model that is contains fields and structs which based on the database 'song'.
 */

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
