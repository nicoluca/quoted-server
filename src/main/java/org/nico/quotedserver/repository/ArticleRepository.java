package org.nico.quotedserver.repository;

import org.nico.quotedserver.domain.Article;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {
    // Together with Spring Rest Starter, will automatically create a REST API for us
    // Endpoint defaults to /articles

    @Query("SELECT a FROM Article a " +
            "WHERE UPPER(a.url) LIKE CONCAT('%', UPPER(?1), '%')")
    Optional<Article> findByUrl(String url);
}
