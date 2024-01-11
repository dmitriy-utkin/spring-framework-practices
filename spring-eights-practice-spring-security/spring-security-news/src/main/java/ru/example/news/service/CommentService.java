package ru.example.news.service;

import ru.example.news.model.Comment;
import ru.example.news.model.News;
import ru.example.news.web.model.defaults.FindAllSettings;

import java.util.List;

public interface CommentService {

    List<Comment> findAll(FindAllSettings findAllSettings);
    List<Comment> findAll();
    Comment findById(Long id);
    Comment save(Comment comment);

    Comment update(Comment comment);
    void deleteById(Long id);
    void deleteByIds(List<Long> ids);
    Long count();
    Long countByNews(News news);
}
