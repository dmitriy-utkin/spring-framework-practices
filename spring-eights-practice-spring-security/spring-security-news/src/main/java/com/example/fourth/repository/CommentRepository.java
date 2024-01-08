package com.example.fourth.repository;

import com.example.fourth.model.Comment;
import com.example.fourth.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Long> countByNews(News news);
}
