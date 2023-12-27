package com.practice.spring.todo.service.impl;

import com.practice.spring.todo.exception.EntityAlreadyExists;
import com.practice.spring.todo.model.User;
import com.practice.spring.todo.repository.UserRepository;
import com.practice.spring.todo.service.UserService;
import com.practice.spring.todo.utils.EntityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Mono<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Mono<User> create(User user) {
        user.setId(UUID.randomUUID().toString());
        return userRepository.save(user);
    }

    @Override
    public Mono<User> updateById(String id, User user) {
        return findById(id).flatMap(userForUpdate -> {
            EntityUtil.copyNonNullProperties(user, userForUpdate);
            return userRepository.save(userForUpdate);
        });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return userRepository.deleteById(id);
    }
}
