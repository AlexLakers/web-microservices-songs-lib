package com.alex.web.microservices.songs.lib.author.service;

import com.alex.web.microservices.songs.lib.author.client.song.SongClient;
import com.alex.web.microservices.songs.lib.author.config.AuthorConfig;
import com.alex.web.microservices.songs.lib.author.client.song.model.Song;
import com.alex.web.microservices.songs.lib.author.dto.WriteDto;
import com.alex.web.microservices.songs.lib.author.exception.AuthorNotFoundException;
import com.alex.web.microservices.songs.lib.author.mapper.AuthorMapper;
import com.alex.web.microservices.songs.lib.author.model.QAuthor;
import com.alex.web.microservices.songs.lib.author.repository.RedisRepository;
import com.alex.web.microservices.songs.lib.author.search.PredicateBuilder;
import com.alex.web.microservices.songs.lib.author.search.SearchDto;
import com.alex.web.microservices.songs.lib.author.exception.AuthorCreationException;
import com.alex.web.microservices.songs.lib.author.model.Author;
import com.alex.web.microservices.songs.lib.author.repository.AuthorRepository;
import com.alex.web.microservices.songs.lib.author.validator.groups.CreateGroup;
import com.querydsl.core.types.Predicate;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated(CreateGroup.class)
@Transactional(readOnly = true)
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorConfig authorConfig;
    private final AuthorMapper authorMapper;
    private final SongClient songClient;
    private final RedisRepository redisRepository;

    public Author findById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("The author with id={%d} is not found".formatted(id)));
    }

    public boolean existByFirstNameAndLastname(String firstName, String lastName) {
        return authorRepository.existByFirstNameAndLastname(firstName, lastName);
    }

    @CircuitBreaker(name = "circuit-song-service")
    @Retry(name = "retry-song-service", fallbackMethod = "fallbackGetAllSongsByAuthorId")
    @Transactional
    public List<Song> getAllSongsByAuthorId(Long authorId) {
        List<Song> songs = redisRepository.getSongListBySetIds(authorId);
        if (songs.isEmpty()) {
            songs = songClient.findAllSongByAuthorId(authorId).getBody();
            songs.forEach(redisRepository::saveSongSetIdAndValue);
            log.info("MISS, songs are not found in Cache-Redis by authorId:{}", authorId);
        } else {
            log.info("HIT, the songs: {} was found in Cache-Redis by authorId:{}", songs, authorId);
        }
        return songs;
    }

    public List<Song> fallbackGetAllSongsByAuthorId(Long authorId, Throwable e) {
        log.error("The service 'song-service' is not available:{}", e.getMessage());
        return Collections.emptyList();
    }

    @Transactional
    public Author save(@Valid WriteDto dto) {

        return Optional.ofNullable(authorMapper.toAuthor(dto))
                .map(authorRepository::save)
                .orElseThrow(() -> new AuthorCreationException("An error has been detected during creation new author"));
    }

    @Transactional
    @Validated
    public Author update(@Valid WriteDto dto, Long id) {
        return authorRepository.findById(id)
                .map(author -> {
                    authorMapper.update(dto, author);
                    return authorRepository.saveAndFlush(author);
                }).orElseThrow(() -> new AuthorNotFoundException("The author with id={%d} is not found".formatted(id)));
    }

    @Validated
    public Page<Author> findAllBy(@Valid SearchDto searchDto) {
        Integer page = searchDto.page() == null ? authorConfig.getDefaultPage() : searchDto.page();
        Integer size = searchDto.size() == null ? authorConfig.getDefaultSize() : searchDto.size();
        Pageable pageable = PageRequest.of(page, size);

        Predicate predicate = PredicateBuilder.builder()
                .add(searchDto.id(), QAuthor.author.id::eq)
                .add(searchDto.firstname(), QAuthor.author.name.firstname::containsIgnoreCase)
                .add(searchDto.lastname(), QAuthor.author.name.lastname::containsIgnoreCase)
                .add(searchDto.birthdate(), QAuthor.author.birthdate::after)
                .buildAnd();
        return authorRepository.findAll(predicate, pageable);
    }

    @Transactional
    public void delete(Long id) {
        authorRepository.findById(id)
                .ifPresentOrElse((author -> {
                    authorRepository.delete(author);
                    authorRepository.flush();
                }), () -> {
                    throw new AuthorNotFoundException("The author with id={%d} is not found".formatted(id));
                });
    }

}
