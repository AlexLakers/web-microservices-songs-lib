package com.alex.web.microservices.songs.lib.songs.mapper;

import com.alex.web.microservices.songs.lib.songs.dto.WriteDto;
import com.alex.web.microservices.songs.lib.songs.model.Song;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("test")
class SongMapperTest {
    private final SongMapperImpl songMapper = new SongMapperImpl();
    private final WriteDto writeDto = new WriteDto(1L, "testname", "testalbum");

    @Test
    public void givenWriteDto_whenMap_thenReturnSong() {
        WriteDto writeDto = new WriteDto(null, "testname", "testalbum");

        Song actual = songMapper.toSong(writeDto);

        Assertions.assertThat(actual).hasFieldOrPropertyWithValue(Song.Fields.name, writeDto.name())
                .hasFieldOrPropertyWithValue(Song.Fields.album, writeDto.album());
    }

    @Test
    public void givenWriteDtoAndSongEntity_whenUpdate_thenReturnUpdatedSong(){
        Song givenEntity=Song.builder().id(1L).name("SourceName").album("SourceAlbum").authorId(1L).build();
        Song expected=Song.builder().id(1L).name("testname").album("testalbum").authorId(1L).build();

         songMapper.updateSong(writeDto,givenEntity);

         Assertions.assertThat(expected).isEqualTo(givenEntity);
    }

}