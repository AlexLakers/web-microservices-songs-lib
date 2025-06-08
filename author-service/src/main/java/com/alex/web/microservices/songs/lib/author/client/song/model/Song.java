package com.alex.web.microservices.songs.lib.author.client.song.model;

import com.alex.web.microservices.songs.lib.author.client.song.SongClient;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

/**
 * This is a model which is used for deserialization/serialization process during calling remote service 'Song-service'.
 * @see SongClient songClient.
 */
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
