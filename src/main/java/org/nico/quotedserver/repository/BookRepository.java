package org.nico.quotedserver.repository;

import org.nico.quotedserver.domain.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
}
