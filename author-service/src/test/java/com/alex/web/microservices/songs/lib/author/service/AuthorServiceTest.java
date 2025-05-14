package com.alex.web.microservices.songs.lib.author.service;

import com.alex.web.microservices.songs.lib.author.config.AuthorConfig;
import com.alex.web.microservices.songs.lib.author.dto.WriteDto;
import com.alex.web.microservices.songs.lib.author.exception.AuthorCreationException;
import com.alex.web.microservices.songs.lib.author.exception.AuthorNotFoundException;
import com.alex.web.microservices.songs.lib.author.mapper.AuthorMapper;
import com.alex.web.microservices.songs.lib.author.model.Author;
import com.alex.web.microservices.songs.lib.author.model.Name;
import com.alex.web.microservices.songs.lib.author.model.QAuthor;
import com.alex.web.microservices.songs.lib.author.repository.AuthorRepository;
import com.alex.web.microservices.songs.lib.author.search.SearchDto;
import com.querydsl.core.types.Predicate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {
    private static final Long ID =1L;
   private final Author author= Author.builder().id(1L).name(new Name("Test","Testov")).birthdate(LocalDate.of(1942,6,19)).build();
    private final WriteDto givenDto=new WriteDto("firstname","lastname",LocalDate.of(1993, 1, 1));

    @Mock
    private AuthorMapper authorMapper;
    @Mock
    private AuthorConfig authorConfig;
    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    @Test
    void givenId_whenFindById_thenReturnFoundedAuthor() {
        Mockito.when(authorRepository.findById(ID)).thenReturn(Optional.of(author));

        Author actual = authorService.findById(ID);

        Assertions.assertThat(actual).isEqualTo(author);
    }

    @Test
    void givenNotFoundId_whenNotFound_thenThrowAuthorNotFoundException() {
        Mockito.when(authorRepository.findById(ID)).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(AuthorNotFoundException.class)
                .isThrownBy(()->authorService.findById(ID))
                .withMessage("The author with id={%d} is not found".formatted(ID));
    }

    @Test
    void givenWriteDto_whenSave_thenReturnSavedAuthor(){
        Mockito.when(authorMapper.toAuthor(Mockito.any(WriteDto.class))).thenReturn(author);
        Mockito.when(authorRepository.save(Mockito.any(Author.class))).thenReturn(author);

        Author actual=authorService.save(givenDto);

        Assertions.assertThat(actual).hasFieldOrPropertyWithValue("id", ID);
    }

    @Test
    void givenWriteDto_whenNotSave_thenThrowAuthorCreationException(){
        String message="An error has been detected during creation new author";
        Mockito.when(authorRepository.save(Mockito.any(Author.class))).thenThrow(new AuthorCreationException(message));
        Mockito.when(authorMapper.toAuthor(Mockito.any(WriteDto.class))).thenReturn(author);

        Assertions.assertThatExceptionOfType(AuthorCreationException.class)
                .isThrownBy(()->authorService.save(givenDto))
                .withMessage(message);
    }

    @Test
    void givenWriteDtoAndId_whenUpdate_thenReturnUpdatedAuthor(){
        Mockito.doNothing().when(authorMapper).update(Mockito.any(WriteDto.class),Mockito.any(Author.class));
        Mockito.when(authorRepository.findById(ID)).thenReturn(Optional.of(author));
        Mockito.when(authorRepository.saveAndFlush(author)).thenReturn(author);

        Author actual=authorService.update(givenDto,ID);

        Assertions.assertThat(actual).isEqualTo(author);
        Mockito.verifyNoMoreInteractions(authorMapper,authorRepository);
    }
    @Test
    void givenWriteDtoAndNotFoundId_whenNotFound_thenThrowAuthorNotFoundException() {
        Mockito.when(authorRepository.findById(ID)).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(AuthorNotFoundException.class)
                .isThrownBy(()->authorService.update(givenDto,ID))
                .withMessage("The author with id={%d} is not found".formatted(ID));

        Mockito.verify(authorMapper,Mockito.never()).toAuthor(Mockito.any(WriteDto.class));
        Mockito.verify(authorRepository,Mockito.times(1)).findById(ID);
      Mockito.verify(authorRepository,Mockito.never()).saveAndFlush(author);

    }
    @Test
    void givenId_whenDelete_thenCallDelete(){
        Mockito.when(authorRepository.findById(ID)).thenReturn(Optional.of(author));
        Mockito.doNothing().when(authorRepository).delete(Mockito.any(Author.class));

        authorService.delete(ID);

        Mockito.verify(authorRepository,Mockito.times(1)).findById(ID);
        Mockito.verify(authorRepository,Mockito.times(1)).delete(author);
    }

    @Test
    void givenNotFoundId_whenNotFoundDuringDelete_thenThrowAuthorNotFoundException(){
        Mockito.when(authorRepository.findById(ID)).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(AuthorNotFoundException.class)
                .isThrownBy(()->authorService.delete(ID))
                .withMessage("The author with id={%d} is not found".formatted(ID));

        Mockito.verify(authorRepository,Mockito.times(1)).findById(ID);
        Mockito.verify(authorRepository,Mockito.never()).delete(author);
    }

    @Test
    void givenSearchDto_whenFoundAuthors_thenReturnPageWithContent(){
       SearchDto searchDto=new SearchDto(ID,"Test","Testov",LocalDate.of(1942,6,19),0,2);
       Pageable pageable = PageRequest.of(searchDto.page(),searchDto.size());
       Predicate predicate= QAuthor.author.birthdate.after(searchDto.birthdate()).and(QAuthor.author.name.firstname.containsIgnoreCase("t"));
        Page<Author> page= new PageImpl<>(Collections.singletonList(author),pageable,1);
        Mockito.when(authorRepository.findAll(Mockito.any(Predicate.class),Mockito.any(Pageable.class))).thenReturn(page);

        Page<Author> actual=authorService.findAllBy(searchDto);

        Assertions.assertThat(actual.getContent()).hasSize(1).contains(author);
    }

}