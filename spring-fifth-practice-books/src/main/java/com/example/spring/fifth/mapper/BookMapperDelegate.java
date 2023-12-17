package com.example.spring.fifth.mapper;

import com.example.spring.fifth.model.Book;
import com.example.spring.fifth.model.Category;
import com.example.spring.fifth.service.CategoryService;
import com.example.spring.fifth.web.model.book.BookResponse;
import com.example.spring.fifth.web.model.book.UpsertBookRequest;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BookMapperDelegate implements BookMapper {

    @Autowired
    private CategoryService categoryService;

    @Override
    public BookResponse bookToBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .author(book.getAuthor())
                .name(book.getName())
                .category(book.getCategory().getName())
                .build();
    }

    @Override
    public Book requestToBook(UpsertBookRequest request) {
        return Book.builder()
                .author(request.getAuthor())
                .name(request.getName())
                .category(categoryService.saveOrGetIfExists(Category.builder().name(request.getCategory()).build()))
                .build();
    }
}
