package com.alex.web.microservices.songs.lib.author.service;

import com.alex.web.microservices.songs.lib.author.client.song.SongClient;
import com.alex.web.microservices.songs.lib.author.client.song.model.Song;
import com.alex.web.microservices.songs.lib.author.events.consumer.model.SongModification;
import com.alex.web.microservices.songs.lib.author.exception.SongNotFoundException;
import com.alex.web.microservices.songs.lib.author.repository.AuthorRepository;
import com.alex.web.microservices.songs.lib.author.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheService {
    private final RedisRepository redisRepository;
    private final SongClient songClient;

    public void handleSongModification(SongModification modification) {
        switch (modification.getAction()) {
            case "GET" -> {
                log.info("Getting song with id:{} in song-service", modification.getSongId());
            }
            case "UPDATE" -> {
                log.info("Updating song with id:{} in song-service", modification.getSongId());
                updateCacheFromRemoteService(modification.getSongId());
                log.info("Updating song with id:{} in cache", modification.getSongId());
            }
            case "SAVE" -> {
                log.info("Saving song with id:{} in song-service", modification.getSongId());
                updateCacheFromRemoteService(modification.getSongId());
                log.info("Saving song with id:{} in cache", modification.getSongId());
            }
            case "DELETE" -> {
                log.info("Deleting song with id:{} in song-service", modification.getSongId());
                redisRepository.deleteSongSetAndValue(modification.getAuthorId(), modification.getSongId());
                log.info("Deleting song with id:{} in cache", modification.getSongId());
            }

        }
    }

    private void updateCacheFromRemoteService(Long songId) {
        Song song = songClient.findBySongId(songId).getBody();
        if (song == null) {
            throw new SongNotFoundException("The song is not found by id:%d".formatted(songId));
        }
        redisRepository.saveSongSetIdAndValue(song);
    }

}
