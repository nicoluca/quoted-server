package org.nico.quotedserver.repository;

import org.nico.quotedserver.domain.Author;
import org.nico.quotedserver.domain.Book;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "fullBooks", types = { Book.class })
public interface BookProjection {
    long getId();
    String getTitle();
    Author getAuthor();
}
