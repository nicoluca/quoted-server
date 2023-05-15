package org.nico.quotedserver.repository;

import org.nico.quotedserver.domain.Article;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {
    // Together with Spring Rest Starter, will automatically create a REST API for us
    // Endpoint defaults to /articles
}
