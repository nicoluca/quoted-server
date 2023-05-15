package org.nico.quotedserver.repository;

import org.springframework.data.repository.CrudRepository;

import org.nico.quotedserver.domain.Source;

@org.springframework.stereotype.Repository
public interface Repository<T extends Source> extends CrudRepository<T, Long> {
}
