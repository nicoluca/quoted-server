package org.nico.quotedserver.rest;

import org.nico.quotedserver.domain.Article;
import org.nico.quotedserver.domain.Quote;
import org.nico.quotedserver.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QuoteRestController {

    private final QuoteRepository quoteRepository;
    private long lastQuoteId = 0;

    @Autowired
    public QuoteRestController(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    @GetMapping("/randomQuote")
    public Quote randomQuote() {
        // Retrieve a random quote from the database, different from the last one
        Iterable<Quote> quotes = quoteRepository.findAll();

        if (!quotes.iterator().hasNext())
            return new Quote("No quotes found", new Article("No articles found", "-"));

        if (quotes.spliterator().getExactSizeIfKnown() == 1)
            return quotes.iterator().next();

        Quote randomQuote = quotes.iterator().next();

        while (randomQuote.getId() == lastQuoteId)
            randomQuote = quotes.iterator().next();

        lastQuoteId = randomQuote.getId();
        return randomQuote;
    }

    @GetMapping("/allQuotes")
    public List<Quote> allQuotes() {
        // Retrieve all quotes from the database
        return (List<Quote>) quoteRepository.findAll();
    }
}
