package org.nico.quotedserver.rest;

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

    @GetMapping("/quotes/random")
    public Quote randomQuote() {
        // Retrieve a random quote from the database, different from the last one
        return quoteService.randomQuote();
    }


    @GetMapping("/quotesByString")
    public List<Quote> quotesByString(@RequestParam(name = "search") String searchString) {
        return quoteRepository.findByTextContaining(searchString);
    }

    @PostMapping(path = "/quotes",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Quote> addQuote(@RequestBody Quote quote) {
        logger.info("Received new quote: " + quote);
        Optional<Quote> savedQuote = quoteService.save(quote);
        return savedQuote.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping(path = "/quotes/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Quote> updateQuote(@RequestBody Quote quote, @PathVariable long id) {
        logger.info("Received updated quote: " + quote);
        quote.setId(id);
        Optional<Quote> savedQuote = quoteService.update(quote);
        return savedQuote.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
