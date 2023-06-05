package org.nico.quotedserver.rest;

import org.nico.quotedserver.domain.Author;
import org.nico.quotedserver.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthorRestController {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorRestController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @GetMapping("/allAuthors")
    public List<Author> getAllAuthors() {
        return (List<Author>) authorRepository.findAll();
    }
}
