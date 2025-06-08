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

@Configuration
@Getter
public class SongModificationPublisher {

    private Sinks.Many<Message<SongModification>> songModificationSink=Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE,false);

    @Bean
    public Supplier<Flux<Message<SongModification>>> outputSongModification(){
        return ()->songModificationSink.asFlux();
    }
}
