package com.alex.web.microservices.songs.lib.author.events.consumer.model;

import com.alex.web.microservices.songs.lib.author.client.song.model.Song;
import lombok.Value;

@Value
public class SongModification {
    String traceId;
    String action;
    Long songId;
    Long authorId;
    String sourceService;
}
