package com.alex.web.microservices.songs.lib.author.service.integration;

import com.alex.web.microservices.songs.lib.author.dto.WriteDto;
import com.alex.web.microservices.songs.lib.author.mapper.AuthorMapper;
import com.alex.web.microservices.songs.lib.author.model.Author;
import com.alex.web.microservices.songs.lib.author.model.Name;
import com.alex.web.microservices.songs.lib.author.model.QAuthor;
import com.alex.web.microservices.songs.lib.author.search.SearchDto;
import com.alex.web.microservices.songs.lib.author.service.AuthorService;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;
import java.util.Collections;
import java.util.stream.Stream;

@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Transactional
class AuthorServiceIT {
    private static final Long ID=1L;
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17");
    private final AuthorService authorService;
    private final EntityManager entityManager;
    private final WriteDto givenDto=new WriteDto("firstname","lastname",LocalDate.of(1993, 1, 1));

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
    void givenIdAndWriteDto_whenUpdate_thenReturnUpdatedAuthor(){
        Author author= Author.builder().id(ID).name(new Name("Test","Testov")).birthdate(LocalDate.of(1942,6,19)).build();

        Author actual=authorService.update(givenDto,1L);

        Assertions.assertThat(actual.getName()).hasFieldOrPropertyWithValue("firstname","firstname");
    }
    @Test
    void givenWriteDto_whenSave_theReturnSavedAuthor(){

        entityManager.createNativeQuery("SELECT SETVAL('author_id_seq', (SELECT MAX(id) FROM author))").getSingleResult();

        Author authorPersistent=authorService.save(givenDto);

        Assertions.assertThat(authorPersistent).hasFieldOrPropertyWithValue("id",3L);
    }
    @ParameterizedTest
    @MethodSource("getValidArgs")
    void givenSearchDto_whenFoundAuthors_thenReturnPageWithContent(SearchDto givenDto, int expectedSize){
        Page<Author> actual=authorService.findAllBy(givenDto);

        Assertions.assertThat(actual.getContent()).hasSize(expectedSize);
    }

    static Stream<Arguments> getValidArgs(){
        return Stream.of(
                Arguments.of(new SearchDto(null,"T",null,null,0,2),2),
                Arguments.of(new SearchDto(null,"T1",null,null,0,2),1),
                Arguments.of(new SearchDto(ID,null,null,null,0,2),1),
                Arguments.of(new SearchDto(null,null,null,LocalDate.of(1941,6,19),0,2),2)
        );
    }

    @ParameterizedTest
    @MethodSource("getArgsForValidate")
    void givenWriteDto_whenNotValidate_thenThrowConstraintViolationException(WriteDto givenDto, String message){
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(()->authorService.save(givenDto))
                .withMessageContaining(message);
    }
    static Stream<Arguments> getArgsForValidate(){
        return Stream.of(
                Arguments.of(new WriteDto(null,"lastname",LocalDate.of(1993, 1, 1)),"The firstname can't be empty or null"),
                Arguments.of(new WriteDto("firstname",null,LocalDate.of(1993, 1, 1)),"The lastname can't be empty or null")

        );
    }
}