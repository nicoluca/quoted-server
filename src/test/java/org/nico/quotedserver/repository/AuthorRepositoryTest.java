package org.nico.quotedserver.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nico.quotedserver.domain.Author;
import org.nico.quotedserver.util.TestUtilTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest // Uses an in-memory database by default (H2), which was added as a dependency in pom.xml
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties") // TODO Use in-cache H2 instead
@ComponentScan(basePackages = "org.nico.quotedserver.repository")
@EnableJpaAuditing
class AuthorRepositoryTest {

    @Autowired // Variable injection is not recommended, but it's fine for testing?
    private AuthorRepository authorRepository;

    @AfterEach
    void tearDown() {
        authorRepository.deleteAll(authorRepository.findAll());
    }

    @Test
    @DisplayName("Create authors and confirm they are in database")
    void create() {
        Author author = new Author("Neil", "Stephenson");
        authorRepository.save(author);

        Author author2 = new Author("Neil", "Armstrong");
        authorRepository.save(author2);

        // Assert if author is in database
        assertEquals(authorRepository.findById(author.getId()).get(), author);
        assertEquals(2, TestUtilTest.countIterable(authorRepository.findAll()));
    }

    @Test
    @DisplayName("Read author by id")
    void readById() {
        Author author = new Author("Neil", "Stephenson");
        authorRepository.save(author);

        assertEquals(authorRepository.findById(author.getId()).get(), author);
    }

    @Test
    @DisplayName("Read all authors")
    void readAll() {
        Author author = new Author("Neil", "Stephenson");
        authorRepository.save(author);

        Author author2 = new Author("Neil", "Armstrong");
        authorRepository.save(author2);

        assertEquals(2, TestUtilTest.countIterable(authorRepository.findAll()));
    }

    @Test
    @DisplayName("Update author")
    void update() {
        Author author = new Author("Neil", "Stephenson");
        authorRepository.save(author);

        author.setFirstName("Neil deGrasse");
        authorRepository.save(author);

        assertEquals(authorRepository.findById(author.getId()).get(), author);
    }

    @Test
    @DisplayName("Delete author")
    void delete() {
        Author author = new Author("Neil", "Stephenson");
        authorRepository.save(author);

        authorRepository.delete(author);

        assertEquals(0, TestUtilTest.countIterable(authorRepository.findAll()));
    }

    @Test
    @DisplayName("Find author by name")
    void findByName() {
        Author author = new Author("Neil", "Stephenson");
        authorRepository.save(author);

        Author author2 = new Author("Neil", "Armstrong");
        authorRepository.save(author2);

        Optional<Author> authorOptional = authorRepository.findByName("NEIL", "stephenson");
        assertTrue(authorOptional.isPresent());
        assertEquals(authorOptional.get(), author);

        Optional<Author> authorOptional2 = authorRepository.findByName("neil", "Armstrong");
        assertTrue(authorOptional2.isPresent());
        assertEquals(authorOptional2.get(), author2);

        Optional<Author> authorOptional3 = authorRepository.findByName("Neil", "NotPresent");
        assertTrue(authorOptional3.isEmpty());
    }

    @Test
    @DisplayName("Delete authors with no books")
    void deleteAuthorWithNoBooks() {
        Author author = new Author("Neil", "Stephenson");
        authorRepository.save(author);

        authorRepository.deleteAuthorsWithNoBooks();

        assertEquals(0, TestUtilTest.countIterable(authorRepository.findAll()));
    }
}