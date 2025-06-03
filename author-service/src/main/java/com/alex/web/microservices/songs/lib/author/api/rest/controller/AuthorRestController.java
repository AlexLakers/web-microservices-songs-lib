package com.alex.web.microservices.songs.lib.author.api.rest.controller;

import com.alex.web.microservices.songs.lib.author.dto.WriteDto;
import com.alex.web.microservices.songs.lib.author.model.Author;
import com.alex.web.microservices.songs.lib.author.search.SearchDto;
import com.alex.web.microservices.songs.lib.author.service.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
@RestController
@Slf4j
public class AuthorRestController {

    private final AuthorService authorService;

    @GetMapping("/{id}")
    ResponseEntity<Author> getOne(@PathVariable Long id) {
        log.info("--start 'find author by id' rest endpoint--");
        return ResponseEntity.status(HttpStatus.OK).body(authorService.findById(id));
    }

    @PostMapping
    ResponseEntity<Author> save(@RequestBody WriteDto writeDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.save(writeDto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Author> update(@RequestBody WriteDto writeDto, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(authorService.update(writeDto, id));
    }
    @GetMapping
    public ResponseEntity<List<Author>> getAll(SearchDto searchDto){
        return ResponseEntity.status(HttpStatus.OK).body(authorService.findAllBy(searchDto).getContent());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        authorService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
