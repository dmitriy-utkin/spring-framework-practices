package ru.example.news.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import ru.example.news.model.Comment;
import ru.example.news.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Long> countByNews(News news);
}
