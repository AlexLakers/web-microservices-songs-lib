package com.alex.web.microservices.songs.lib.songs.events.producer.publisher;

import com.alex.web.microservices.songs.lib.songs.events.producer.model.Action;
import com.alex.web.microservices.songs.lib.songs.events.producer.model.SongModification;
import com.alex.web.microservices.songs.lib.songs.model.Song;
import com.alex.web.microservices.songs.lib.songs.service.SongService;
import io.opentelemetry.api.trace.Span;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

/**
 * This class creates and sends message to own buss of messages.
 * @see SongModificationPublisher publisher.
 */
@RequiredArgsConstructor
@Component
public class SongModificationAction {
    private final SongModificationPublisher publisher;

    public void publish(Long songId, Long authorId, Action action) {
        String traceId = Span.current().getSpanContext().getTraceId();
        SongModification songModification = new SongModification(
                traceId, action.name(), songId, authorId, SongService.class.getSimpleName()
        );
        publisher.getSongModificationSink().emitNext(MessageBuilder.withPayload(songModification).build(),
                Sinks.EmitFailureHandler.FAIL_FAST);
    }
}
