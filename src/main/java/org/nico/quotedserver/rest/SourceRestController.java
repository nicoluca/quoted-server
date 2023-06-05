package org.nico.quotedserver.rest;

import org.nico.quotedserver.domain.Quote;
import org.nico.quotedserver.domain.Source;
import org.nico.quotedserver.repository.QuoteRepository;
import org.nico.quotedserver.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        // Retrieve all quotes from the database
        return quoteRepository.findBySourceId(sourceId);
    }

}
