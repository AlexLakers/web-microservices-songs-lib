package com.alex.web.microservices.songs.lib.author.client.song;

import com.alex.web.microservices.songs.lib.author.client.song.model.Song;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("song-service")
public interface SongClient {

    @GetMapping("/api/v1/songs")
     ResponseEntity<List<Song>> findAllSongByAuthorId(@RequestParam Long authorId);
}
