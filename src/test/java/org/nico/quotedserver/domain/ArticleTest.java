package org.nico.quotedserver.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArticleTest {

    @Test
    @DisplayName("Test if the constructor sets the title and url")
    void testEquals() {
        Article article1 = new Article("google", "https://www.google.com");
        Article article2 = new Article("Google Search Engine","https://www.Google.com");
        Article article3 = new Article("google","https://www.google.com/");
        Article article4 = Article.builder()
                .title("google")
                .url("https://www.google.com")
                .build();

        assertEquals(article1, article2);
        assertEquals(article1, article3);
        assertEquals(article3, article4);
    }

    @Test
    @DisplayName("Test if the date is set to today when the article is created")
    void testGetLastVisited() {
        Article article = new Article();
        LocalDate today = LocalDate.now();
        LocalDate articleDate = article.getLastVisited().toLocalDateTime().toLocalDate();
        assertEquals(today, articleDate);
    }

    @Test
    void testToString() {
        Article article = Article.builder().url("test.de").build();
        String result = article.toString();
        assertEquals("test.de", result);

        Article article2 = Article.builder().url("test.de").title("test").build();
        String result2 = article2.toString();
        assertEquals("test (test.de)", result2);
    }

    @Test
    void originToString() {
        Article article = Article.builder().url("test.de").build();
        assertEquals("test.de", article.originToString());
    }
}