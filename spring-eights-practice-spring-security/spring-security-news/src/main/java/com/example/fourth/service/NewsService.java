package com.example.fourth.service;

import com.example.fourth.model.News;
import com.example.fourth.web.model.defaults.FindAllSettings;

import java.util.List;

public interface NewsService {
    List<News> findAll(FindAllSettings findAllSettings);
    News findById(Long id);
    News save(News news);
    News update(News news);
    void deleteById(Long id);
    void deleteByIds(List<Long> ids);
    Long count();
}
