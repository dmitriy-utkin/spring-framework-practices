package com.example.spring.fifth.service;

import com.example.spring.fifth.model.Book;
import com.example.spring.fifth.web.model.defaults.FindAllSettings;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BookService {
    List<Book> findAll(FindAllSettings settings);
    Book findById(Long id);
    Book findByName(String name);
    Book createBook(Book book);
    Book updateBook(Long id, Book book);
    void deleteBookById(Long id);
    Long count();
}
