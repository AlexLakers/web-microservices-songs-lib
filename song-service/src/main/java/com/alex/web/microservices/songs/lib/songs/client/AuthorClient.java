package com.alex.web.microservices.songs.lib.songs.client;

import com.alex.web.microservices.songs.lib.songs.client.model.Author;
import com.alex.web.microservices.songs.lib.songs.client.model.Name;
import com.alex.web.microservices.songs.lib.songs.exception.AuthorNotFoundException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.dynamic.DynamicType;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * This class is a HttpClient for remote calls 'authorService'.
 * It uses webClient as a basic functional.
 */
@RequiredArgsConstructor
@Component
@Retry(name = "retry-author-service", fallbackMethod = "getFallbackAuthor")
@CircuitBreaker(name = "circuit-author-service",fallbackMethod = "getFallbackAuthor")
public class AuthorClient {
    private final RestTemplate restTemplate;
    private final WebClient webClient;

    public Optional<Author> getAuthor(Long id) {
        return Optional.ofNullable(webClient
                .get()
                .uri("/authors/{id}",id)
                .retrieve()
                .bodyToMono(Author.class)
                .block());
    }
    private Optional<Author> getFallbackAuthor(Long id, Throwable e) {
        return Optional.empty();
    }
}
