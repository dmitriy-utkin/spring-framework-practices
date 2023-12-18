package com.example.spring.fifth.service.impl;

import com.example.spring.fifth.config.properties.AppCacheProperties;
import com.example.spring.fifth.exception.EntityNotFoundException;
import com.example.spring.fifth.model.Book;
import com.example.spring.fifth.repository.BookRepository;
import com.example.spring.fifth.repository.BookSpecification;
import com.example.spring.fifth.service.BookService;
import com.example.spring.fifth.utils.EntityUtil;
import com.example.spring.fifth.web.model.book.BookFilter;
import com.example.spring.fifth.web.model.defaults.FindAllSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheManager = "redisCacheManager")
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Cacheable(value = AppCacheProperties.CacheNames.DB_BOOKS, key = "#findAllSettings")
    @Override
    public List<Book> findAll(FindAllSettings findAllSettings) {
        return bookRepository.findAll(BookSpecification.withFilter(findAllSettings.getFilter()),
                PageRequest.of(findAllSettings.getPageNum(), findAllSettings.getPageSize())).getContent();
    }

    @Cacheable(value = AppCacheProperties.CacheNames.DB_BOOK_BY_ID, key = "#id")
    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Book with id " + id + " not found."));
    }

    @Cacheable(value = AppCacheProperties.CacheNames.DB_BOOK_BY_NAME, key = "#name")
    @Override
    public Book findByName(String name) {
        return bookRepository.findByName(name)
                .orElseThrow(() ->
                        new EntityNotFoundException("Book with name \"" + name + "\" not found."));
    }

    @Cacheable(value = AppCacheProperties.CacheNames.DB_BOOKS_BY_AUTHOR, key = "#author")
    @Override
    public List<Book> findAllByAuthor(String author) {
        return bookRepository.findByAuthor(author)
                .orElseThrow(() ->
                        new EntityNotFoundException("Books by author \"" + author + "\" not found"));
    }

    @Cacheable(value = AppCacheProperties.CacheNames.DB_BOOKS_BY_CATEGORY, key = "#category")
    @Override
    public List<Book> findAllByCategory(String category) {
        BookFilter bookFilter = BookFilter.builder().categoryName(category).build();
        return bookRepository.findAll(BookSpecification.withFilter(bookFilter));
    }

    @Cacheable(value = AppCacheProperties.CacheNames.DB_BOOK_BY_NAME_AND_AUTHOR, key = "#name + #author")
    @Override
    public Book findByNameAndAuthor(String name, String author) {
        return bookRepository.findByNameAndAuthor(name, author).orElseThrow(
                () -> new EntityNotFoundException("Book with author " + author +
                        " and name " + name + " not found.")
        );
    }

    @Caching(evict = {
            @CacheEvict(value = AppCacheProperties.CacheNames.DB_BOOKS, allEntries = true),
            @CacheEvict(value = AppCacheProperties.CacheNames.DB_BOOKS_BY_CATEGORY, allEntries = true),
            @CacheEvict(value = AppCacheProperties.CacheNames.DB_BOOKS_BY_AUTHOR, allEntries = true),
    })
    @Override
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @Caching(evict = {
            @CacheEvict(value = AppCacheProperties.CacheNames.DB_BOOKS, allEntries = true),
            @CacheEvict(value = AppCacheProperties.CacheNames.DB_BOOKS_BY_CATEGORY, allEntries = true),
            @CacheEvict(value = AppCacheProperties.CacheNames.DB_BOOKS_BY_AUTHOR, allEntries = true),
            @CacheEvict(value = AppCacheProperties.CacheNames.DB_BOOK_BY_NAME, allEntries = true),
            @CacheEvict(value = AppCacheProperties.CacheNames.DB_BOOK_BY_ID, allEntries = true),
            @CacheEvict(value = AppCacheProperties.CacheNames.DB_BOOK_BY_NAME_AND_AUTHOR, allEntries = true)
    })
    @Override
    public Book updateBook(Long id, Book book) {
        Book existedBook = findById(id);
        EntityUtil.copyNotNullFields(book, existedBook); //TODO: убедиться, что поле id заполняется корректно
        return bookRepository.save(existedBook);
    }

    @Caching(evict = {
            @CacheEvict(value = AppCacheProperties.CacheNames.DB_BOOKS, allEntries = true),
            @CacheEvict(value = AppCacheProperties.CacheNames.DB_BOOKS_BY_CATEGORY, allEntries = true),
            @CacheEvict(value = AppCacheProperties.CacheNames.DB_BOOKS_BY_AUTHOR, allEntries = true),
            @CacheEvict(value = AppCacheProperties.CacheNames.DB_BOOK_BY_NAME, allEntries = true),
            @CacheEvict(value = AppCacheProperties.CacheNames.DB_BOOK_BY_ID, allEntries = true),
            @CacheEvict(value = AppCacheProperties.CacheNames.DB_BOOK_BY_NAME_AND_AUTHOR, allEntries = true)
    })
    @Override
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Long count() {
        return bookRepository.count();
    }
}
