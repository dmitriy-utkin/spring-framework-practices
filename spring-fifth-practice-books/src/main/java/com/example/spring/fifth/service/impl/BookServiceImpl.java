package com.example.spring.fifth.service.impl;

import com.example.spring.fifth.exception.EntityNotFoundException;
import com.example.spring.fifth.model.Book;
import com.example.spring.fifth.repository.BookRepository;
import com.example.spring.fifth.repository.BookSpecification;
import com.example.spring.fifth.service.BookService;
import com.example.spring.fifth.utils.EntityUtil;
import com.example.spring.fifth.web.model.book.BookFilter;
import com.example.spring.fifth.web.model.defaults.FindAllSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public List<Book> findAll(FindAllSettings findAllSettings) {
        return bookRepository.findAll(BookSpecification.withFilter(findAllSettings.getFilter()),
                PageRequest.of(findAllSettings.getPageNum(), findAllSettings.getPageSize())).getContent();
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Book with id " + id + " not found."));
    }

    @Override
    public Book findByName(String name) {
        return bookRepository.findByName(name)
                .orElseThrow(() ->
                        new EntityNotFoundException("Book with name \"" + name + "\" not found."));
    }

    @Override
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Long id, Book book) {
        Book existedBook = findById(id);
        EntityUtil.copyNotNullFields(book, existedBook); //TODO: убедиться, что поле id заполняется корректно
        return bookRepository.save(existedBook);
    }

    @Override
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Long count() {
        return bookRepository.count();
    }

    @Override
    public List<Book> findByCategory(String category) {
        BookFilter bookFilter = BookFilter.builder().categoryName(category).build();
        return bookRepository.findAll(BookSpecification.withFilter(bookFilter));
    }

    @Override
    public Book findByNameAndAuthor(String name, String author) {
        return bookRepository.findByNameAndAuthor(name, author).orElseThrow(
                () -> new EntityNotFoundException("Book with author " + author +
                        " and name " + name + " not found.")
        );
    }
}
