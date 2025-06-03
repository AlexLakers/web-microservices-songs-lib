package com.alex.web.microservices.songs.lib.author.client.song.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
