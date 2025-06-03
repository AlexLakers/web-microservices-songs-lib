package com.alex.web.microservices.songs.lib.author.api.rest.controller;

import com.alex.web.microservices.songs.lib.author.dto.WriteDto;
import com.alex.web.microservices.songs.lib.author.exception.AuthorNotFoundException;
import com.alex.web.microservices.songs.lib.author.exception.handler.ErrorResponse;
import com.alex.web.microservices.songs.lib.author.model.Author;
import com.alex.web.microservices.songs.lib.author.model.Name;
import com.alex.web.microservices.songs.lib.author.service.AuthorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = AuthorRestController.class)
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles("test")
@WithMockUser(username = "test", authorities = {"ADMIN", "USER"}, password = "password")
class AuthorRestControllerTest {
    private final Long ID = 1L;
    private final Long NOT_VALID_ID = 10000L;
    private final Author author = Author.builder().id(1L).name(new Name("Test", "Testov")).birthdate(LocalDate.of(1942, 6, 19)).build();
    private final WriteDto givenDto = new WriteDto("firstname", "lastname", LocalDate.of(1993, 1, 1));
    private final ObjectMapper objectMapper;
    @MockitoBean
    private final AuthorService authorService;
    private final MockMvc mockMvc;


    @Test
    @SneakyThrows
    void givenId_whenFoundAuthor_thenReturn200AndBody() {
        Mockito.when(authorService.findById(ID)).thenReturn(author);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1//authors/{id}", ID)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthdate", Matchers.is("1942-06-19")));
    }

    @Test
    @SneakyThrows
    void givenNotFoundId_whenNotFoundAuthor_thenReturn404() {
        Mockito.doThrow(new AuthorNotFoundException("The author with id={%d} is not found".formatted(NOT_VALID_ID))).when(authorService).findById(Mockito.anyLong());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/authors/{id}", NOT_VALID_ID)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "The author with id={%d} is not found".formatted(NOT_VALID_ID)))));
    }

    @Test
    @SneakyThrows
    void givenId_whenFound_thenCallDelete() {
        Mockito.doNothing().when(authorService).delete(ID);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/authors/{id}", ID)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @SneakyThrows
    void givenNotValidID_whenNotFoundAuthor_thenReturn404() {
        Mockito.doThrow(new AuthorNotFoundException("The author with id={%d} is not found".formatted(NOT_VALID_ID))).when(authorService).delete(Mockito.anyLong());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/authors/{id}", ID)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "The author with id={%d} is not found".formatted(NOT_VALID_ID)))));
    }
}