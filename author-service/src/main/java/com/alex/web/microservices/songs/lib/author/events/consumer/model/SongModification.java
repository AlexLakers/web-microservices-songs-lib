package com.alex.web.microservices.songs.lib.author.events.consumer.model;

import com.alex.web.microservices.songs.lib.author.client.song.model.Song;
import lombok.Value;

/**
 * This class as a set properties(fields) which part of message from kafka.
 * It is used as a deserialization model.
 */
@Value
public class SongModification {
    String traceId;
    String action;
    Long songId;
    Long authorId;
    String sourceService;
}
