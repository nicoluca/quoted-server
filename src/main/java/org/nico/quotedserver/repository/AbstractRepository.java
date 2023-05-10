package org.nico.quotedserver.repository;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
abstract class AbstractRepository<T> implements CRUDRepository<T> {
    private final Class<T> type;
    private final EntityManager entityManager;

    public AbstractRepository(Class<T> type, EntityManager entityManager) {
        this.type = type;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void create(T t) {
        this.entityManager.persist(t);
    }

    @Override
    public Optional<T> readById(long id) {
        T t = entityManager.find(type, id);
        return Optional.ofNullable(t);
    }

    @Override
    public List<T> readAll() {
        return entityManager.createQuery("from " + type.getName(), type).getResultList();
    }

    @Override
    @Transactional
    public void update(T t) {
        entityManager.merge(t);
    }

    @Override
    public void delete(T t) {
        entityManager.remove(t);
    }
}