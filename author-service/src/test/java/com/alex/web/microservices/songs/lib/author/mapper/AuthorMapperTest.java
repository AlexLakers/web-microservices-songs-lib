package com.alex.web.microservices.songs.lib.author.mapper;

import com.alex.web.microservices.songs.lib.author.dto.WriteDto;
import com.alex.web.microservices.songs.lib.author.model.Author;
import com.alex.web.microservices.songs.lib.author.model.Name;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthorMapperTest {

    private AuthorMapperImpl authorMapper = new AuthorMapperImpl();

    @Test
    void givenWriteDto_whenMap_thenReturnAuthor() {
       Author expected= Author.builder().name(new Name("firstname", "lastname"))
                .birthdate(LocalDate.of(1993, 1, 1)).build();
        WriteDto givenDto=new WriteDto("firstname","lastname",LocalDate.of(1993, 1, 1));

        Author actual=authorMapper.toAuthor(givenDto);

        Assertions.assertThat(actual).isEqualTo(expected);

    }

    @Test
    void givenWriteDtoAndValidId_whenAuthorExist_thenReturnUpdatedAuthor() {
        WriteDto givenDto=new WriteDto("UPDATEfirstname","UPDATElastname",LocalDate.of(1993, 1, 1));

        Author givenAuthor= Author.builder().name(new Name("firstname", "lastname"))
                .birthdate(LocalDate.of(1993, 1, 1)).build();

        Author expected= Author.builder().name(new Name("UPDATEfirstname", "UPDATElastname"))
                .birthdate(LocalDate.of(1993, 1, 1)).build();

        authorMapper.update(givenDto,givenAuthor);

        Assertions.assertThat(expected.getName().getFirstname()).isEqualTo(givenAuthor.getName().getFirstname());
    }

}