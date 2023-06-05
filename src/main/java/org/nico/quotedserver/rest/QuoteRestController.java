package org.nico.quotedserver.rest;

import org.nico.quotedserver.domain.Article;
import org.nico.quotedserver.domain.Book;
import org.nico.quotedserver.domain.Quote;
import org.nico.quotedserver.domain.Source;
import org.nico.quotedserver.repository.ArticleRepository;
import org.nico.quotedserver.repository.BookRepository;
import org.nico.quotedserver.repository.QuoteRepository;
import org.nico.quotedserver.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
public class QuoteRestController {

    private final QuoteRepository quoteRepository;
    private final QuoteService quoteService;
    private final Logger logger = Logger.getLogger(QuoteRestController.class.getName());

    @Autowired
    public QuoteRestController(QuoteRepository quoteRepository, QuoteService quoteService) {
        this.quoteRepository = quoteRepository;
        this.quoteService = quoteService;
    }

    @GetMapping("/randomQuote")
    public Quote randomQuote() {
        // Retrieve a random quote from the database, different from the last one
        return quoteService.randomQuote();
    }

    @GetMapping("/allQuotes")
    public List<Quote> allQuotes() {
        // Retrieve all quotes from the database
        return (List<Quote>) quoteRepository.findAll();
    }

    @GetMapping("/quotesBySource/{sourceId}")
    public List<Quote> quotesBySource(@PathVariable long sourceId) {
        // Retrieve all quotes from the database
        return quoteRepository.findBySourceId(sourceId);
    }

    @GetMapping("/quotesByString/{searchString}")
    public List<Quote> quotesByString(@PathVariable String searchString) {
        // Retrieve all quotes from the database
        return quoteRepository.findByTextContaining(searchString);
    }

    @PostMapping(path = "/addQuote",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Quote> addQuote(@RequestBody Quote quote) {
        logger.info("Received new quote: " + quote);
        Optional<Quote> savedQuote = quoteService.save(quote);
        return savedQuote.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping(path = "/updateQuote/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Quote> updateQuote(@RequestBody Quote quote, @PathVariable long id) {
        logger.info("Received updated quote: " + quote);
        quote.setId(id);
        Optional<Quote> savedQuote = quoteService.update(quote);
        return savedQuote.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
