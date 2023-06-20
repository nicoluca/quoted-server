package org.nico.quotedserver.controller;

import org.junit.jupiter.api.Test;
import org.nico.quotedserver.TestUtil;
import org.nico.quotedserver.domain.*;
import org.nico.quotedserver.repository.QuoteRepository;
import org.nico.quotedserver.repository.SourceRepository;
import org.nico.quotedserver.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class SourceRestControllerTest {

    @Value("${spring.security.user.name}")
    private String username;

    @Value("${spring.security.user.password}")
    private String password;

    @MockBean
    private SourceRepository sourceRepository;

    @MockBean
    private QuoteRepository quoteRepository;

    @MockBean
    private ArticleService articleService;

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

        verify(sourceRepository).findAll();
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

        verify(sourceRepository).findAll();
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

        verify(quoteRepository).findBySourceId(1L);
    }

    @Test
    void postValidQuoteToExistingSource() throws Exception {
        Source source = new Article("Test Source", "test.de");
        Quote quote = new Quote("Test Quote", source);
        when(sourceRepository.findById(1L)).thenReturn(java.util.Optional.of(source));
        when(quoteRepository.save(any(Quote.class))).thenReturn(quote);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/sources/1/quotes")
                                .with(user(username).password(password))
                                .with(csrf())
                                .accept("application/json")
                                .contentType("application/json")
                                .content(TestUtil.resourceToString("/json/quote_without_source.json"))
                )
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    assertTrue(contentAsString.contains("Test Quote"));
                    assertTrue(contentAsString.contains("Test Source"));
                });

        verify(sourceRepository).findById(1L);
        verify(quoteRepository).save(quote);
    }

    @Test
    void postInvalidQuoteToExistingSource() throws Exception {
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/sources/1/quotes")
                                .with(user(username).password(password))
                                .with(csrf())
                                .accept("application/json")
                                .contentType("application/json")
                                .content(TestUtil.resourceToString("/json/quote.json"))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void postValidQuoteToNonExistingSource() throws Exception {
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/sources/1/quotes")
                                .with(user(username).password(password))
                                .with(csrf())
                                .accept("application/json")
                                .contentType("application/json")
                                .content(TestUtil.resourceToString("/json/quote_without_source.json"))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteSource() throws Exception {
        Source source = new Article("Test Source", "test.de");
        when(sourceRepository.findById(1L)).thenReturn(Optional.of(source));
        doNothing().when(articleService).delete(any(Article.class));

        this.mockMvc.perform(
                        MockMvcRequestBuilders.delete("/sources/1")
                                .with(user(username).password(password))
                                .with(csrf())
                                .accept("application/json")
                )
                .andExpect(status().isOk());

        verify(sourceRepository).findById(1L);
        verify(articleService).delete((Article) source);
    }

}