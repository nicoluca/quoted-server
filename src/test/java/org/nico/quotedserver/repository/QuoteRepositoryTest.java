package org.nico.quotedserver.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.nico.quotedserver.domain.*;

import org.nico.quotedserver.util.TestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties") // TODO Use in-cache H2 instead
@ComponentScan(basePackages = "org.nico.quotedserver.repository")
@EnableJpaAuditing
class QuoteRepositoryTest {

    @Autowired
    private QuoteRepository quoteRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private ArticleRepository articleRepository;

    private Book book;
    private Article article;

    @BeforeEach
    void setUp() {
        Author author = new Author("Test", "Test");
        authorRepository.save(author);
        book = new Book("Test book", author);
        bookRepository.save(book);
        article = new Article("Test article", "Test article");
        articleRepository.save(article);
    }

    // delete all quotes from the database
    @AfterEach
    void tearDown() {
        quoteRepository.deleteAll(quoteRepository.findAll());
        bookRepository.deleteAll(bookRepository.findAll());
        authorRepository.deleteAll(authorRepository.findAll());
        articleRepository.deleteAll(articleRepository.findAll());
    }

    @Test
    @DisplayName("Create quote for existing book")
    void createValidQuote() {
        Quote quote = new Quote("Test quote", book);
        quoteRepository.save(quote);

        assertEquals(quote, quoteRepository.findById(quote.getId()).get());
        assertEquals(1, TestUtil.countIterable(quoteRepository.findAll()));
    }

    @Test
    @DisplayName("Create quote for non-existing book")
    void createInvalidQuote() {
        Author authorTest = new Author("Test", "Test create invalid quote");
        Book bookTest = new Book("Test boook create invalid quote", authorTest);
        Quote quote = new Quote("Test quote create invalid quote", bookTest);
        quoteRepository.save(quote);
        assertThrows(InvalidDataAccessApiUsageException.class, () -> quoteRepository.findAll()); // TODO unclear why this is only thrown when findAll() is called
        quoteRepository.delete(quote);
        assertEquals(0, TestUtil.countIterable(quoteRepository.findAll()));
    }

    @Test
    void readById() {
        assertEquals(Optional.empty(), quoteRepository.findById(1L));
    }

    @Test
    void readAll() {
        Quote quote1 = new Quote("Test quote 1", book);
        Quote quote2 = new Quote("Test quote 2", book);
        Quote quote3 = new Quote("Test quote 3", article);

        quoteRepository.save(quote1);
        quoteRepository.save(quote2);
        quoteRepository.save(quote3);

        assertEquals(3, TestUtil.countIterable(quoteRepository.findAll()));
    }

    @Test
    void update() {
        Quote quote = new Quote("Test quote", book);
        quoteRepository.save(quote);

        quote.setText("Test quote updated");
        quoteRepository.save(quote);

        assertEquals("Test quote updated", quoteRepository.findById(quote.getId()).get().getText());
    }

    @Test
    void delete() {
        Quote quote = new Quote("Test quote", book);
        quoteRepository.save(quote);

        quoteRepository.delete(quote);

        assertEquals(Optional.empty(), quoteRepository.findById(quote.getId()));
    }

    @Test
    void testLastEdited() {
        Quote quote = new Quote("Test quote", book);
        quoteRepository.save(quote);

        assertEquals(quote.getLastEdited(), quoteRepository.findById(quote.getId()).get().getLastEdited());

        LocalDate today = LocalDate.now();
        LocalDate lastEdited = quote.getLastEdited().toLocalDateTime().toLocalDate();
        assertEquals(today, lastEdited);
    }

    @Test
    @DisplayName("Load test - create 1000 new quotes with books and authors, read all in under 1 second")
    void loadTest() {
        Instant start = Instant.now();
        for (int i = 0; i < 1000; i++) {
            Author author = new Author("Test", "Test");
            authorRepository.save(author);
            Book book = new Book("Test Title", author);
            bookRepository.save(book);
            Quote quote = new Quote("Test quote " + i, book);
            quoteRepository.save(quote);
        }
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        assertTrue(duration.toMillis() < 1000);
        assertEquals(1000, TestUtil.countIterable(quoteRepository.findAll()));
    }
}