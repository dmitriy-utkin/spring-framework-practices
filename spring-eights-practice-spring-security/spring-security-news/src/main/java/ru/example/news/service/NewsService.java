package ru.example.news.service;

import ru.example.news.model.News;
import ru.example.news.web.model.defaults.FindAllSettings;

import java.util.List;

public interface NewsService {
    List<News> findAll(FindAllSettings findAllSettings);
    List<News> findAll();
    News findById(Long id);
    News save(News news, String username);
    News update(News news);
    void deleteById(Long id);
    void deleteByIds(List<Long> ids);
    Long count();
}
