package com.practice.spring.todo.service;

import com.practice.spring.todo.model.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {

    Flux<Task> findAll();
    Mono<Task> findById(String id);
    Mono<Task> findByName(String name);
    Mono<Task> create(Task task);
    Mono<Task> updateById(String id, Task task);
    Mono<Void> deleteById(String id);

}
