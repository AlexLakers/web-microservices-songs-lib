package com.alex.web.microservices.songs.lib.songs.events.producer.model;

import com.alex.web.microservices.songs.lib.songs.events.producer.publisher.SongModificationPublisher;
import lombok.Value;

/**
 * This is model for data transfer using message-broker.
 * @see SongModificationPublisher publisher
 */
@Value
public class SongModification {
    String traceId;
    String action;
    Long songId;
    Long authorId;
    String sourceService;
}
