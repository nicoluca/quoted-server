package org.nico.quotedserver.service;

import org.nico.quotedserver.domain.Article;
import org.nico.quotedserver.domain.Quote;
import org.nico.quotedserver.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuoteService {

    private final QuoteRepository quoteRepository;
    private static long lastQuoteId = 0;

    @Autowired
    public QuoteService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    // TODO - test
    // TODO - random is likely not desirable, as we want to cycle through all quotes
    public Quote randomQuote() {
        // Retrieve a random quote from the database, different from the last one
        long numberOfQuotes = quoteRepository.count();
        Quote exceptionQuote = new Quote("No quotes in the database", Article.builder().title("No quotes in the database").build());

        if (numberOfQuotes == 0)
            return exceptionQuote;
        else if (numberOfQuotes == 1)
            return quoteRepository.findAll().iterator().next();

        Quote quote = quoteRepository.findRandomQuote(lastQuoteId).orElse(exceptionQuote);
        lastQuoteId = quote.getId();
        return quote;
    }
}
