package org.nico.quotedserver.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookTest {
    private Book book1, book2;

    @BeforeAll
    void setUp() {
        Author author = new Author("J.R.R.", "Tolkien");
        book1 = new Book("The Hobbit", author);
        book2 = new Book("The Lord of the Rings", author);
    }

    @Test
    void getTitle() {
        assert book1.getTitle().equals("The Hobbit");
        assert book2.getTitle().equals("The Lord of the Rings");
    }

    @Test
    void getAuthor() {
        Author author1 = new Author("j.r.r.", "Tolkien");
        assert book1.getAuthor().equals(author1);
        assert book2.getAuthor().equals(author1);
    }
}