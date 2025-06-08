package com.alex.web.microservices.songs.lib.frontend.client.author;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorClient {
    private final RestTemplate restTemplate;
    private final OAuth2AuthorizedClientService authorizedClientService;
    public final String GATEWAY_AUTHOR_URI="http://localhost:8072/authors";

    public Author getAuthor( Long id) {
        return WebClient.create()
                .get()
                .uri(GATEWAY_AUTHOR_URI+"/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getRegisteredOAuth2AuthorizedClient().getAccessToken().getTokenValue())
                .retrieve()
                .bodyToMono(Author.class)
                .block();


    }

    public List<Author> getAuthors(OAuth2AuthorizedClient authorizedClient, SearchDto dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + authorizedClient.getAccessToken().getTokenValue());
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        String uri = UriComponentsBuilder.fromUriString(GATEWAY_AUTHOR_URI)
                .queryParam("id", dto.id())
                .queryParam("firstname", dto.firstname())
                .queryParam("lastname", dto.lastname())
                .queryParam("birthdate", dto.birthdate())
                .queryParam("page", dto.page())
                .queryParam("size", dto.size())
                .build().toUriString();
        ResponseEntity<List<Author>> resp = restTemplate.exchange(uri,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                });
        return resp.getBody();
    }

    public Author updateAuthor( Long id, WriteDto dto) {
        return WebClient.create()
                .put()
                .uri(GATEWAY_AUTHOR_URI+"/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getRegisteredOAuth2AuthorizedClient().getAccessToken().getTokenValue())
                .bodyValue(dto)
                .retrieve()
                .bodyToFlux(Author.class)
                .blockFirst();
    }

    public void deleteAuthor( Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + getRegisteredOAuth2AuthorizedClient().getAccessToken().getTokenValue());
        restTemplate.delete(GATEWAY_AUTHOR_URI+"/{id}", id, new HttpEntity<>(headers), HttpMethod.DELETE);
    }

    public Author createAuthor(Author author) {
        return WebClient.create()
                .post()
                .uri(GATEWAY_AUTHOR_URI)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getRegisteredOAuth2AuthorizedClient().getAccessToken().getTokenValue())
                .bodyValue(author)
                .retrieve()
                .bodyToMono(Author.class)
                .block();
    }

    private OAuth2AuthorizedClient getRegisteredOAuth2AuthorizedClient() {
        return (OAuth2AuthorizedClient) Optional.ofNullable((OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
                .map(oauthToken -> authorizedClientService.loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName()))
                .orElseThrow(() -> new UsernameNotFoundException("Could not find authorized client"));

    }
}
