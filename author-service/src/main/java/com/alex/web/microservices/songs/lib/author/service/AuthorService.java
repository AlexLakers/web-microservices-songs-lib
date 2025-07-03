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

/**
 * This class as a service-layer on this app.It contains all the methods for
 * realization of buisness logic.
 * @see Author author-entity.
 * @see AuthorRepository repository.
 * @see AuthorMapper mapper.
 *
 */
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

    /**
     * Returns founded author-entity by id.
     * @param id id of author.
     * @return author.
     */
    public Author findById(Long id) {
        log.debug("The method 'findById' with arg:{}", id);
        Author foundedAuthor= authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("The author with id={%d} is not found".formatted(id)));
        log.info("Founded author by id:{}", foundedAuthor);
        return foundedAuthor;
    }

    /**
     * Returns result of boolean operation available entity by fullname.
     * @param firstName firstname for search.
     * @param lastName lastname for search.
     * @return true if entity exists by conditions or else false.
     */
    public boolean existByFirstNameAndLastname(String firstName, String lastName) {
        return authorRepository.existByFirstNameAndLastname(firstName, lastName);
    }

    /**
     * Returns list of songs by id of author.
     * This method is protected using resilience-lib.
     * Firstly, occurs request to redis-cache to check available data in there.
     * After it if teh data is not exist then occurs request to Song-service using httpClient.
     * And then founded data is saved to redis-cache.
     * @param authorId if of author.
     * @return list of song.
     */

    @CircuitBreaker(name = "circuit-song-service")
    @Retry(name = "retry-song-service", fallbackMethod = "fallbackGetAllSongsByAuthorId")
    @Transactional
    public List<Song> getAllSongsByAuthorId(Long authorId) {
        log.debug("The method 'getAllSongsByAuthorId' with arg:{}", authorId);
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

    /**
     * This method is fallback-method realization for 'getAllSongsByAuthorId'.
     * @param authorId id of author.
     * @param e exception.
     * @return list of song.
     */
    public List<Song> fallbackGetAllSongsByAuthorId(Long authorId, Throwable e) {
        log.error("The service 'song-service' is not available:{}", e.getMessage());
        return Collections.emptyList();
    }

    /**
     * Saves a new author in the database using input-dto.
     * @param dto input-dto
     * @return author.
     */
    @Transactional
    public Author save(@Valid WriteDto dto) {
        log.debug("The method 'save' with arg:{}", dto);
        Author savedAuthor= Optional.ofNullable(authorMapper.toAuthor(dto))
                .map(authorRepository::save)
                .orElseThrow(() -> new AuthorCreationException("An error has been detected during creation new author"));
        log.info("A new author with id:{} has been created",savedAuthor.getId());
        return savedAuthor;
    }

    /**
     * Updates author in the database using input-dto.
     * @param dto input-dto
     * @return author.
     */
    @Transactional
    @Validated
    public Author update(@Valid WriteDto dto, Long id) {
        log.debug("The method 'update' with arg:{}", dto);
        Author updatedAuthor= authorRepository.findById(id)
                .map(author -> {
                    authorMapper.update(dto, author);
                    return authorRepository.saveAndFlush(author);
                }).orElseThrow(() -> new AuthorNotFoundException("The author with id={%d} is not found".formatted(id)));
        log.info("The author with id:{} has been updated",id);
        return updatedAuthor;
    }

    /**
     * Returns page with authors by input search dto.
     * Firstly, occurs the predicate building process.
     * Then, occurs search using predicate and sort-info.
     * @param searchDto input-dto(conditions).
     * @return page of authors.
     */
    @Validated
    public Page<Author> findAllBy(@Valid SearchDto searchDto) {
        log.debug("The method 'findAllBy' with arg:{}", searchDto);
        Integer page = searchDto.page() == null ? authorConfig.getDefaultPage() : searchDto.page();
        Integer size = searchDto.size() == null ? authorConfig.getDefaultSize() : searchDto.size();
        Pageable pageable = PageRequest.of(page, size);

        Predicate predicate = PredicateBuilder.builder()
                .add(searchDto.id(), QAuthor.author.id::eq)
                .add(searchDto.firstname(), QAuthor.author.name.firstname::containsIgnoreCase)
                .add(searchDto.lastname(), QAuthor.author.name.lastname::containsIgnoreCase)
                .add(searchDto.birthdate(), QAuthor.author.birthdate::after)
                .buildAnd();
        Page<Author> foundPage=authorRepository.findAll(predicate, pageable);
        log.info("Founded page of authors by search-dto:{}", foundPage);
        return foundPage;
    }

    /**
     * Delete author by id.
     * @param id id of author.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("The method 'delete' with args:{}",id);
        authorRepository.findById(id)
                .ifPresentOrElse((author -> {
                    authorRepository.delete(author);
                    authorRepository.flush();
                }), () -> {
                    throw new AuthorNotFoundException("The author with id={%d} is not found".formatted(id));
                });
    }

}
