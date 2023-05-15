package org.nico.quotedserver.repository;

import org.nico.quotedserver.domain.Author;
import org.springframework.data.repository.CrudRepository;

import java.lang.reflect.Type;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    // Together with Spring Rest Starter, will automatically create a REST API for us
    // Endpoint defaults to /authors
}
