package org.nico.quotedserver.service;

import java.util.Optional;

interface Update<T> {
    Optional<T> update(T t);
}
