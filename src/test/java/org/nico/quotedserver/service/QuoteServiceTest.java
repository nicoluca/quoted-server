package org.nico.quotedserver.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nico.quotedserver.domain.Article;
import org.nico.quotedserver.domain.Author;
import org.nico.quotedserver.domain.Book;
import org.nico.quotedserver.domain.Quote;
import org.nico.quotedserver.repository.ArticleRepository;
import org.nico.quotedserver.repository.BookRepository;
import org.nico.quotedserver.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class QuoteServiceTest {

    @Autowired
    QuoteService quoteService;

    @MockBean
    QuoteRepository quoteRepository;

    @MockBean
    BookRepository bookRepository;

    @MockBean
    ArticleRepository articleRepository;

    private Quote quote;
    private List<Quote> quotes;

    @BeforeEach
    void setUp() {
        quote = Quote.builder()
                .text("Test quote")
                .build();
        quote.setId(1L);

        quotes = List.of(quote);
    }

    @Test
    void saveQuoteWithNoSource() {
        quote.setSource(null);
        assertFalse(quoteService.save(quote).isPresent());
    }

    @Test
    void saveQuoteWithExistingArticleId() {
        Article article = Article.builder()
                .title("Test article")
                .url("https://www.test.com")
                .build();
        article.setId(1L);
        quote.setSource(article);

        when(articleRepository.findById(any())).thenReturn(Optional.of(article));
        when(articleRepository.save(any())).thenReturn(article);
        when(quoteRepository.save(any())).thenReturn(quote);

        assertTrue(quoteService.save(quote).isPresent());
        assertEquals(quote, quoteService.save(quote).get());
    }

    @Test
    void saveQuoteWithExistingArticleUrl() {
        Article article = Article.builder()
                .title("Test article")
                .url("https://www.test.com")
                .build();
        article.setId(1L);
        quote.setSource(article);

        when(articleRepository.findById(any())).thenReturn(Optional.empty());
        when(articleRepository.findByUrl(any())).thenReturn(Optional.of(article));
        when(articleRepository.save(any())).thenReturn(article);
        when(quoteRepository.save(any())).thenReturn(quote);

        assertTrue(quoteService.save(quote).isPresent());
        assertEquals(quote, quoteService.save(quote).get());
    }

    @Test
    void saveQuoteWithNewArticle() {
        Article article = Article.builder()
                .title("Test article")
                .url("https://www.test.com")
                .build();
        article.setId(1L);
        quote.setSource(article);

        when(articleRepository.findById(any())).thenReturn(Optional.empty());
        when(articleRepository.findByUrl(any())).thenReturn(Optional.empty());
        when(articleRepository.save(any())).thenReturn(article);
        when(quoteRepository.save(any())).thenReturn(quote);

        assertTrue(quoteService.save(quote).isPresent());
        assertEquals(quote, quoteService.save(quote).get());
    }

    @Test
    void saveQuoteWithExistingBook() {
        Book book = Book.builder()
                .title("Test book")
                .author(new Author("Firstname", "Lastname"))
                .build();
        book.setId(1L);
        quote.setSource(book);

        when(bookRepository.findById(any())).thenReturn(Optional.of(book));
        when(bookRepository.save(any())).thenReturn(book);
        when(quoteRepository.save(any())).thenReturn(quote);

        assertTrue(quoteService.save(quote).isPresent());
        assertEquals(quote, quoteService.save(quote).get());
    }

    @Test
    void updateQuoteWithoutSource() {
        quote.setSource(null);
        assertFalse(quoteService.update(quote).isPresent());
    }

    @Test
    void updateQuoteWithArticle() {
        Article article = Article.builder()
                .title("Test article")
                .url("https://www.test.com")
                .build();
        article.setId(1L);
        quote.setSource(article);

        when(articleRepository.findById(any())).thenReturn(Optional.of(article));
        when(articleRepository.save(any())).thenReturn(article);
        when(quoteRepository.findById(1L)).thenReturn(Optional.ofNullable(quote));
        when(quoteRepository.save(any())).thenReturn(quote);

        assertTrue(quoteService.update(quote).isPresent());
        assertEquals(quote, quoteService.update(quote).get());
    }

    @Test
    void updateQuoteWithBook() {
        Book book = Book.builder()
                .title("Test book")
                .author(new Author("Firstname", "Lastname"))
                .build();
        book.setId(1L);
        quote.setSource(book);

        when(bookRepository.findById(any())).thenReturn(Optional.of(book));
        when(bookRepository.save(any())).thenReturn(book);
        when(quoteRepository.findById(1L)).thenReturn(Optional.ofNullable(quote));
        when(quoteRepository.save(any())).thenReturn(quote);

        assertTrue(quoteService.update(quote).isPresent());
        assertEquals(quote, quoteService.update(quote).get());
    }
}