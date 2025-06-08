package com.alex.web.microservices.songs.lib.songs.service;

import com.alex.web.microservices.songs.lib.songs.client.AuthorClient;
import com.alex.web.microservices.songs.lib.songs.client.model.Author;
import com.alex.web.microservices.songs.lib.songs.config.PaginationConfig;
import com.alex.web.microservices.songs.lib.songs.dto.WriteDto;
import com.alex.web.microservices.songs.lib.songs.events.producer.model.Action;
import com.alex.web.microservices.songs.lib.songs.events.producer.publisher.SongModificationAction;
import com.alex.web.microservices.songs.lib.songs.exception.SongNotFoundException;
import com.alex.web.microservices.songs.lib.songs.mapper.SongMapperImpl;
import com.alex.web.microservices.songs.lib.songs.model.Song;
import com.alex.web.microservices.songs.lib.songs.repository.SongRepository;
import com.alex.web.microservices.songs.lib.songs.search.PageDto;
import com.alex.web.microservices.songs.lib.songs.search.SearchDto;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class SongServiceTest {
    private final static Long ID = 1L;
    private final static Long NOT_VALID_ID = Long.MAX_VALUE;
    private final Song song = Song.builder().id(ID).name("name").album("album").authorId(ID).build();
    private final WriteDto writeDto = new WriteDto(ID, "name", "album");

    @Mock
    private SongRepository songRepository;
    @Mock
    private PaginationConfig paginationConfig;
    @Mock
    private SongMapperImpl songMapper;
    @Mock
    private AuthorClient authorClient;
    @Mock
    private SongModificationAction songModificationAction;
    @InjectMocks
    private SongService songService;

    @Test
    public void givenSearchDto_whenFindAll_thenReturnPageDto() {
        SearchDto givenSearchDto = new SearchDto(0, 10, "id", "ASC");
        PageDto expectedPageDto = new PageDto(0, 10, 1, 1, Collections.singletonList(song));
        Mockito.when(songRepository.findAll(
                givenSearchDto.page(), givenSearchDto.size(), givenSearchDto.sortColumn(), givenSearchDto.sortDirection()
        )).thenReturn(expectedPageDto);

        PageDto actual = songService.findAll(givenSearchDto);

        Assertions.assertThat(actual).isEqualTo(expectedPageDto);
        Mockito.verifyNoMoreInteractions(paginationConfig);
    }

    @Test
    public void givenId_whenFound_thenReturnSong() {
        Mockito.when(songRepository.findById(ID)).thenReturn(Optional.of(song));
        Mockito.doNothing().when(songModificationAction).publish(Mockito.anyLong(),Mockito.anyLong(),Mockito.any(Action.class));
        Song actual = songService.findById(ID);

        Assertions.assertThat(actual).isEqualTo(song);
    }

    @Test
    public void givenNotValidId_whenNotFound_thenThrowSongNotFoundException() {
        String message = "The song is not found by id: %d".formatted(NOT_VALID_ID);
        Mockito.when(songRepository.findById(NOT_VALID_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(SongNotFoundException.class)
                .isThrownBy(() -> songService.findById(NOT_VALID_ID))
                .withMessage(message);
    }

    @Test
    public void givenWriteDto_whenSave_thenReturnSongWithId() {
        Mockito.when(songRepository.save(Mockito.any(Song.class))).thenReturn(song);
        Mockito.when(songMapper.toSong(Mockito.any(WriteDto.class))).thenReturn(song);
        Mockito.when(authorClient.getAuthor(Mockito.anyLong())).thenReturn(Optional.of(Author.builder().id(ID).build()));
        Mockito.doNothing().when(songModificationAction).publish(Mockito.anyLong(),Mockito.anyLong(),Mockito.any(Action.class));

        Song actual = songService.save(writeDto);

        Assertions.assertThat(actual).isEqualTo(song);
    }

    @Test
    public void givenWriteDto_whenUpdate_thenReturnUpdatedSong() {
        Mockito.when(songRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(song));
        Mockito.doNothing().when(songMapper).updateSong(Mockito.any(WriteDto.class), Mockito.any(Song.class));
        Mockito.when(songRepository.update(Mockito.anyLong(), Mockito.any(Song.class))).thenReturn(song);
        Mockito.doNothing().when(songModificationAction).publish(Mockito.anyLong(),Mockito.anyLong(),Mockito.any(Action.class));

        Song actual = songService.update(ID, writeDto);

        Mockito.verify(songRepository).findById(Mockito.anyLong());
        Mockito.verify(songMapper).updateSong(Mockito.any(WriteDto.class), Mockito.any(Song.class));
        Mockito.verify(songRepository).update(Mockito.anyLong(), Mockito.any(Song.class));
        Assertions.assertThat(actual).isEqualTo(song);
    }

    @Test
    public void givenNotFoundId_whenNotFound_thenThrowSongNotFoundException() {
        String message = "The song is not found by id: %d".formatted(NOT_VALID_ID);
        Mockito.when(songRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());


        Assertions.assertThatExceptionOfType(SongNotFoundException.class)
                .isThrownBy(() -> songService.update(NOT_VALID_ID,writeDto))
                .withMessage(message);
        Mockito.verify(songRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(songRepository, Mockito.never()).update(Mockito.anyLong(), Mockito.any(Song.class));
        Mockito.verify(songMapper, Mockito.never()).updateSong(Mockito.any(WriteDto.class), Mockito.any(Song.class));
    }

    @Test
    public void givenId_whenDelete_thenReturnTrue(){
        Mockito.when(songRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(song));
        Mockito.doReturn(true).when(songRepository).delete(ID);
        Mockito.doNothing().when(songModificationAction).publish(Mockito.anyLong(),Mockito.anyLong(),Mockito.any(Action.class));

        boolean actual=songService.delete(ID);

        Assertions.assertThat(actual).isTrue();
    }
    @Test
    public void givenNotFoundId_whenNotFoundSong_thenThrowSongNotFoundException() {
        String message = "The song is not found by id: %d".formatted(NOT_VALID_ID);
        Mockito.when(songRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(SongNotFoundException.class)
                .isThrownBy(() -> songService.delete(NOT_VALID_ID))
                .withMessage(message);
    }


}