package com.alex.web.microservices.songs.lib.songs.events.producer.model;

import lombok.Value;

@Value
public class SongModification {
    String traceId;
    String action;
    Long songId;
    Long authorId;
    String sourceService;
}
