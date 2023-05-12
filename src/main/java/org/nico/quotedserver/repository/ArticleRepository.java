package org.nico.quotedserver.repository;

import org.nico.quotedserver.domain.Article;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {
    // TODO Do we need JPARepository or CrudRepository is enough?
}
