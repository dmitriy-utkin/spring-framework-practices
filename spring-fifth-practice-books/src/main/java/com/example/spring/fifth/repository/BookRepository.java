package com.example.spring.fifth.repository;

import com.example.spring.fifth.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    Optional<Book> findByName(String name);
    Optional<Book> findByNameAndAuthor(String name, String author);
    Optional<List<Book>> findByAuthor(String author);
}
