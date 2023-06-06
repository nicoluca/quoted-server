package org.nico.quotedserver.rest;

import org.junit.jupiter.api.Test;
import org.nico.quotedserver.domain.*;
import org.nico.quotedserver.repository.QuoteRepository;
import org.nico.quotedserver.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SourceRestController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class SourceRestControllerTest {

    @Value("${spring.security.user.name}")
    private String username;

    @Value("${spring.security.user.password}")
    private String password;

    @MockBean
    SourceRepository sourceRepository;

    @MockBean
    QuoteRepository quoteRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllSourcesForEmptySources() throws Exception {
        when(sourceRepository.findAll()).thenReturn(new ArrayList<>());

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/sources")
                                .with(user(username).password(password))
                                .accept("application/json")
                )
                .andExpect(status().isOk());
    }

    @Test
    void getAllSourcesForNonEmptySources() throws Exception {
        List<Source> sources = new ArrayList<>();
        sources.add(new Article("Test Source", "test.de"));
        sources.add(new Book("Test Source", new Author("Firstname", "Lastname")));
        when(sourceRepository.findAll()).thenReturn(sources);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/sources")
                                .with(user(username).password(password))
                                .accept("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    assertTrue(contentAsString.contains("Test Source"));
                    assertTrue(contentAsString.contains("Firstname"));
                    assertTrue(contentAsString.contains("article"));
                    assertTrue(contentAsString.contains("book"));
                });
    }

    @Test
    void quotesBySource() throws Exception {
        List<Quote> quotes= new ArrayList<>();
        quotes.add(new Quote("Test Quote", Article.builder().title("Test Source").build()));

        when(quoteRepository.findBySourceId(1L)).thenReturn(quotes);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/sources/1/quotes")
                                .with(user(username).password(password))
                                .accept("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    assertTrue(contentAsString.contains("Test Quote"));
                    assertTrue(contentAsString.contains("Test Source"));
                });
    }
}