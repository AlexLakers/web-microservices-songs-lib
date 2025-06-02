package com.alex.web.microservices.songs.lib.songs.api.rest.controller;

import com.alex.web.microservices.songs.lib.songs.dto.WriteDto;
import com.alex.web.microservices.songs.lib.songs.model.Song;
import com.alex.web.microservices.songs.lib.songs.search.PageDto;
import com.alex.web.microservices.songs.lib.songs.search.SearchDto;
import com.alex.web.microservices.songs.lib.songs.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/songs")
@RequiredArgsConstructor
public class SongRestController {
    public final SongService songService;

    @GetMapping("/{id}")
    public ResponseEntity<Song> findById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(songService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Song> save(@RequestBody WriteDto dto) {
        Song savedSong = songService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSong);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Song> update(@PathVariable Long id, @RequestBody WriteDto writeDto) {
        return ResponseEntity.status(HttpStatus.OK).body(songService.update(id, writeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        songService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<Song>> findAllByAuthorId(@RequestParam Long authorId) {
        List<Song> songs = songService.findAllByAuthorId((authorId));
        return ResponseEntity.status(HttpStatus.OK).body(songs);
    }

    @PostMapping("/search")
    public ResponseEntity<PageDto> findAllBySearchDto(@RequestBody SearchDto searchDto) {
        return ResponseEntity.status(HttpStatus.OK).body(songService.findAll(searchDto));
    }
}
