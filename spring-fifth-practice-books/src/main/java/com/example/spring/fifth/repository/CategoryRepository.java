package com.example.spring.fifth.repository;

import com.example.spring.fifth.model.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
    boolean existsByName(String categoryName);
    Optional<Category> findByName(String categoryName);
}
