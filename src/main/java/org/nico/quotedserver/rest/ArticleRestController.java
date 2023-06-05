package org.nico.quotedserver.rest;

import org.nico.quotedserver.domain.Article;
import org.nico.quotedserver.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.logging.Logger;

@RestController
public class ArticleRestController {

    private final Logger logger = Logger.getLogger(ArticleRestController.class.getName());
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleRestController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @PutMapping(path = "/updateArticle/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Article> updateArticle(@PathVariable long id, @RequestBody Article article) {
        logger.info("Received article to update: " + article);
        Optional<Article> optionalArticle = articleRepository.findById(id);

        if (optionalArticle.isEmpty())
            return ResponseEntity.notFound().build();

        Article articleToUpdate = optionalArticle.get();
        articleToUpdate.setTitle(article.getTitle());
        articleToUpdate.setUrl(article.getUrl());
        articleToUpdate.setLastVisited(article.getLastVisited());

        Article savedArticle = articleRepository.save(articleToUpdate);
        logger.info("Saved article: " + savedArticle);
        return ResponseEntity.ok(savedArticle);
    }
}
