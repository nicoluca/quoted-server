package org.nico.quotedserver.rest;

import org.nico.quotedserver.service.ArticleService;
import org.nico.quotedserver.util.EntitiyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ArticleRestController {

    private final ArticleService articleService;

    @Autowired
    public ArticleRestController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @DeleteMapping("/articles/{id}")
    public ResponseEntity<String> deleteArticle(@PathVariable Long id) {
        try {
            this.articleService.delete(id);
            return new ResponseEntity<>("Article with id " + id + " has been deleted.", HttpStatus.OK);
        } catch (EntitiyNotFoundException e) {
            return EntitiyNotFoundException.notFoundResponse(e.getMessage());
        }
    }
}
