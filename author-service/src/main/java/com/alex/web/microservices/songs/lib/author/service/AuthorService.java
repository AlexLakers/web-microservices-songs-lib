package com.alex.web.microservices.songs.lib.author.service;

import com.alex.web.microservices.songs.lib.author.config.AuthorConfig;
import com.alex.web.microservices.songs.lib.author.model.QAuthor;
import com.alex.web.microservices.songs.lib.author.search.PredicateBuilder;
import com.alex.web.microservices.songs.lib.author.search.SearchDto;
import com.alex.web.microservices.songs.lib.author.exception.AuthorCreationException;
import com.alex.web.microservices.songs.lib.author.model.Author;
import com.alex.web.microservices.songs.lib.author.repository.AuthorRepository;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorConfig authorConfig;

    public Optional<Author> findById(Long id) {
        return authorRepository.findById(id);
    }

    public boolean existByFirstNameAndLastname(String firstName, String lastName) {
        return authorRepository.existByFirstNameAndLastname(firstName, lastName);
    }

    public Author save(Author author) {
        return Optional.ofNullable(authorRepository.save(author))
                .orElseThrow(() -> new AuthorCreationException("Creation error"));
    }

    public Page<Author> findAllBy(SearchDto searchDto) {
        Integer page = searchDto.page() == null ? authorConfig.getDefaultPage() : searchDto.page();
        Integer size = searchDto.size() == null ? authorConfig.getDefaultSize() : searchDto.size();
        Pageable pageable = PageRequest.of(page, size);

        Predicate predicate = PredicateBuilder.builder()
                .add(searchDto.id(), QAuthor.author.id::eq)
                .add(searchDto.firstname(), QAuthor.author.name.firstname::containsIgnoreCase)
                .add(searchDto.lastname(), QAuthor.author.name.lastname::containsIgnoreCase)
                .add(searchDto.birthdate(), QAuthor.author.birthdate::after)
                .buildAnd();
        return authorRepository.findAllBy(predicate, pageable);
    }
}
