package com.alex.web.microservices.songs.lib.author.repository.integration;

import com.alex.web.microservices.songs.lib.author.model.Author;
import com.alex.web.microservices.songs.lib.author.model.Name;
import com.alex.web.microservices.songs.lib.author.model.QAuthor;
import com.alex.web.microservices.songs.lib.author.repository.AuthorRepository;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestConstructor;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;
import java.util.List;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class AuthorRepositoryIT {
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17");
    private final AuthorRepository authorRepository;

    @BeforeAll
    static void startContainer() {
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    }
    @AfterAll
    static void stopContainer() {
        postgreSQLContainer.stop();
    }

    @Test
    void givenFirstnameAndLastname_whenExists_thenReturnsTrue() {
        boolean actual=authorRepository.existByFirstNameAndLastname("Test", "Testov");

        Assertions.assertThat(actual).isTrue();
    }

    @Test
    void givenPageableAndPredicate_whenDoPagination_thenReturnPage(){
       List<Author> content=List.of(
                Author.builder().id(1L).name(new Name("Test","Testov")).birthdate(LocalDate.of(1942,6,19)).build(),
                Author.builder().id(2L).name(new Name("Test1","Testov1")).birthdate(LocalDate.of(1958,8,29)).build());
        Pageable givenPageable=PageRequest.of(0,2);
        Predicate predicate=QAuthor.author.name.firstname.containsIgnoreCase("Test");

       PageImpl<Author> expected= new PageImpl<>(content,givenPageable,content.size());

       Page<Author> actual=authorRepository.findAll(predicate,givenPageable);

       Assertions.assertThat(actual.getContent()).isEqualTo(expected.getContent());

    }
}