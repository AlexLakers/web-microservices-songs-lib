package com.alex.web.microservices.songs.lib.author.repository;

import com.alex.web.microservices.songs.lib.author.model.Author;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface AuthorRepository extends JpaRepository<Author, Long>, QuerydslPredicateExecutor<Author> {
    @Query(value = "SELECT EXISTS(SELECT 1 FROM author WHERE first_name=:firstname and last_name=:lastname)",nativeQuery = true)
    boolean existByFirstNameAndLastname(String firstname, String lastname);

    Page<Author> findAllBy(Predicate predicate, Pageable pageable);

}
