package org.nico.quotedserver.rest;

import org.nico.quotedserver.domain.Author;
import org.nico.quotedserver.domain.Book;
import org.nico.quotedserver.repository.AuthorRepository;
import org.nico.quotedserver.repository.BookRepository;
import org.nico.quotedserver.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.logging.Logger;

@RestController
public class BookRestController {

    Logger logger = Logger.getLogger(BookRestController.class.getName());
    private final BookService bookService;

    @Autowired
    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping(path = "/newBook",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> newBook(@RequestBody Book book) {
        logger.info("Received new book: " + book);
        Book savedBook = bookService.save(book);
        return ResponseEntity.ok(savedBook);
    }

    @PutMapping(path = "/updateBook/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> updateBook(@PathVariable long id, @RequestBody Book book) {
        logger.info("Received update book: " + book);
        Optional<Book> updatedBook = bookService.update(book);
        return updatedBook.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
