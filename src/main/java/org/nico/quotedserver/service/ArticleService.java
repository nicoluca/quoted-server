package org.nico.quotedserver.service;

import org.nico.quotedserver.domain.Article;
import org.nico.quotedserver.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleService extends AbstractService<Article> {

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        super(articleRepository, Article.class);
    }
}
