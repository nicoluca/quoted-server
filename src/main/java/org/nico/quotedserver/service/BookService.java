package org.nico.quotedserver.service;

import org.nico.quotedserver.domain.Author;
import org.nico.quotedserver.domain.Book;
import org.nico.quotedserver.domain.Quote;
import org.nico.quotedserver.repository.AuthorRepository;
import org.nico.quotedserver.repository.BookRepository;
import org.nico.quotedserver.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class BookService implements Save<Book>, Update<Book>, Delete<Book> {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final AuthorService authorService;
    private final Logger logger = Logger.getLogger(BookService.class.getName());

    private final QuoteRepository quoteRepository;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, AuthorService authorService, QuoteRepository quoteRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.authorService = authorService;
        this.quoteRepository = quoteRepository;
    }

    public Book save(Book book) {
        Author author = book.getAuthor();
        Optional<Author> optionalAuthor= authorRepository.findByName(author.getFirstName(), author.getLastName());

        if (optionalAuthor.isPresent()) {
            logger.info("Author already exists:" + author);
            author = optionalAuthor.get();
        } else {
            logger.info("Author does not exist, creating new one: " + author);
            author = authorRepository.save(author); // TODO Save author names in camel case
        }

        book.setAuthor(author);
        Book savedBook = bookRepository.save(book);
        logger.info("Saved book: " + savedBook);
        return savedBook;
    }

    public Optional<Book> update(Book book) {
        Optional<Book> optionalBook = bookRepository.findById(book.getId());

        if (optionalBook.isEmpty())
            return Optional.empty();

        Book savedBook = optionalBook.get();
        savedBook.setTitle(book.getTitle());
        savedBook.setIsbn(book.getIsbn());
        savedBook.setCoverPath(book.getCoverPath());

        logger.info("Saving author: " + book.getAuthor());
        Author author = authorService.save(book.getAuthor());

        logger.info("Deleting authors with no books");
        authorRepository.deleteAuthorsWithNoBooks();

        savedBook.setAuthor(author);
        savedBook = bookRepository.save(savedBook);

        logger.info("Saved book: " + savedBook);
        return Optional.of(savedBook);
    }

    public void delete(Book book) {
        logger.info("Looking for: " + book);
        Optional<Book> optionalBook = bookRepository.findById(book.getId());

        if (optionalBook.isEmpty())
            return;

        logger.info("Found: " + optionalBook.get());

        logger.info("Deleting quotes from book: " + book);
        List<Quote> quotes = quoteRepository.findBySourceId(book.getId());
        quoteRepository.deleteAll(quotes);

        logger.info("Deleting book: " + book);
        bookRepository.delete(book);
    }
}
