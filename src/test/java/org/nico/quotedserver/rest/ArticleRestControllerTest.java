package org.nico.quotedserver.rest;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.nico.quotedserver.TestUtil;
import org.nico.quotedserver.domain.Article;
import org.nico.quotedserver.repository.ArticleRepository;
import org.nico.quotedserver.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleRestController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class ArticleRestControllerTest {

    @Value("${spring.security.user.name}")
    private String username;

    @Value("${spring.security.user.password}")
    private String password;

    @Value("${base.url}")
    private String baseUrl;

    @MockBean
    ArticleService articleService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenArticleIdThatDoesNotExist_whenUpdated_Then404() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put(baseUrl + "/articles/1")
                .contentType("application/json")
                .content(TestUtil.resourceToString("/json/article.json"))
                        .with(csrf())
                                .with(user(username).password(password))
                                        .accept("application/json")
                )
                    .andExpect(status().isNotFound());
    }
}