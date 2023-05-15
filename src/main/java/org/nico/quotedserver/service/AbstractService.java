package org.nico.quotedserver.service;

import org.nico.quotedserver.domain.Source;
import org.nico.quotedserver.util.EntitiyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@org.springframework.stereotype.Service
abstract class AbstractService<T extends Source> implements Service<T> {

    private final CrudRepository<T, Long> repository;
    private final Class<T> type;

    @Autowired
    public AbstractService(CrudRepository<T, Long> repository, Class<T> type) {
        this.repository = repository;
        this.type = type;
    }
    @Override
    public void delete(Long id) {
        Optional<T> optionalEntity = this.repository.findById(id);

        if (optionalEntity.isPresent())
            this.repository.delete(optionalEntity.get());
        else {
            throw new EntitiyNotFoundException(type.getSimpleName() + " with id " + id + " not found.");
        }
    }
}
