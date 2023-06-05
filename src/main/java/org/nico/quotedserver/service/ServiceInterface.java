package org.nico.quotedserver.service;

import java.util.Optional;

interface ServiceInterface<T> {
    Optional<T> update(T t);

}
