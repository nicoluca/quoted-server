package org.nico.quotedserver.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AuthorTest {

    @Test
    void testEquals() {
        Author author1 = new Author("J.R.R.", "Tolkien");
        Author author2 = new Author("J.r.r.", "tolkien");
        Author author3 = new Author("J.R.R.", "T");

        assertEquals(author1, author2);
        assertNotEquals(author1, author3);
    }
}