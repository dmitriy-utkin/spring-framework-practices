package com.example.spring.fifth.repository;

import com.example.spring.fifth.model.Book;
import com.example.spring.fifth.model.Category;
import com.example.spring.fifth.web.model.book.BookFilter;
import org.springframework.data.jpa.domain.Specification;

public interface BookSpecification {

    static Specification<Book> withFilter(BookFilter filter) {
        return Specification.where(byBookName(filter.getBookName()))
                .and(byCategoryName(filter.getCategoryName()))
                .and(byCategoryId(filter.getCategoryId()));
    }

    static Specification<Book> byCategoryId(Integer categoryId) {
        return (root, query, cb) -> {
            if (categoryId == null) {
                return null;
            }
            return cb.equal(root.get(Book.Fields.category).get(Category.Fields.id), categoryId);
        };
    }

    static Specification<Book> byCategoryName(String categoryName) {
        return (root, query, cb) -> {
            if (categoryName == null) {
                return null;
            }
            return cb.equal(root.get(Book.Fields.category).get(Category.Fields.name), categoryName);
        };
    }

    static Specification<Book> byBookName(String bookName) {
        return (root, query, cb) -> {
            if (bookName == null) {
                return null;
            }
            return cb.equal(root.get(Book.Fields.name), bookName);
        };
    }


}
