package org.nico.quotedserver.repository;

import org.junit.jupiter.api.*;
import org.nico.quotedserver.domain.Article;
import org.nico.quotedserver.domain.Author;
import org.nico.quotedserver.domain.Book;
import org.nico.quotedserver.domain.Quote;
import org.nico.quotedserver.util.TestUtilTest;
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
        book = bookRepository.save(book);
        article = new Article("Test article", "Test article");
        article = articleRepository.save(article);
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
        assertEquals(1, TestUtilTest.countIterable(quoteRepository.findAll()));
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
        assertEquals(0, TestUtilTest.countIterable(quoteRepository.findAll()));
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

        assertEquals(3, TestUtilTest.countIterable(quoteRepository.findAll()));
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
    @DisplayName("Load test - create 1000 new quotes with books and authors, read all in under 3 second")
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
        assertTrue(duration.toMillis() < 3000);
        assertEquals(1000, TestUtilTest.countIterable(quoteRepository.findAll()));
    }

    @Test
    void findBySourceId() {
        Quote quote = new Quote("Test quote", book);
        Quote savedQuote = quoteRepository.save(quote);
        assertFalse(quoteRepository.findBySourceId(savedQuote.getSource().getId()).isEmpty());
        assertTrue(quoteRepository.findBySourceId(0L).isEmpty());
        assertTrue(quoteRepository.findBySourceId(article.getId()).isEmpty());
    }

    @Test
    void findByTextContaining() {
        Author author = new Author("AuthorTest", "Test");
        author = authorRepository.save(author);
        Book book = new Book("Test Title", author);
        book = bookRepository.save(book);
        Quote quote = new Quote("Test quote", book);
        quoteRepository.save(quote);
        assertFalse(quoteRepository.findByTextContaining("Test").isEmpty());
        assertFalse(quoteRepository.findByTextContaining("TEST").isEmpty());
        assertFalse(quoteRepository.findByTextContaining("AUTHORTEST").isEmpty());
        assertTrue(quoteRepository.findByTextContaining("Random").isEmpty());

        Article article = Article.builder().title("test.com").build();
        article = articleRepository.save(article);
        Quote quote2 = new Quote("Test quote", article);
        quoteRepository.save(quote2);
        assertFalse(quoteRepository.findByTextContaining("Test.Com").isEmpty());
    }

    @Test
    @Disabled("Cascade delete does not work for repositories. Services to be used instead for now.")
    void testCascadingDeleteWhenDeletingSource() {
        Quote quote = new Quote("Test quote", book);
        quoteRepository.save(quote);

        assertEquals(1, TestUtilTest.countIterable(bookRepository.findAll()));
        assertEquals(1, TestUtilTest.countIterable(quoteRepository.findAll()));

        bookRepository.delete(book);
        assertEquals(0, TestUtilTest.countIterable(quoteRepository.findAll()));
    }
}