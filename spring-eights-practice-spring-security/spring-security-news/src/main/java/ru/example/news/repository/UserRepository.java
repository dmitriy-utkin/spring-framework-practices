package ru.example.news.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import ru.example.news.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByUsername(String username);
}
