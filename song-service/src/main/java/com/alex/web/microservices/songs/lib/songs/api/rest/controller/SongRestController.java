package com.alex.web.microservices.songs.lib.songs.api.rest.controller;

import com.alex.web.microservices.songs.lib.songs.dto.WriteDto;
import com.alex.web.microservices.songs.lib.songs.model.Song;
import com.alex.web.microservices.songs.lib.songs.search.PageDto;
import com.alex.web.microservices.songs.lib.songs.search.SearchDto;
import com.alex.web.microservices.songs.lib.songs.service.SongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This is REST-Controller.It provides the main endpoint-methods with {@link Song song-entity.}
 *
 * @see SongService songService
 */
@RestController
@RequestMapping("/api/v1/songs")
@RequiredArgsConstructor
@Slf4j
public class SongRestController {
    public final SongService songService;

    /**
     * Returns status and song formatted  .JSON
     *
     * @param id id of author.
     * @return song-entity.
     */

    @GetMapping("/{id}")
    public ResponseEntity<Song> findById(@PathVariable long id) {
        log.info("--start 'find song by id' rest endpoint--");
        return ResponseEntity.status(HttpStatus.OK).body(songService.findById(id));
    }

    /**
     * Saves a new song by input dto.
     *
     * @param dto input dto.
     * @return saved song.
     */

    @PostMapping
    public ResponseEntity<Song> save(@RequestBody WriteDto dto) {
        log.info("--start 'save a new song by input write dto' rest endpoint--");
        Song savedSong = songService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSong);
    }

    /**
     * Updates song by input dto and id.
     *
     * @param id       id of song.
     * @param writeDto input dto.
     * @return updated song.
     */

    @PutMapping("/{id}")
    public ResponseEntity<Song> update(@PathVariable Long id, @RequestBody WriteDto writeDto) {
        log.info("--start 'update an available song by input write dto' rest endpoint--");
        return ResponseEntity.status(HttpStatus.OK).body(songService.update(id, writeDto));
    }

    /**
     * Deletes song by id
     *
     * @param id id of song.
     * @return status.
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        log.info("--start 'delete an available song by id' rest endpoint--");
        songService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Returns status and list of songs formatted .JSON by id of author.
     *
     * @param authorId if of author.
     * @return list of songs.
     */
    @GetMapping
    public ResponseEntity<List<Song>> findAllByAuthorId(@RequestParam Long authorId) {
        log.info("--start 'find songs by id of author' rest endpoint--");
        List<Song> songs = songService.findAllByAuthorId((authorId));
        return ResponseEntity.status(HttpStatus.OK).body(songs);
    }

    /**
     * Returns status and list of songs formatted .JSON by dto of search.
     *
     * @param searchDto - input dto for search
     * @return page of songs.
     */
    @PostMapping("/search")
    public ResponseEntity<PageDto> findAllBySearchDto(@RequestBody SearchDto searchDto) {
        log.info("--start 'find songs by input-dto for search' rest endpoint--");
        return ResponseEntity.status(HttpStatus.OK).body(songService.findAll(searchDto));
    }
}
