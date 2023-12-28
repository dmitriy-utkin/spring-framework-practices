package com.practice.spring.todo.service;

import com.practice.spring.todo.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    Flux<User> findAll();
    Mono<User> findById(String id);
    Mono<User> findByUsername(String username);
    Mono<User> create(User user);
    Mono<User> updateById(String id, User user);
    Mono<Void> deleteById(String id);
    Mono<Boolean> existsByUsername(String username);

}
