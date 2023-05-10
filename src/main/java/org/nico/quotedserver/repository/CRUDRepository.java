package org.nico.quotedserver.repository;

import java.util.List;
import java.util.Optional;

public interface CRUDRepository<T> {
    void create(T t);
    Optional<T> readById(long id); // TODO unused - needed?
    List<T> readAll();
    void update(T t);
    void delete(T t);
}
