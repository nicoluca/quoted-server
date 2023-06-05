package org.nico.quotedserver.rest;

import org.nico.quotedserver.domain.Article;
import org.nico.quotedserver.domain.Book;
import org.nico.quotedserver.domain.Quote;
import org.nico.quotedserver.domain.Source;
import org.nico.quotedserver.repository.ArticleRepository;
import org.nico.quotedserver.repository.BookRepository;
import org.nico.quotedserver.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;

@RestController
public class QuoteRestController {

    private final QuoteRepository quoteRepository;
    private final ArticleRepository articleRepository;
    private final BookRepository bookRepository;
    private long lastQuoteId = 0;
    private final Logger logger = Logger.getLogger(QuoteRestController.class.getName());

    @Autowired
    public QuoteRestController(QuoteRepository quoteRepository, ArticleRepository articleRepository, BookRepository bookRepository) {
        this.quoteRepository = quoteRepository;
        this.articleRepository = articleRepository;
        this.bookRepository = bookRepository;
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

    @PostMapping(path = "/addQuote",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Quote> addQuote(@RequestBody Quote quote) {
        logger.info("Received new quote: " + quote);
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
            return ResponseEntity.notFound().build();

        Quote savedQuote = quoteRepository.save(quote);
        return ResponseEntity.ok(savedQuote);
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
}
