package com.example.spring.fifth.repository;

import com.example.spring.fifth.model.Book;
import com.example.spring.fifth.model.Category;
import com.example.spring.fifth.web.model.book.BookFilter;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public interface BookSpecification {

    static Specification<Book> withFilter(BookFilter bookFilter) {
        return Specification.where(byBookName(bookFilter.getBookName()))
                .and(byCategoryName(bookFilter.getCategoryName()))
                .and(byCategoryId(bookFilter.getCategoryId()))
                .and(byAuthor(bookFilter.getAuthor()));
    }

    static Specification<Book> byAuthor(String author) {
        return (root, query, cb) -> {
            if (author == null) {
                return null;
            }
            return cb.equal(root.get(Book.Fields.author), author);
        };
    }

    static Specification<Book> byCategoryId(Long categoryId) {
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
            return cb.equal(root.get(Book.Fields.category).get(Category.Fields.name),
                    categoryName.toLowerCase(Locale.ROOT));
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
