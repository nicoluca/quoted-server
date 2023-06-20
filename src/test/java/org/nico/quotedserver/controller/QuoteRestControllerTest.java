package org.nico.quotedserver.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.nico.quotedserver.TestUtil;
import org.nico.quotedserver.domain.Article;
import org.nico.quotedserver.domain.Quote;
import org.nico.quotedserver.repository.ArticleRepository;
import org.nico.quotedserver.repository.QuoteRepository;
import org.nico.quotedserver.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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


@WebMvcTest(QuoteRestController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class QuoteRestControllerTest {
    @Value("${spring.security.user.name}")
    private String username;

    @Value("${spring.security.user.password}")
    private String password;

    @MockBean
    QuoteService quoteService;

    @MockBean
    QuoteRepository quoteRepository;

    @MockBean
    ArticleRepository articleRepository;

    @Autowired
    private MockMvc mockMvc;

    private Quote quote;
    private List<Quote> quotes;

    @BeforeEach
    void setUp() {
        quote = Quote.builder()
                .text("Test quote")
                .source(
                        Article.builder()
                            .url("test.de")
                            .build()
                )
                .build();
        quote.setId(1L);

        quotes = new ArrayList<>();
        quotes.add(quote);
    }

    @Test
    void getAllQuotes() throws Exception {
        when(quoteRepository.findAll()).thenReturn(quotes);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/quotes")
                                .accept("application/json")
                                .with(user(username).password(password))
                )
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String contentString = result.getResponse().getContentAsString();
                    assertTrue(contentString.contains("test.de"));
                    assertTrue(contentString.contains("Test quote"));
                });

        verify(quoteRepository).findAll();
    }

    @Test
    void getRandomQuote() throws Exception {
        when(quoteService.randomQuote()).thenReturn(quote);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/quotes/random")
                                .accept("application/json")
                                .with(user(username).password(password))
                )
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String contentString = result.getResponse().getContentAsString();
                    assertTrue(contentString.contains("test.de"));
                    assertTrue(contentString.contains("Test quote"));
                });

        verify(quoteService).randomQuote();
    }

    @Test
    void getQuotesByString() throws Exception {
        when(quoteRepository.findByTextContaining("test")).thenReturn(quotes);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/quotes/search")
                                .accept("application/json")
                                .with(user(username).password(password))
                                .param("string", "test")
                )
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String contentString = result.getResponse().getContentAsString();
                    assertTrue(contentString.contains("test.de"));
                    assertTrue(contentString.contains("Test quote"));
                });

        verify(quoteRepository).findByTextContaining("test");
    }

    @Test
    @Disabled("Moved functionality to source rest controller, can be deleted once finalised.")
    void postValidQuote() throws Exception {
        when(quoteService.save(any(Quote.class))).thenReturn(Optional.ofNullable(quote));

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/quotes")
                                .accept("application/json")
                                .with(user(username).password(password))
                                .with(csrf())
                                .contentType("application/json")
                                .content(TestUtil.resourceToString("/json/quote.json"))
                )
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String contentString = result.getResponse().getContentAsString();
                    assertTrue(contentString.contains("test.de"));
                    assertTrue(contentString.contains("Test quote"));
                });

        verify(quoteService).save(any(Quote.class));
    }

    @Test
    @Disabled("Moved functionality to source rest controller, can be deleted once finalised.")
    void postInvalidQuote() throws Exception {
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/quotes")
                                .accept("application/json")
                                .with(user(username).password(password))
                                .with(csrf())
                                .contentType("application/json")
                                .content("{\"text\": \"Test quote\"}")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateQuote() throws Exception {
        when(quoteService.update(any(Quote.class))).thenReturn(Optional.ofNullable(quote));

        this.mockMvc.perform(
                        MockMvcRequestBuilders.put("/quotes/1")
                                .accept("application/json")
                                .with(user(username).password(password))
                                .with(csrf())
                                .contentType("application/json")
                                .content(TestUtil.resourceToString("/json/quote.json"))
                )
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String contentString = result.getResponse().getContentAsString();
                    assertTrue(contentString.contains("test.de"));
                    assertTrue(contentString.contains("Test quote"));
                });

        verify(quoteService).update(any(Quote.class));
    }

    @Test
    void deleteExistingQuote() throws Exception {
        Quote quote = new Quote("Test", null);
        when(quoteRepository.findById(1L)).thenReturn(Optional.of(quote));
        doNothing().when(quoteRepository).delete(any(Quote.class));

        this.mockMvc.perform(
                        MockMvcRequestBuilders.delete("/quotes/1")
                                .accept("application/json")
                                .with(user(username).password(password))
                                .with(csrf())
                )
                .andExpect(status().isOk());
    }

    @Test
    void deleteNonExistingQuote() throws Exception {
        when(quoteRepository.findById(1L)).thenReturn(Optional.empty());

        this.mockMvc.perform(
                        MockMvcRequestBuilders.delete("/quotes/1")
                                .accept("application/json")
                                .with(user(username).password(password))
                                .with(csrf())
                )
                .andExpect(status().isNotFound());
    }

}