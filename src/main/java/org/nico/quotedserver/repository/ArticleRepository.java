package org.nico.quotedserver.repository;

import org.nico.quotedserver.domain.Article;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {
    @Query("SELECT a FROM Article a " +
            "WHERE UPPER(a.url) LIKE CONCAT('%', UPPER(?1), '%')") // TODO extract hardcoded SQL and check if Spring does not offer it built-in
    Optional<Article> findByUrl(String url);
}
