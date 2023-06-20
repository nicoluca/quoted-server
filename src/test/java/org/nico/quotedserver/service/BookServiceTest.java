package org.nico.quotedserver.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nico.quotedserver.domain.Author;
import org.nico.quotedserver.domain.Book;
import org.nico.quotedserver.repository.AuthorRepository;
import org.nico.quotedserver.repository.BookRepository;
import org.nico.quotedserver.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookServiceTest {

    @Autowired
    BookService bookService;

    @MockBean
    AuthorService authorService;

    @MockBean
    BookRepository bookRepository;

    @MockBean
    AuthorRepository authorRepository;

    @MockBean
    QuoteRepository quoteRepository;

    private Book book;

    @BeforeEach
    void setUp() {
        book = Book.builder()
                .title("Test book")
                .author(
                        new Author("Firstname", "Lastname")
                )
                .build();
        book.setId(1L);
    }

    @Test
    void saveBookWithExistingAuthor() {
        when(authorRepository.findByName(any(), any())).thenReturn(Optional.ofNullable(book.getAuthor()));
        when(bookRepository.save(any())).thenReturn(book);

        assertEquals(book, bookService.save(book));
    }

    @Test
    void saveBookWithNonExistingAuthor() {
        when(authorRepository.findByName(any(), any())).thenReturn(Optional.empty());
        when(authorRepository.save(any())).thenReturn(book.getAuthor());
        when(bookRepository.save(any())).thenReturn(book);

        assertEquals(book, bookService.save(book));
    }

    @Test
    void updateExistingBook() {
        when(bookRepository.findById(any())).thenReturn(Optional.ofNullable(book));
        when(bookRepository.save(any())).thenReturn(book);

        assertTrue(bookService.update(book).isPresent());
        assertEquals(book, bookService.update(book).get());
    }

    @Test
    void updateNonExistingBook() {
        when(bookRepository.findById(any())).thenReturn(Optional.empty());
        assertFalse(bookService.update(book).isPresent());
    }

    @Test
    void testCascadeDeleteBookWithQuotes() {
        when(bookRepository.findById((any()))).thenReturn(Optional.of(book));
        when(quoteRepository.findBySourceId(1L)).thenReturn(new ArrayList<>());
        doNothing().when(quoteRepository).deleteAll(any());

        bookService.delete(book);

        verify(bookRepository).findById(any());
        verify(quoteRepository).findBySourceId(1L);
        verify(quoteRepository).deleteAll(any());
    }
}