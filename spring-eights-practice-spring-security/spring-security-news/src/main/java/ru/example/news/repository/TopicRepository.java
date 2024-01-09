package ru.example.news.repository;

import ru.example.news.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    Optional<Topic> findByTopic(String topic);
    Boolean existsByTopic(String topic);
}
