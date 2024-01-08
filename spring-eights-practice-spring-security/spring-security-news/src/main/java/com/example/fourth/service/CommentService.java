package com.example.fourth.service;

import com.example.fourth.model.Comment;
import com.example.fourth.model.News;
import com.example.fourth.web.model.defaults.FindAllSettings;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    List<Comment> findAll(FindAllSettings findAllSettings);
    Comment findById(Long id);
    Comment save(Comment comment);

    Comment update(Comment comment);
    void deleteById(Long id);
    void deleteByIds(List<Long> ids);
    Long count();
    Long countByNews(News news);
}
