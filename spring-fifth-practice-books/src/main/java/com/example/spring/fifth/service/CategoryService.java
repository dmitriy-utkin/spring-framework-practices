package com.example.spring.fifth.service;

import com.example.spring.fifth.model.Category;

public interface CategoryService {
    Category saveOrGetIfExists(Category category);
    Category create(Category category);
    Category findByName(String categoryName);
}
