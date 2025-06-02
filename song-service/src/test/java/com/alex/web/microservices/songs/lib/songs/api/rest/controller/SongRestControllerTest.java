package com.alex.web.microservices.songs.lib.songs.api.rest.controller;

import com.alex.web.microservices.songs.lib.songs.dto.WriteDto;
import com.alex.web.microservices.songs.lib.songs.model.Song;
import com.alex.web.microservices.songs.lib.songs.search.PageDto;
import com.alex.web.microservices.songs.lib.songs.search.SearchDto;
import com.alex.web.microservices.songs.lib.songs.service.SongService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = SongRestController.class)
@ActiveProfiles("test")
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class SongRestControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    @MockitoBean
    private final SongService songService;
    private final static Long ID = 1L;
    private final static Long NOT_VALID_ID = Long.MAX_VALUE;
    private final Song song = Song.builder().id(ID).name("name").album("album").authorId(ID).build();
    private final WriteDto writeDto = new WriteDto(ID, "name", "album");

    @SneakyThrows
    @Test
    public void givenWriteDto_whenCreate_theReturnCreatedStatusAndNotEmptyBody() {
        Mockito.when(songService.save(Mockito.any(WriteDto.class))).thenReturn(song);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/songs")
                        //.with()
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(writeDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(ID));
    }

    @Test
    @SneakyThrows
    public void givenWriteDto_whenUpdate_thenReturnStatusOkAndNotEmptyBody() {
        Mockito.when(songService.update(Mockito.anyLong(), Mockito.any(WriteDto.class))).thenReturn(song);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/songs/{id}", ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(writeDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(song)));
    }

    @Test
    @SneakyThrows
    public void givenSearchDto_wheFind_thenReturnStatusOkAndPageDto(){
        SearchDto searchDto = new SearchDto(0,1,"id","ASC");
        List<Song> content= Collections.singletonList(song);
        PageDto pageDto=new PageDto(0,1,3,3,content);
        Mockito.when(songService.findAll(searchDto)).thenReturn(pageDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/songs/search")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsString(searchDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(3));

    }
}