package org.nico.quotedserver.service;

import org.nico.quotedserver.domain.Author;
import org.nico.quotedserver.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorService implements Save<Author>, Delete<Author> {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author save(Author author) {
        Optional<Author> savedAuthor
                = authorRepository.findByName(author.getFirstName(), author.getLastName());
        return savedAuthor.orElseGet(() -> authorRepository.save(author));
    }

    public void delete(Author author) {
        authorRepository.delete(author);
    }
}
