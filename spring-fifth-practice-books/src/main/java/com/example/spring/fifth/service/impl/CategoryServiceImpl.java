package com.example.spring.fifth.service.impl;

import com.example.spring.fifth.model.Category;
import com.example.spring.fifth.repository.CategoryRepository;
import com.example.spring.fifth.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category saveOrGetIfExists(Category category) {
        category.setName(category.getName().toLowerCase(Locale.ROOT));
        return categoryRepository.existsByName(category.getName()) ?
                findByName(category.getName()) : create(category);
    }

    @Override
    public Category findByName(String categoryName) {
        categoryName = categoryName.toLowerCase(Locale.ROOT);
        return categoryRepository.findByName(categoryName).orElse(null);
    }

    @Override
    public Category create(Category category) {
        category.setName(category.getName().toLowerCase(Locale.ROOT));
        return categoryRepository.save(category);
    }
}
