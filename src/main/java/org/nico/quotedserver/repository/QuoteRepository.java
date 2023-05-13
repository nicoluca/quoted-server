package org.nico.quotedserver.repository;

import org.nico.quotedserver.domain.Quote;
import org.springframework.data.repository.CrudRepository;

public interface QuoteRepository extends CrudRepository<Quote, Long> {
}
