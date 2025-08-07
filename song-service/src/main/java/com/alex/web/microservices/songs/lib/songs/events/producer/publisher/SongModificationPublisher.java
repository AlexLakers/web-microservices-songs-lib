package com.alex.web.microservices.songs.lib.songs.events.producer.publisher;

import com.alex.web.microservices.songs.lib.songs.events.producer.model.SongModification;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

import java.util.function.Supplier;

/**
 * This class is publisher. It defines own buss of messages.
 * Also it defines the output channel and action for sending message to message-broker.
 */

@Configuration
@Getter
public class SongModificationPublisher {

    private Sinks.Many<Message<SongModification>> songModificationSink=Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE,false);

    /**
     * In here we just subscribe on buss and wait for messages from there.
     * @return supplier-bean.
     */

    @Bean
    public Supplier<Flux<Message<SongModification>>> outputSongModification(){
        return ()->songModificationSink.asFlux();
    }
}
