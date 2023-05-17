package org.nico.quotedserver.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nico.quotedserver.domain.Article;
import org.nico.quotedserver.util.TestUtilTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest // Uses an in-memory database by default (H2), which was added as a dependency in pom.xml
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties") // TODO Use in-cache H2 instead
@ComponentScan(basePackages = "org.nico.quotedserver.repository")
@EnableJpaAuditing
class ArticleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager; // TODO Is this necessary?

    @Autowired // Variable injection is not recommended, but it's fine for testing?
    private ArticleRepository articleRepository;
    private Article article;

    @BeforeEach
    void setUp() {
        article = Article.builder()
                .title("Test article")
                .url("Test article")
                .build();
    }

    @AfterEach
    void tearDown() {
        articleRepository.deleteAll(articleRepository.findAll());
    }

    @Test
    @DisplayName("Test a transaction using the TestEntityManager")
    void testTransaction() {
        entityManager.persist(article);
        assertEquals(article, entityManager.find(Article.class, article.getId()));
    }

    @Test
    void create() {
        articleRepository.save(article);

        assertEquals(article, articleRepository.findById(article.getId()).get());
    }

    @Test
    void readById() {
        assertEquals(Optional.empty(), articleRepository.findById(1L));
    }

    @Test
    void readAll() {
        assertEquals(0, TestUtilTest.countIterable(articleRepository.findAll()));

        int count = 10;
        for (int i = 0; i < count; i++) {
            article = new Article("Test article", "Test article");
            articleRepository.save(article);
        }

        assertEquals(count, TestUtilTest.countIterable(articleRepository.findAll()));
    }

    @Test
    void update() {
        articleRepository.save(article);

        String newTitle = "New title";
        article.setTitle(newTitle);
        articleRepository.save(article);

        assertEquals(newTitle, articleRepository.findById(article.getId()).get().getTitle());
    }

    @Test
    void delete() {
        articleRepository.save(article);

        articleRepository.delete(article);

        assertEquals(Optional.empty(), articleRepository.findById(article.getId()));
    }

    @Test
    void testLastVisited() {
        articleRepository.save(article);

        assertEquals(article.getLastVisited(), articleRepository.findById(article.getId()).get().getLastVisited());

        LocalDate today = LocalDate.now();
        LocalDate lastVisited = article.getLastVisited().toLocalDateTime().toLocalDate();
        assertEquals(today, lastVisited);
    }
}