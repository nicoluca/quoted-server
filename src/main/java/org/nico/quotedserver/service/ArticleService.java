package org.nico.quotedserver.service;

import org.nico.quotedserver.domain.Article;
import org.nico.quotedserver.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class ArticleService implements ServiceInterface<Article> {

    private final ArticleRepository articleRepository;
    private final Logger logger = Logger.getLogger(ArticleService.class.getName());

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
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
}
