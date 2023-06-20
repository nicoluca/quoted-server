package org.nico.quotedserver.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nico.quotedserver.TestUtil;
import org.nico.quotedserver.domain.Author;
import org.nico.quotedserver.domain.Book;
import org.nico.quotedserver.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class BookRestControllerTest {

    @Value("${spring.security.user.name}")
    private String username;

    @Value("${spring.security.user.password}")
    private String password;

    @MockBean
    private BookService bookService;

    @Autowired
    private MockMvc mockMvc;

    private Book book;

    @BeforeEach
    void setup() {
        book = new Book("Test book", new Author("Firstname", "Lastname"));
        book.setId(1L);
    }

    @Test
    void newBook() throws Exception {
        when(bookService.save(any(Book.class))).thenReturn(book);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/books")
                                .with(user(username).password(password))
                                .accept("application/json")
                                .contentType("application/json")
                                .content(TestUtil.resourceToString("/json/book.json"))
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    assertTrue(contentAsString.contains("Test book"));
                    assertTrue(contentAsString.contains("Firstname"));
                    assertTrue(contentAsString.contains("\"id\":1"));
                    assertTrue(contentAsString.contains("book"));
                });

        verify(bookService).save(any(Book.class));
    }

    @Test
    void newBookWithInvalidData_ThenExpectBadRequest() throws Exception {
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/books")
                                .with(user(username).password(password))
                                .accept("application/json")
                                .contentType("application/json")
                                .content("{\"title\":\"Test book\"}")
                                .with(csrf())
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBook() throws Exception {
        when(bookService.update(any(Book.class))).thenReturn(Optional.ofNullable(book));

        this.mockMvc.perform(
                        MockMvcRequestBuilders.put("/books/1")
                                .with(user(username).password(password))
                                .accept("application/json")
                                .contentType("application/json")
                                .content(TestUtil.resourceToString("/json/book.json"))
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    assertTrue(contentAsString.contains("Test book"));
                    assertTrue(contentAsString.contains("Firstname"));
                    assertTrue(contentAsString.contains("\"id\":1"));
                    assertTrue(contentAsString.contains("book"));
                });

        verify(bookService).update(any(Book.class));
    }
}