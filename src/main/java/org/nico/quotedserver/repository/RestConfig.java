package org.nico.quotedserver.repository;

import org.nico.quotedserver.domain.Article;
import org.nico.quotedserver.domain.Author;
import org.nico.quotedserver.domain.Book;
import org.nico.quotedserver.domain.Quote;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Component
public class RestConfig implements RepositoryRestConfigurer {

    @Value("${custom.config.expose-id}")
    private boolean exposeId;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        if (exposeId) {
            config.exposeIdsFor(Book.class);
            config.exposeIdsFor(Author.class);
            config.exposeIdsFor(Quote.class);
            config.exposeIdsFor(Article.class);
        }
    }
}
