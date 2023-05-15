package org.nico.quotedserver.service;

import org.nico.quotedserver.domain.Article;
import org.nico.quotedserver.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArticleServiceImplementation implements ArticleService {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceImplementation(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }
    @Override
    public void deleteArticle(Long id) throws IllegalArgumentException {
        Optional<Article> optionalArticle = this.articleRepository.findById(id);

        if (optionalArticle.isPresent())
            this.articleRepository.delete(optionalArticle.get());
        else
            throw new IllegalArgumentException("Article with id " + id + " not found.");
    }
}
