package org.nico.quotedserver.rest;

import org.nico.quotedserver.domain.Source;
import org.nico.quotedserver.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SourceRestController {

    private final SourceRepository sourceRepository;

    @Autowired
    public SourceRestController(SourceRepository sourceRepository) {
        this.sourceRepository = sourceRepository;
    }

    @GetMapping("/allSources")
    public List<Source> getAllSources() {
        return (List<Source>) sourceRepository.findAll();
    }

}
