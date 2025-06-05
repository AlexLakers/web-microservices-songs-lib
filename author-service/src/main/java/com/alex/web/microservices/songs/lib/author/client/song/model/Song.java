package com.alex.web.microservices.songs.lib.author.client.song.model;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;


@Getter
@NoArgsConstructor
@Data
@Setter
@ToString
@RedisHash("song")
public class Song implements Serializable {
    @Id
    private Long id;
    private String name;
    private Long authorId;
    private String album;
}
