package com.example.fourth.service.impl;

import com.example.fourth.aop.IdLogger;
import com.example.fourth.aop.PrivilegeValidation;
import com.example.fourth.exception.EntityNotFoundException;
import com.example.fourth.model.Comment;
import com.example.fourth.model.News;
import com.example.fourth.repository.CommentRepository;
import com.example.fourth.service.CommentService;
import com.example.fourth.utils.BeanUtils;
import com.example.fourth.web.model.defaults.FindAllSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public List<Comment> findAll(FindAllSettings findAllSettings) {
        return commentRepository
                .findAll(PageRequest.of(findAllSettings.getPageNum(), findAllSettings.getPageSize())).getContent();
    }

    @Override
    @IdLogger
    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Comment with ID " + id + " not found"));
    }

    @Override
    @IdLogger
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    @PrivilegeValidation
    @IdLogger
    public Comment update(Comment comment) {
        Comment existedComment = findById(comment.getId());
        BeanUtils.copyNonNullProperties(comment, existedComment);
        return commentRepository.save(existedComment);
    }

    @Override
    @PrivilegeValidation
    public void deleteById(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new EntityNotFoundException("Comment with ID " + id + " not found");
        }
        commentRepository.deleteById(id);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        commentRepository.deleteAllById(ids);
    }

    @Override
    public Long count() {
        return commentRepository.count();
    }

    @Override
    public Long countByNews(News news) {
        return commentRepository.countByNews(news).orElse(0L);
    }
}
