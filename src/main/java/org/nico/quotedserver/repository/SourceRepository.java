package org.nico.quotedserver.repository;

import org.nico.quotedserver.domain.Source;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "sources")
public interface SourceRepository extends CrudRepository<Source, Long> {
}
