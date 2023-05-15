package org.nico.quotedserver.repository;

import org.nico.quotedserver.domain.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
    // Together with Spring Rest Starter, will automatically create a REST API for us
    // Endpoint defaults to /books
}
