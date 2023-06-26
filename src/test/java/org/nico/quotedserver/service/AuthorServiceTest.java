package org.nico.quotedserver.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nico.quotedserver.domain.Author;
import org.nico.quotedserver.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthorServiceTest {

    @MockBean
    AuthorRepository authorRepository;

    @Autowired
    AuthorService authorService;

    private Author author;

    @BeforeEach
    void setUp() {
        author = new Author("Firstname", "Lastname");
        author.setId(1L);
    }

    @Test
    void saveExistingAuthor() {
        when(authorRepository.findByName(any(), any())).thenReturn(Optional.ofNullable(author));
        assertEquals(author, authorService.save(author));
    }

    @Test
    void saveNonExistingAuthor() {
        when(authorRepository.findByName(any(), any())).thenReturn(Optional.empty());
        when(authorRepository.save(any())).thenReturn(author);
        assertEquals(author, authorService.save(author));
    }
}