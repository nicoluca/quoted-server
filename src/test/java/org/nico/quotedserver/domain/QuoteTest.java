package org.nico.quotedserver.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class QuoteTest {
    private Quote quote, quote2, quote3;

    @BeforeEach
    void setUp() {
        quote = new Quote("Hello World!",
                new Book("The Hobbit", new Author("J.R.R.", "Tolkien")));
        quote2 = new Quote("Hello World!",
                new Book("The Hobbit", new Author("J.R.R.", "Tolkien")));
        quote3 = new Quote("Hello World!", new Article("Google", "https://www.google.com"));
    }

    @Test
    void getText() {
        assert quote.getText().equals("Hello World!");
        assert quote2.getText().equals("Hello World!");
        assert quote3.getText().equals("Hello World!");
    }

    @Test
    void getSource() {
        assert quote.getSource().equals(quote2.getSource());
        assert !quote.getSource().equals(quote3.getSource());
    }

    @Test
    void testEquals() {
        assertEquals(quote, quote2);
        assertNotEquals(quote, quote3);
    }
}