package com.example.spring.fifth.web.controller;


import com.example.spring.fifth.AbstractControllerTest;
import com.example.spring.fifth.web.model.book.BookResponse;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


public class BookControllerTest extends AbstractControllerTest {

    @Test
    public void whenGetAllBooks_thenReturnBooksAndCreateCache() throws Exception {
        assertTrue(redisTemplate.keys("*").isEmpty());
        assertEquals(4, bookRepository.count());

        String expected = objectMapper.writeValueAsString(bookMapper.bookListToBookListResponse(bookService.findAll(FIND_ALL_SETTINGS)));

        String actual = mockMvc.perform(get("/api/book")
                .content(objectMapper.writeValueAsString(FIND_ALL_SETTINGS))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertFalse(redisTemplate.keys("*").isEmpty());

        JsonAssert.assertJsonEquals(expected, actual);
    }

    @Test
    public void whenGetBookByName_thenReturnBookAndCreateCache() throws Exception {
        assertTrue(redisTemplate.keys("*").isEmpty());
        assertEquals(4, bookRepository.count());

        String expected = objectMapper.writeValueAsString(bookMapper.bookToBookResponse(bookService.findByName("book1")));

        String actual = mockMvc.perform(get("/api/book/book1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertFalse(redisTemplate.keys("*").isEmpty());

        JsonAssert.assertJsonEquals(expected, actual);
    }

    @Test
    public void whenGetBookById_thenReturnBookAndCreateCache() throws Exception {
        assertTrue(redisTemplate.keys("*").isEmpty());

        String expected = objectMapper.writeValueAsString(bookMapper.bookToBookResponse(bookService.findById(2L)));

        String actual = mockMvc.perform(get("/api/book/id/2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertFalse(redisTemplate.keys("*").isEmpty());

        JsonAssert.assertJsonEquals(expected, actual);
    }

    @Test
    public void whenCreateBook_thenReturnBookAndEvictCache() throws Exception {
        assertTrue(redisTemplate.keys("*").isEmpty());
        assertEquals(4, bookRepository.count());

        mockMvc.perform(get("/api/book")
                        .content(objectMapper.writeValueAsString(FIND_ALL_SETTINGS))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertFalse(redisTemplate.keys("*").isEmpty());

        BookResponse bookResponse = BookResponse.builder()
                .author(DEFAULT_CREATE_BOOK.getAuthor())
                .name(DEFAULT_CREATE_BOOK.getName())
                .category(DEFAULT_CREATE_BOOK.getCategory())
                .id(5L)
                .build();

        String expected = objectMapper.writeValueAsString(bookResponse);

        String actual = mockMvc.perform(post("/api/book")
                .content(objectMapper.writeValueAsString(DEFAULT_CREATE_BOOK))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(redisTemplate.keys("*").isEmpty());

        assertEquals(5, bookRepository.count());

        JsonAssert.assertJsonEquals(expected, actual);
    }

    @Test
    public void whenUpdateBookById_thenReturnBookAndEvictCache() throws Exception {
        assertTrue(redisTemplate.keys("*").isEmpty());
        assertEquals(4, bookRepository.count());

        mockMvc.perform(get("/api/book")
                .content(objectMapper.writeValueAsString(FIND_ALL_SETTINGS))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertFalse(redisTemplate.keys("*").isEmpty());

        BookResponse bookResponse = BookResponse.builder()
                .author(DEFAULT_CREATE_BOOK.getAuthor())
                .name(DEFAULT_CREATE_BOOK.getName())
                .category(DEFAULT_CREATE_BOOK.getCategory())
                .id(UPDATE_ID)
                .build();

        String expected = objectMapper.writeValueAsString(bookResponse);

        String actual = mockMvc.perform(put("/api/book/" + UPDATE_ID)
                        .content(objectMapper.writeValueAsString(DEFAULT_CREATE_BOOK))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(4, bookRepository.count());

        assertTrue(redisTemplate.keys("*").isEmpty());

        JsonAssert.assertJsonEquals(expected, actual);
    }

    @Test
    public void whenDeleteById_returnNoContentAndEvictCache() throws Exception {
        assertTrue(redisTemplate.keys("*").isEmpty());
        assertEquals(4, bookRepository.count());

        mockMvc.perform(get("/api/book/book1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertFalse(redisTemplate.keys("*").isEmpty());

        mockMvc.perform(delete("/api/book/{id}", UPDATE_ID))
                .andExpect(status().isNoContent());

        assertTrue(redisTemplate.keys("*").isEmpty());
        assertEquals(3, bookRepository.count());
    }
}