package org.nico.quotedserver.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.nico.quotedserver.domain.Article;
import org.nico.quotedserver.domain.Quote;
import org.nico.quotedserver.util.TestUtilTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties") // TODO Use in-cache H2 instead
@ComponentScan(basePackages = "org.nico.quotedserver.repository")
@EnableJpaAuditing
public class SourceRepositoryTest {

    @Autowired
    private SourceRepository sourceRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private QuoteRepository quoteRepository;

    @AfterEach
    void tearDown() {
        quoteRepository.deleteAll(quoteRepository.findAll());
        articleRepository.deleteAll(articleRepository.findAll());
        sourceRepository.deleteAll(sourceRepository.findAll());
    }

    @Test
    @Disabled("Cascade delete does not work for repositories. Services to be used instead for now.")
    void testCascadingDeleteWhenDeletingSource() {
        Article article = new Article("Test Article", " Test Url");
        Quote quote = new Quote("Test quote", article);
        articleRepository.save(article);
        quoteRepository.save(quote);

        assertEquals(1, TestUtilTest.countIterable(sourceRepository.findAll()));
        assertEquals(1, TestUtilTest.countIterable(articleRepository.findAll()));
        assertEquals(1, TestUtilTest.countIterable(quoteRepository.findAll()));

        sourceRepository.delete(article);
        assertEquals(0, TestUtilTest.countIterable(sourceRepository.findAll()));
        assertEquals(0, TestUtilTest.countIterable(articleRepository.findAll()));
        assertEquals(0, TestUtilTest.countIterable(quoteRepository.findAll()));
    }




}
