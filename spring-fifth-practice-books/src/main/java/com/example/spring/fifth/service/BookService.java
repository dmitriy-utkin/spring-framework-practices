package com.example.spring.fifth.service;

import com.example.spring.fifth.model.Book;
import com.example.spring.fifth.web.model.defaults.FindAllSettings;

import java.util.List;

public interface BookService {
    List<Book> findAll(FindAllSettings findAllSettings);
    Book findById(Long id);
    Book findByName(String name);
    Book createBook(Book book);
    Book updateBook(Long id, Book book);
    void deleteBookById(Long id);
    Long count();
    List<Book> findAllByCategory(String category);
    Book findByNameAndAuthor(String name, String author);
    List<Book> findAllByAuthor(String author);
}
