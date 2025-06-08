package com.alex.web.microservices.songs.lib.songs.service;

import com.alex.web.microservices.songs.lib.songs.client.AuthorClient;
import com.alex.web.microservices.songs.lib.songs.client.model.Author;
import com.alex.web.microservices.songs.lib.songs.config.PaginationConfig;
import com.alex.web.microservices.songs.lib.songs.dto.WriteDto;
import com.alex.web.microservices.songs.lib.songs.events.producer.model.Action;
import com.alex.web.microservices.songs.lib.songs.events.producer.publisher.SongModificationAction;
import com.alex.web.microservices.songs.lib.songs.exception.AuthorNotFoundException;
import com.alex.web.microservices.songs.lib.songs.exception.SaveOperationException;
import com.alex.web.microservices.songs.lib.songs.exception.SongCreationException;
import com.alex.web.microservices.songs.lib.songs.exception.SongNotFoundException;
import com.alex.web.microservices.songs.lib.songs.mapper.SongMapper;
import com.alex.web.microservices.songs.lib.songs.model.Song;
import com.alex.web.microservices.songs.lib.songs.search.PageDto;
import com.alex.web.microservices.songs.lib.songs.search.SearchDto;
import com.alex.web.microservices.songs.lib.songs.repository.SongRepository;
import com.alex.web.microservices.songs.lib.songs.validator.CreateGroup;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.opentelemetry.api.trace.Span;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

import static java.util.concurrent.CompletableFuture.supplyAsync;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Validated
public class SongService {
    private final SongRepository songRepository;
    private final PaginationConfig paginationConfig;
    private final SongMapper songMapper;
    private final AuthorClient authorClient;
    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
    private final SongModificationAction songModificationAction;

    public PageDto findAll(SearchDto searchDto) {
        int page = searchDto.page() == null ? paginationConfig.getPage() : searchDto.page();
        int size = searchDto.size() == null || searchDto.size() == 0 ? paginationConfig.getSize() : searchDto.size();
        String orderColumn = searchDto.sortColumn() == null ? paginationConfig.getColumn() : searchDto.sortColumn();
        String orderDirection = searchDto.sortDirection() == null ? paginationConfig.getDirection() : searchDto.sortDirection();
        return songRepository.findAll(page, size, orderColumn, orderDirection);
    }

    public List<Song> findAllByAuthorId(Long authorId) {
        return songRepository.findAllByAuthorId(authorId);
    }

    public Song findById(Long id) {

        Song song = songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException("The song is not found by id: %d".formatted(id)));

        songModificationAction.publish(song.getId(), song.getAuthorId(), Action.GET);
        return song;
    }

    @Transactional
    public Song update(Long id, @Valid WriteDto writeDto) {
        //add checking exists unique fields or throw exception
        Song updatedSong = songRepository.findById(id)
                .map(song -> {
                    songMapper.updateSong(writeDto, song);
                    return songRepository.update(id, song);
                })
                .orElseThrow(() -> new SongNotFoundException("The song is not found by id: %d".formatted(id)));
        songModificationAction.publish(updatedSong.getId(), updatedSong.getAuthorId(), Action.UPDATE);
        return updatedSong;
    }


    @Transactional
    @Validated(CreateGroup.class)
    //server.tomcat.threads.virtual.enabled=true
    public void saveCompose(@Valid WriteDto writeDto) {
        CompletableFuture<Song> future = saveSongAsyncCompose(writeDto);
        future.thenAccept((song -> {
            log.info("The song:{} has been saved successfully", song);
        })).exceptionally(ex -> {
            throw new SaveOperationException("General saving error", ex);
        });
    }

    private CompletableFuture<Song> saveSongAsyncCompose(WriteDto writeDto) {
        return CompletableFuture
                .supplyAsync(() -> authorClient.getAuthor(writeDto.authorId()), executorService)
                .thenCompose(maybeAuthor ->
                        maybeAuthor.map(
                                        author -> CompletableFuture.supplyAsync(
                                                () -> Optional.ofNullable(songMapper.toSong(writeDto))
                                                        .map(songRepository::save)
                                                        .orElseThrow(() -> new SongCreationException("The error has been detected during creation a new song")), executorService
                                        ))
                                .orElse(CompletableFuture.failedFuture(new AuthorNotFoundException("The author is not found by id: %d".formatted(writeDto.authorId())))));

    }

    private CompletableFuture<Song> saveSongAsyncComposeCompletionException(WriteDto writeDto) {
        return CompletableFuture.supplyAsync(() -> authorClient.getAuthor(writeDto.authorId()), executorService)
                .thenCompose(maybeAuthor -> {
                            if (maybeAuthor.isEmpty()) {
                                return CompletableFuture.failedFuture(new AuthorNotFoundException("The author is not found by id: %d".formatted(writeDto.authorId())));
                            }
                            return CompletableFuture.supplyAsync(() -> Optional.ofNullable(songMapper.toSong(writeDto))
                                            .map(songRepository::save)
                                            .orElseThrow(() -> new SongCreationException("The error has been detected during creation a new song")), executorService)
                                    .exceptionally(ex -> {
                                        throw new CompletionException(ex);
                                    });

                        }
                );
    }

    @Transactional
    @Validated(CreateGroup.class)
    public Song save(@Valid WriteDto writeDto) {
        //add checking exists unique fields or throw exception
        CompletableFuture<Song> future = saveAsyncInOneFuture(writeDto);
        Song savedSong = future.join();
        songModificationAction.publish(savedSong.getId(), savedSong.getAuthorId(),Action.SAVE);
        log.info("The song:{} has been saved successfully", savedSong);
        return savedSong;
    }


    private CompletableFuture<Song> saveAsyncInOneFuture(WriteDto writeDto) {
        return CompletableFuture.supplyAsync(
                        () ->
                                authorClient.getAuthor(writeDto.authorId())
                        , executorService)
                .thenApply(maybeAuthor -> maybeAuthor.map(
                                author -> Optional.ofNullable(songMapper.toSong(writeDto))
                                        .map(songRepository::save)
                                        .orElseThrow(() -> new SongCreationException("The error has been detected during creation a new song"))
                        )
                        .orElseThrow(() -> new AuthorNotFoundException("The author is not found by id: %d".formatted(writeDto.authorId()))));
    }


    @Transactional
    public boolean delete(Long id) {
        Boolean isDeleted=songRepository.findById(id)
                .map(song -> songRepository.delete(id))
                .orElseThrow(() -> new SongNotFoundException("The song is not found by id: %d".formatted(id)));
        songModificationAction.publish(id,id,Action.DELETE);
        return isDeleted;
    }
}
