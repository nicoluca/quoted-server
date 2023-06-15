package org.nico.quotedserver.rest;

import org.nico.quotedserver.domain.Quote;
import org.nico.quotedserver.domain.Source;
import org.nico.quotedserver.repository.QuoteRepository;
import org.nico.quotedserver.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class SourceRestController {

    private final SourceRepository sourceRepository;
    private final QuoteRepository quoteRepository;

    @Autowired
    public SourceRestController(SourceRepository sourceRepository, QuoteRepository quoteRepository) {
        this.sourceRepository = sourceRepository;
        this.quoteRepository = quoteRepository;
    }

    @GetMapping("/sources")
    public List<Source> getAllSources() {
        return (List<Source>) sourceRepository.findAll();
    }


    @GetMapping("/sources/{sourceId}/quotes")
    public List<Quote> quotesBySource(@PathVariable long sourceId) {
        return quoteRepository.findBySourceId(sourceId);
    }

    @PostMapping("/sources/{sourceId}/quotes")
    public ResponseEntity<Quote> addQuoteToSource(@PathVariable long sourceId,
                                                  @RequestBody Quote quote) {
        if (quote.getSource() != null)
            return ResponseEntity.badRequest().build();

        Optional<Source> source = sourceRepository.findById(sourceId);
        if (source.isEmpty())
            return ResponseEntity.notFound().build();
        quote.setSource(source.get());

        Quote savedQuote = quoteRepository.save(quote);
        return ResponseEntity.ok(savedQuote);
    }
}
