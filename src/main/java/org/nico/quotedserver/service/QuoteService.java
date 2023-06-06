package org.nico.quotedserver.service;

import org.nico.quotedserver.domain.Article;
import org.nico.quotedserver.domain.Book;
import org.nico.quotedserver.domain.Quote;
import org.nico.quotedserver.domain.Source;
import org.nico.quotedserver.repository.ArticleRepository;
import org.nico.quotedserver.repository.BookRepository;
import org.nico.quotedserver.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class QuoteService implements ServiceInterface<Quote> {

    private final QuoteRepository quoteRepository;
    private final BookRepository bookRepository;
    private final ArticleRepository articleRepository;
    private static long lastQuoteId = 0L;
    private final Logger logger = Logger.getLogger(QuoteService.class.getName());

    @Autowired
    public QuoteService(QuoteRepository quoteRepository, BookRepository bookRepository, ArticleRepository articleRepository) {
        this.quoteRepository = quoteRepository;
        this.bookRepository = bookRepository;
        this.articleRepository = articleRepository;
    }

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

    public Optional<Quote> save(Quote quote) {
        Source source = quote.getSource();
        if (source instanceof Article) {
            Article article = resolveArticle(source);
            quote.setSource(article);
            logger.info("Article found and timestamp updated: " + article);
        } else if (source instanceof Book && bookRepository.findById(source.getId()).isPresent()) {
            org.nico.quotedserver.domain.Book book = bookRepository.findById(source.getId()).get();
            quote.setSource(book);
            logger.info("Book found: " + book);
        } else
            return Optional.empty();

        Quote savedQuote = quoteRepository.save(quote);
        return Optional.of(savedQuote);
    }

    private Article resolveArticle(Source source) {
        Article article = (Article) source;
        if (articleRepository.findById(source.getId()).isPresent())
            article = articleRepository.findById(source.getId()).get();
        else if (articleRepository.findByUrl(article.getTitle()).isPresent()) // Source only passes the title, not the URL
            article = articleRepository.findByUrl(article.getTitle()).get();
        else
            article = new Article(source.getTitle(), source.getTitle());

        article.setLastVisited(Timestamp.from(Instant.now()));
        article = articleRepository.save(article);
        return article;
    }


    public Optional<Quote> update(Quote quote) {
        Optional<Quote> optionalQuoteToUpdate= quoteRepository.findById(quote.getId());
        if (optionalQuoteToUpdate.isEmpty())
            return Optional.empty();
        Quote quoteToUpdate = optionalQuoteToUpdate.get();

        quoteToUpdate.setText(quote.getText());
        quoteToUpdate.setLastEdited(Timestamp.from(Instant.now()));

        Source source = quote.getSource();
        if (source instanceof Article) {
            Article article = resolveArticle(source);
            quoteToUpdate.setSource(article);
            logger.info("Article found and timestamp updated: " + article);
        } else if (source instanceof Book && bookRepository.findById(source.getId()).isPresent()) {
            Book book = bookRepository.findById(source.getId()).get();
            quoteToUpdate.setSource(book);
            logger.info("Book found: " + book);
        } else
            return Optional.empty();
        Quote savedQuote = quoteRepository.save(quoteToUpdate);
        return Optional.of(savedQuote);
    }
}
