package org.nico.quotedserver.repository;

import org.nico.quotedserver.domain.Article;
import org.springframework.data.rest.core.config.Projection;

import java.sql.Timestamp;

@Projection(name = "article", types = { Article.class })
public interface ArticleProjection {
    // Using this projection, author will be embedded in book
    long getId();
    String getTitle();
    String getUrl();
    Timestamp getLastVisited();
}
