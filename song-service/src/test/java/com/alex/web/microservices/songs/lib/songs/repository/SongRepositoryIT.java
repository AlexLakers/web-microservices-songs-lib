package com.alex.web.microservices.songs.lib.songs.repository;

import com.alex.web.microservices.songs.lib.songs.dto.WriteDto;
import com.alex.web.microservices.songs.lib.songs.model.Song;
import com.alex.web.microservices.songs.lib.songs.search.PageDto;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.*;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.swing.text.html.parser.Entity;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Transactional
@ActiveProfiles("test")
@RequiredArgsConstructor
class SongRepositoryIT {

    private static final Long ID=1L;
    private  final SongRepository songRepository;
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
    public void givenId_whenFoundEntity_thenReturnEntityWithId(){
        Optional<Song> actual=songRepository.findById(ID);

        Assertions.assertThat(actual.isPresent()).isTrue();
        Assertions.assertThat(actual.get().getId()).isEqualTo(ID);
    }

    @Test
    public void givenNotValidId_whenNotFound_thenReturnEmptyOptional(){
        Optional<Song> actual=songRepository.findById(Long.MAX_VALUE);

        Assertions.assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    public void givenWriteDto_whenSave_thenReturnPersistentEntity(){
        Song transientSong=Song.builder().authorId(3L).name("TestName4").album("TestAlbum4").build();
        jdbcTemplate.execute("SELECT SETVAL ('song_id_seq', (SELECT MAX(id) FROM song))");

        Song actual=songRepository.save(transientSong);

        Assertions.assertThat(actual).hasFieldOrPropertyWithValue(Song.Fields.id,4L);
    }

    @Test
    public void givenIdAndWriteDto_whenUpdate_thenReturnUpdatedEntity(){
        //WriteDto song=new WriteDto(ID,"UpdatedTestName","UpdatedTestAlbum");

        Song actual=songRepository.update(ID,song);

        Assertions.assertThat(actual)
                .hasFieldOrPropertyWithValue(Song.Fields.id,ID)
                .hasFieldOrPropertyWithValue(Song.Fields.name,song.getName());
    }

    @Test
    public void givenParamsForPagination_whenFind_thenReturnPageDto(){
      PageDto actual=songRepository.findAll(0,3,"name","DESC");

        Assertions.assertThat(actual.content()).hasSize(3).contains(song);
        Assertions.assertThat(actual.totalElements()).isEqualTo(3);
        Assertions.assertThat(actual.totalPages()).isEqualTo(1);
    }

    @Test
    public void givenId_whenDelete_thenNotFound(){
        songRepository.delete(ID);

        Long count=jdbcTemplate.queryForObject("SELECT COUNT(ID) FROM song WHERE id=?",Long.class,ID);
        Assertions.assertThat(count).isEqualTo(0L);
    }

    @ParameterizedTest
    @MethodSource("getArgs")
    public void givenNameAndAuthorId_whenExist_thenReturnTrue(String name,Long id,boolean expected){
        boolean actual=songRepository.existByNameAndAuthorId(name,id);

        Assertions.assertThat(actual).isEqualTo(expected);
    }
    static Stream<Arguments> getArgs(){
        return Stream.of(
                Arguments.of("UnknownName",Long.MAX_VALUE,false),
                Arguments.of("UnknownName",ID,false),
                Arguments.of("TestName1",ID,true)
        );
    }

    @Test
    public void givenAuthorId_whenFound_thenReturnListSong(){
        List<Song> actual=songRepository.findAllByAuthorId(3L);

        Assertions.assertThat(actual).hasSize(1).contains(song);
    }
    @Test
    public void givenNotValidAuthorId_whenNotFound_thenReturnEmptyList(){
        List<Song> actual=songRepository.findAllByAuthorId(Long.MAX_VALUE);

        Assertions.assertThat(actual).isEmpty();
    }

    @Test
    public void givenListIds_whenFound_thenReturnSongsList(){
       List<Long> givenIds= Arrays.asList(1L,2L,3L);

       List<Song> actual=songRepository.findAllByIds(givenIds);

       Assertions.assertThat(actual).hasSize(3).contains(song);
    }

}