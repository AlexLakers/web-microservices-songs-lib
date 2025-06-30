package com.alex.web.microservices.songs.lib.author.api.rest.controller;

import com.alex.web.microservices.songs.lib.author.client.song.model.Song;
import com.alex.web.microservices.songs.lib.author.dto.WriteDto;
import com.alex.web.microservices.songs.lib.author.model.Author;
import com.alex.web.microservices.songs.lib.author.search.SearchDto;
import com.alex.web.microservices.songs.lib.author.service.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This is REST-Controller.It provides the main endpoint-methods with {@link Author author-entity.}
 * @see AuthorService authorService
 */
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
@RestController
@Slf4j
public class AuthorRestController {

    private final AuthorService authorService;

    /**
     * Returns status and author formatted  .JSON
     *
     * @param id id of author
     * @return author-entity
     */

    @GetMapping("/{id}")
    ResponseEntity<Author> getOne(@PathVariable Long id) {
        log.info("--start 'find author by id' rest endpoint--");
        return ResponseEntity.status(HttpStatus.OK).body(authorService.findById(id));
    }

    /**
     * Returns status and list of songs formatted .JSON by id of author.
     * Occurs the invocation of the endpoint another service(song-service).
     * @param authorId id of authors.
     * @return list of songs.
     */

    @GetMapping("/{authorId}/songs")
    public ResponseEntity<List<Song>> getAllSongsByAuthorId(@PathVariable Long authorId) {
        log.info("--start 'find songs by id of author' rest endpoint--");
        return ResponseEntity.status(HttpStatus.OK).body(authorService.getAllSongsByAuthorId(authorId));
    }

    /**
     * Saves new author in the database using input-dto formatted .JSON.
     * @param writeDto input-dto.
     * @return a saved author.
     */

    @PostMapping
    ResponseEntity<Author> save(@RequestBody WriteDto writeDto) {
        log.info("--start 'save a new author by input write dto' rest endpoint--");
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.save(writeDto));
    }

    /**
     * Updates available author by id and input-dto formatted .JSON.
     * @param writeDto input-dto.
     * @param id id of updated author.
     * @return an updated author.
     */

    @PutMapping("/{id}")
    public ResponseEntity<Author> update(@RequestBody WriteDto writeDto, @PathVariable Long id) {
        log.info("--start 'update an available author by input write dto' rest endpoint--");
        return ResponseEntity.status(HttpStatus.OK).body(authorService.update(writeDto, id));
    }

    /**
     * Returns list of author using input-search dto as a set input parameters.
     * @param searchDto set input parameters for search.
     * @return list of authors.
     */

    @GetMapping
    public ResponseEntity<List<Author>> getAll(SearchDto searchDto) {
        log.info("--start 'find authors by input-dto for search' rest endpoint--");
        return ResponseEntity.status(HttpStatus.OK).body(authorService.findAllBy(searchDto).getContent());
    }

    /**
     * Delete available author by author id.
     * @param id id of author.
     * @return status-code.
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        log.info("--start 'delete an available author by id' rest endpoint--");
        authorService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
