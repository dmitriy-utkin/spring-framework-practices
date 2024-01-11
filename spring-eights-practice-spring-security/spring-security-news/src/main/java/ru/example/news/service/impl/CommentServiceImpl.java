package ru.example.news.service.impl;

import ru.example.news.aop.PrivilegeValidation;
import ru.example.news.exception.EntityNotFoundException;
import ru.example.news.model.Comment;
import ru.example.news.model.News;
import ru.example.news.repository.CommentRepository;
import ru.example.news.service.CommentService;
import ru.example.news.utils.BeanUtils;
import ru.example.news.web.model.defaults.FindAllSettings;
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
    public List<Comment> findAll() {
        return commentRepository
                .findAll();
    }

    @Override
    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Comment with ID " + id + " not found"));
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    @PrivilegeValidation
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
