package com.alex.web.microservices.songs.lib.frontend.controller;

import com.alex.web.microservices.songs.lib.frontend.client.author.AuthorClient;
import com.alex.web.microservices.songs.lib.frontend.client.author.SearchDto;
import com.alex.web.microservices.songs.lib.frontend.client.author.WriteDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * This  controller for interaction with author-service using REST-client {@link AuthorClient cleint}
 *  It represents author.html and authors.html
 */
@Controller
@RequestMapping("/dashboard/services/authors")
@RequiredArgsConstructor
@Slf4j
public class AuthorController {
    private final AuthorClient authorClient;

    @GetMapping("/home")
    public String authors(@ModelAttribute SearchDto search, Model model) {
        log.info("--start 'authors view with author-list in model' search mvc endpoint--");
        model.addAttribute("search", search);
        return "/author/authors";
    }

    @GetMapping("/{id}")
    public String getAuthor(Model model,
                            @PathVariable Long id) {
        log.info("--start 'author view with author in model' search mvc endpoint--");
        model.addAttribute("author", authorClient.getAuthor(id));
        return "author/author";
    }

    @GetMapping
    public String getAuthors(Model model,
                             @RegisteredOAuth2AuthorizedClient("keycloak") OAuth2AuthorizedClient authClient,
                             @RequestParam(required = false) Long id,
                             @RequestParam(required = false) String firstname,
                             @RequestParam(required = false) String lastname,
                             @RequestParam(required = false) LocalDate birthdate,
                             @RequestParam(required = false) Integer page,
                             @RequestParam(required = false) Integer size) {
        log.info("--start 'authors view with author-list and search params in model' search mvc endpoint--");
        SearchDto dto = new SearchDto(id, firstname, lastname, birthdate, page, size);
        model.addAttribute("authors", authorClient.getAuthors(authClient, dto));
        model.addAttribute("search", dto);
        return "author/authors";
    }

    @PostMapping("/{id}/update")
    public String updateAuthor(Model model,
                               @PathVariable Long id,
                               WriteDto writeDto) {
        log.info("--start 'author view with author in model' update mvc endpoint--");
        model.addAttribute("author", authorClient.updateAuthor(id, writeDto));
        return "author/author";
    }

    @PostMapping("/{id}/delete")
    public String deleteAuthor(@PathVariable Long id){
        log.info("--start 'home view' delete mvc endpoint--");
        authorClient.deleteAuthor(id);
        return "redirect:/dashboard/services/authors/home";
    }
}
