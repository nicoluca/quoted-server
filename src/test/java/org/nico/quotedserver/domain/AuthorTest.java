package org.nico.quotedserver.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthorTest {

    @Test
    void testEquals() {
        Author author1 = new Author("J.R.R.", "Tolkien");
        Author author2 = new Author("J.r.r.", "tolkien");
        Author author3 = new Author("J.R.R.", "T");

        assertTrue(author1.equals(author2));
        assertFalse(author1.equals(author3));
    }
}