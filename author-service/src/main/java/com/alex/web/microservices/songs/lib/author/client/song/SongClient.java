package com.alex.web.microservices.songs.lib.author.client.song;

import com.alex.web.microservices.songs.lib.author.client.song.model.Song;
import jakarta.ws.rs.Path;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * This is REST-client to call the remote service 'Song-service'.
 * It used open-feign by spring-cloud project.
 */
@FeignClient("song-service")
public interface SongClient {
    /**
     * Returns list of songs by id of author.For this operation used the same endpoint from the song-service.
     * @param authorId id of author.
     * @return list of songs.
     */
    @GetMapping("/api/v1/songs")
    ResponseEntity<List<Song>> findAllSongByAuthorId(@RequestParam Long authorId);

    /**
     * Return one song by id .For this operation used the same endpoint from the song-service.
     * @param id id of song.
     * @return song.
     */
    @GetMapping("/api/v1/songs/{id}")
    ResponseEntity<Song> findBySongId(@PathVariable Long id);
}
