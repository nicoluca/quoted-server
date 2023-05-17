package org.nico.quotedserver.repository;

import org.nico.quotedserver.domain.Quote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "quotes")
public interface QuoteRepository extends CrudRepository<Quote, Long> {
    // Together with Spring Rest Starter, will automatically create a REST API for us
    // Endpoint defaults to /quotes
}
