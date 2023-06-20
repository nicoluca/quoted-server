package org.nico.quotedserver.service;

import org.nico.quotedserver.domain.Article;
import org.nico.quotedserver.domain.Book;
import org.nico.quotedserver.domain.Quote;
import org.nico.quotedserver.repository.ArticleRepository;
import org.nico.quotedserver.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class ArticleService implements ServiceInterface<Article> {

    private final ArticleRepository articleRepository;

    private final QuoteRepository quoteRepository;
    private final Logger logger = Logger.getLogger(ArticleService.class.getName());

    @Autowired
    public ArticleService(ArticleRepository articleRepository, QuoteRepository quoteRepository) {
        this.articleRepository = articleRepository;
        this.quoteRepository = quoteRepository;
    }

    public Optional<Article> update(Article article) {
        Optional<Article> optionalArticle = articleRepository.findById(article.getId());

        if (optionalArticle.isEmpty())
            return Optional.empty();

        Article articleToUpdate = optionalArticle.get();
        articleToUpdate.setTitle(article.getTitle());
        articleToUpdate.setUrl(article.getUrl());
        articleToUpdate.setLastVisited(article.getLastVisited());

        Article savedArticle = articleRepository.save(articleToUpdate);
        logger.info("Saved article: " + savedArticle);
        return Optional.of(savedArticle);
    }

    public void delete(Article article) {
        Optional<Article> optionalArticle = articleRepository.findById(article.getId());

        if (optionalArticle.isEmpty())
            return;

        List<Quote> quotes = quoteRepository.findBySourceId(article.getId());
        quoteRepository.deleteAll(quotes);

        articleRepository.delete(article);
    }
}
