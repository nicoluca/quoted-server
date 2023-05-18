package org.nico.quotedserver.repository;

import org.nico.quotedserver.domain.Author;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "author", types = { Author.class })
public interface AuthorProjection {
    // Using this projection, author will be embedded in book
    long getId();
    String getFirstName();
    String getLastName();
}
