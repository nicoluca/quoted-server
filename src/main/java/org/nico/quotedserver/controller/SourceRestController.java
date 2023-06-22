package org.nico.quotedserver.controller;

import org.nico.quotedserver.domain.Article;
import org.nico.quotedserver.domain.Quote;
import org.nico.quotedserver.domain.Source;
import org.nico.quotedserver.repository.QuoteRepository;
import org.nico.quotedserver.repository.SourceRepository;
import org.nico.quotedserver.service.ArticleService;
import org.nico.quotedserver.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
public class SourceRestController {

    private final SourceRepository sourceRepository;
    private final QuoteRepository quoteRepository;

    private final ArticleService articleService;

    private final BookService bookService;

    private final Logger logger = Logger.getLogger(QuoteRestController.class.getName());

    @Autowired
    public SourceRestController(SourceRepository sourceRepository, QuoteRepository quoteRepository, ArticleService articleService, BookService bookService) {
        this.sourceRepository = sourceRepository;
        this.quoteRepository = quoteRepository;
        this.articleService = articleService;
        this.bookService = bookService;
    }

    @GetMapping("/sources")
    public List<Source> getAllSources() {
        logger.info("Received request for all sources");
        return (List<Source>) sourceRepository.findAll();
    }


    @GetMapping("/sources/{sourceId}/quotes")
    public List<Quote> quotesBySource(@PathVariable long sourceId) {
        logger.info("Received request for quotes from source with id: " + sourceId);
        return quoteRepository.findBySourceId(sourceId);
    }

    @PostMapping("/sources/{sourceId}/quotes")
    public ResponseEntity<Quote> addQuoteToSource(@PathVariable long sourceId,
                                                  @RequestBody Quote quote) {
        logger.info("Received request to add quote: " + quote + " to source with id: " + sourceId);

        if (quote.getSource() != null)
            return ResponseEntity.badRequest().build();

        Optional<Source> source = sourceRepository.findById(sourceId);
        if (source.isEmpty())
            return ResponseEntity.notFound().build();
        quote.setSource(source.get());

        Quote savedQuote = quoteRepository.save(quote);
        return ResponseEntity.ok(savedQuote);
    }

    @DeleteMapping("/sources/{sourceId}")
    public ResponseEntity<Long> deleteSource(@PathVariable long sourceId) {
        logger.info("Received request to delete source with id: " + sourceId);

        Optional<Source> source = sourceRepository.findById(sourceId);
        if (source.isEmpty())
            return ResponseEntity.notFound().build();

        if (source.get() instanceof Article article)
            articleService.delete(article);
        else if (source.get() instanceof org.nico.quotedserver.domain.Book book)
            bookService.delete(book);
        else
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(sourceId);
    }
}
