package org.nico.quotedserver.repository;

import org.nico.quotedserver.domain.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long> {
}
