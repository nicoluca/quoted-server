package org.nico.quotedserver.service;

import org.nico.quotedserver.domain.Article;
import org.nico.quotedserver.domain.Quote;
import org.nico.quotedserver.repository.ArticleRepository;
import org.nico.quotedserver.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class ArticleService implements Save<Article>, Update<Article>, Delete<Article> {

    private final ArticleRepository articleRepository;

    private final QuoteRepository quoteRepository;
    private final Logger logger = Logger.getLogger(ArticleService.class.getName());

    @Autowired
    public ArticleService(ArticleRepository articleRepository, QuoteRepository quoteRepository) {
        this.articleRepository = articleRepository;
        this.quoteRepository = quoteRepository;
    }

    public Article save(Article article) {
        Article savedArticle = articleRepository.save(article);
        logger.info("Saved article: " + savedArticle);
        return savedArticle;
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
        logger.info("Looking for: " + article);
        Optional<Article> optionalArticle = articleRepository.findById(article.getId());

        if (optionalArticle.isEmpty())
            return;

        logger.info("Found: " + optionalArticle.get());

        logger.info("Deleting quotes for article: " + article);
        List<Quote> quotes = quoteRepository.findBySourceId(article.getId());
        quoteRepository.deleteAll(quotes);

        logger.info("Deleting article: " + article);
        articleRepository.delete(article);
    }
}
