package org.nico.quotedserver.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nico.quotedserver.domain.Article;
import org.nico.quotedserver.repository.ArticleRepository;
import org.nico.quotedserver.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ArticleServiceTest {

    @MockBean
    ArticleRepository articleRepository;

    @MockBean
    QuoteRepository quoteRepository;

    @Autowired
    ArticleService articleService;

    private Article article;

    @BeforeEach
    void setUp() {
        article = Article.builder()
                .title("Test article")
                .url("https://www.test.com")
                .build();
        article.setId(1L);
    }

    @Test
    void updateNonExistingId() {
        when(articleRepository.findById(any())).thenReturn(Optional.empty());
        assertFalse(articleService.update(article).isPresent());
    }

    @Test
    void updateExistingId() {
        when(articleRepository.findById(any())).thenReturn(Optional.ofNullable(article));
        when(articleRepository.save(any())).thenReturn(article);

        assertTrue(articleService.update(article).isPresent());
        assertEquals(article, articleService.update(article).get());
    }

    @Test
    void testCascadeDeleteArticleWithQuotes() {
        when(articleRepository.findById((any()))).thenReturn(Optional.of(article));
        when(quoteRepository.findBySourceId(1L)).thenReturn(new ArrayList<>());
        doNothing().when(quoteRepository).deleteAll(any());

        articleService.delete(article);

        verify(articleRepository).findById(any());
        verify(quoteRepository).findBySourceId(1L);
        verify(quoteRepository).deleteAll(any());
    }
}