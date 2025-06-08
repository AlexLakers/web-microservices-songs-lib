package com.alex.web.microservices.songs.lib.author.events.consumer;

import com.alex.web.microservices.songs.lib.author.events.consumer.model.SongModification;
import com.alex.web.microservices.songs.lib.author.service.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

/**
 * This class as a consumer for get  messages from kafka.
 *The messages can be received from song-service about modification a specific song.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SongModificationConsumer {
    private final CacheService cacheService;

    @Bean
    public Consumer<Message<SongModification>> inputModificationSong() {
        return message -> {
            SongModification songModification = message.getPayload();
            log.debug("The event: {} from: {}", songModification, songModification.getSourceService());
            cacheService.handleSongModification(songModification);
        };
    }
}
