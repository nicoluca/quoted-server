package org.nico.quotedserver.rest;

import org.nico.quotedserver.domain.Author;
import org.nico.quotedserver.domain.Book;
import org.nico.quotedserver.repository.AuthorRepository;
import org.nico.quotedserver.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.logging.Logger;

@RestController
public class BookRestController {

    Logger logger = Logger.getLogger(BookRestController.class.getName());
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public BookRestController(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @PostMapping(path = "/newBook",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> newBook(@RequestBody Book book) {
        logger.info("Received new book: " + book);
        Author author = book.getAuthor();
        Optional<Author> optionalAuthor= authorRepository.findByName(author.getFirstName(), author.getLastName());

        if (optionalAuthor.isPresent()) {
            logger.info("Author already exists:" + author);
            author = optionalAuthor.get();
        } else {
            logger.info("Author does not exist, creating new one: " + author);
            author = authorRepository.save(author);
        }

        book.setAuthor(author);

        Book savedBook = bookRepository.save(book);
        logger.info("Saved book: " + savedBook);
        return ResponseEntity.ok(savedBook);
    }

    @PutMapping(path = "/updateBook/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> updateBook(@PathVariable long id, @RequestBody Book book) {
        logger.info("Received update book: " + book);
        Optional<Book> optionalBook = bookRepository.findById(id);

        if (optionalBook.isEmpty())
            return ResponseEntity.notFound().build();

        Book savedBook = optionalBook.get();
        savedBook.setTitle(book.getTitle());
        savedBook.setIsbn(book.getIsbn());
        savedBook.setCoverPath(book.getCoverPath());

        Optional<Author> savedAuthor
                = authorRepository.findByName(book.getAuthor().getFirstName(), book.getAuthor().getLastName());
        Author author;
        if (savedAuthor.isEmpty())
            author = authorRepository.save(book.getAuthor());
        else
            author = savedAuthor.get();

        savedBook.setAuthor(author);
        savedBook = bookRepository.save(savedBook);
        logger.info("Saved book: " + savedBook);
        return ResponseEntity.ok(savedBook);
    }
}
