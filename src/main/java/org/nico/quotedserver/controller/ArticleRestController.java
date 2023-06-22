package org.nico.quotedserver.controller;

import org.nico.quotedserver.domain.Article;
import org.nico.quotedserver.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.logging.Logger;

@RestController
public class ArticleRestController {

    private final Logger logger = Logger.getLogger(ArticleRestController.class.getName());
    private final ArticleService articleService;

    @Autowired
    public ArticleRestController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping(path = "/articles",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Article> newArticle(@RequestBody Article article) {
        logger.info("Received new article: " + article);
        Article savedArticle = articleService.save(article);
        return ResponseEntity.ok(savedArticle);
    }

    @PutMapping(path = "/articles/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Article> updateArticle(@PathVariable long id, @RequestBody Article article) {
        logger.info("Received article to update: " + article);
        article.setId(id);
        Optional<Article> result = articleService.update(article);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
