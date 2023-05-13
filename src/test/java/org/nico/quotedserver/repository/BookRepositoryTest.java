package org.nico.quotedserver.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nico.quotedserver.domain.Author;
import org.nico.quotedserver.domain.Book;
import org.nico.quotedserver.util.TestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest // Uses an in-memory database by default (H2), which was added as a dependency in pom.xml
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties") // TODO Use in-cache H2 instead
@ComponentScan(basePackages = "org.nico.quotedserver.repository")
@EnableJpaAuditing
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;


    private Author author;

    @BeforeEach
    void setUp() {
        author = new Author("Test", "Test");
        authorRepository.save(author);
    }

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll(bookRepository.findAll());
        authorRepository.deleteAll(authorRepository.findAll());
    }

    @Test
    void create() {
        Book book = new Book("Test book", author);
        bookRepository.save(book);

        assertEquals(book, bookRepository.findById(book.getId()).get());
    }

    @Test
    void readById() {
        assertEquals(Optional.empty(), bookRepository.findById(1L));
    }

    @Test
    void readAll() {
        assertEquals(0, TestUtil.countIterable(bookRepository.findAll()));

        int count = 10;
        IntStream.range(0, count).forEach(i -> {
            Book book = new Book("Test book", author);
            bookRepository.save(book);
        });

        assertEquals(count, TestUtil.countIterable(bookRepository.findAll()));
    }

    @Test
    void update() {
        Book book = new Book("Test book", author);
        bookRepository.save(book);

        String newTitle = "New title";
        book.setTitle(newTitle);
        bookRepository.save(book);

        assertEquals(newTitle, bookRepository.findById(book.getId()).get().getTitle());
    }

    @Test
    void delete() {
        Book book = new Book("Test book", author);
        bookRepository.save(book);

        bookRepository.delete(book);

        assertEquals(Optional.empty(), bookRepository.findById(book.getId()));
        assertEquals(0, TestUtil.countIterable(bookRepository.findAll()));
    }
}