package org.nico.quotedserver.controller;

import org.nico.quotedserver.domain.Quote;
import org.nico.quotedserver.repository.QuoteRepository;
import org.nico.quotedserver.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/quotes")
    public List<Quote> allQuotes() {
        // Retrieve all quotes from the database
        return (List<Quote>) quoteRepository.findAll();
    }

    @GetMapping("/quotes/search")
    public List<Quote> quotesByString(@RequestParam(name = "string") String searchString) {
        return quoteRepository.findByTextContaining(searchString);
    }

    @PutMapping(path = "/quotes/{quoteId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Quote> updateQuote(@RequestBody Quote quote,
                                             @PathVariable long quoteId) {
        logger.info("Received updated quote: " + quote);
        quote.setId(quoteId);
        Optional<Quote> savedQuote = quoteService.update(quote);
        return savedQuote.map(ResponseEntity::ok).orElseGet(()
                -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping(path = "/quotes/{quoteId}")
    public ResponseEntity<Long> deleteQuote(@PathVariable long quoteId) {
        logger.info("Received request to delete quote with id: " + quoteId);
        Optional<Quote> quote = quoteRepository.findById(quoteId);
        if (quote.isEmpty())
            return ResponseEntity.notFound().build();

        quoteRepository.delete(quote.get());
        return ResponseEntity.ok(quoteId);
    }
}
