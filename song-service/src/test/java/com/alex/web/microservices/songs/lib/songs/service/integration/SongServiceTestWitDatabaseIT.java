package com.alex.web.microservices.songs.lib.songs.service.integration;

import com.alex.web.microservices.songs.lib.songs.dto.WriteDto;
import com.alex.web.microservices.songs.lib.songs.model.Song;
import com.alex.web.microservices.songs.lib.songs.search.PageDto;
import com.alex.web.microservices.songs.lib.songs.search.SearchDto;
import com.alex.web.microservices.songs.lib.songs.service.SongService;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
@Transactional
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles("test")
class SongServiceTestWitDatabaseIT {
    private static final Long ID = 1L;
    private final SongService songService;
    private final JdbcTemplate jdbcTemplate;
    private static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:17");
    private final Song song =Song.builder()
            .id(ID)
            .name("TestName3")
            .authorId(3L)
            .album("TestAlbum1")
            .build();
    @BeforeAll
    public static void startContainer() {
        postgreSQLContainer.start();
    }

    @AfterAll
    public static void stopContainer() {
        postgreSQLContainer.stop();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    }

    @Test
    public void givenId_whenFoundSong_thenReturnSong() {
        Song actual=songService.findById(ID);

        Assertions.assertThat(actual).isEqualTo(song);
    }
    @Test
    public void givenSearchDto_whenFound_thenReturnPageDto() {
        SearchDto givenSearchDto = new SearchDto(0, 1, "id", "ASC");

        PageDto actual = songService.findAll(givenSearchDto);

        Assertions.assertThat(actual.totalPages()).isEqualTo(3);
        Assertions.assertThat(actual.currentPage()).isEqualTo(0);
        Assertions.assertThat(actual.pageSize()).isEqualTo(1);
        Assertions.assertThat(actual.content()).hasSize(1);
    }
    @Test
    public void givenWriteDtoAndId_whenFieldIsNull_thenNotSettFiled(){
         WriteDto givenWriteDto = new WriteDto(ID,null ,"" );

         Song actual=songService.update(ID, givenWriteDto);

         Assertions.assertThat(actual)
                 .hasFieldOrPropertyWithValue("id", ID)
                 .hasFieldOrPropertyWithValue("name",song.getName())
                 .hasFieldOrPropertyWithValue("album",song.getAlbum());
    }
    @Test/**/
    public void givenWriteDtoAndId_whenUpdate_thenReturnUpdatedSong(){
        WriteDto givenWriteDto = new WriteDto(ID,"UpdatedName" ,"UpdatedAlbum" );

        Song actual=songService.update(ID, givenWriteDto);

        Assertions.assertThat(actual)
                .hasFieldOrPropertyWithValue("id", ID)
                .hasFieldOrPropertyWithValue("name",givenWriteDto.name())
                .hasFieldOrPropertyWithValue("album",givenWriteDto.album());
    }
    @Test
    public void givenWriteDto_whenSave_thenReturnSavedSong(){
        WriteDto givenWriteDto = new WriteDto(1L,"name" ,"nameeee" );

        jdbcTemplate.execute("SELECT SETVAL ('song_id_seq', (SELECT MAX(id) FROM song))");

        Song actual=songService.save(givenWriteDto);

        Assertions.assertThat(actual).hasFieldOrPropertyWithValue("name", givenWriteDto.name())
                .hasFieldOrPropertyWithValue("album", givenWriteDto.album());

    }

}