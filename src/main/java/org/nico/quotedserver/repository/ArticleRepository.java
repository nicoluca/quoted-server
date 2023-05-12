package org.nico.quotedserver.repository;

import jakarta.persistence.EntityManager;
import org.nico.quotedserver.domain.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleRepository extends AbstractRepository<Article> { // TODO replace with JPARepository<Article, Integer>
    @Autowired
    public ArticleRepository(EntityManager entityManager) {
        super(Article.class, entityManager);
    }
}
