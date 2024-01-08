package com.practice.spring.todo.service.impl;

import com.practice.spring.todo.model.Task;
import com.practice.spring.todo.model.User;
import com.practice.spring.todo.repository.TaskRepository;
import com.practice.spring.todo.service.TaskService;
import com.practice.spring.todo.service.UserService;
import com.practice.spring.todo.utils.EntityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    @Override
    public Flux<Task> findAll() {
        Sort sort = Sort.by(Sort.Direction.ASC, Task.Fields.createdAt);
        return taskRepository.findAll(sort)
                .flatMap(this::getZippedMonoTask);
    }

    @Override
    public Mono<Task> findById(String id) {
        return taskRepository.findById(id)
                .flatMap(this::getZippedMonoTask);
    }

    @Override
    public Mono<Task> findByName(String name) {
        return taskRepository.findByName(name);
    }

    @Override
    public Mono<Task> create(Task task) {
        task.setId(UUID.randomUUID().toString());
        task.setCreatedAt(Instant.now());
        task.setUpdatedAt(Instant.now());
        return taskRepository.save(task);
    }

    @Override
    public Mono<Task> updateById(String id, Task task) {
        return findById(id).flatMap(taskForUpdate -> {
            EntityUtil.copyNonNullProperties(task, taskForUpdate);
            taskForUpdate.setUpdatedAt(Instant.now());
            return taskRepository.save(taskForUpdate);
        });
    }

    @Override
    public Mono<Task> addObserver(String taskId, String observerId) {
        return findById(taskId).flatMap(taskForObserverAdding -> {
            Set<String> observers = taskForObserverAdding.getObserverIds();
            observers.add(observerId);
            taskForObserverAdding.setObserverIds(observers);
            taskForObserverAdding.setUpdatedAt(Instant.now());
            return taskRepository.save(taskForObserverAdding);
        });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return taskRepository.deleteById(id);
    }

    private Mono<Task> getZippedMonoTask(Task task) {

        Mono<User> monoAuthor = task.getAuthorId() == null ? Mono.just(User.emptyUser()) : userService.findById(task.getAuthorId());
        Mono<User> monoAssignee = task.getAssigneeId() == null ? Mono.just(User.emptyUser()) : userService.findById(task.getAssigneeId());
        Flux<User> fluxObservers = task.getObserverIds().size() == 0 ? Flux.empty() : userService.findAllByIds(task.getObserverIds());

        return Mono.zip(monoAuthor, monoAssignee, fluxObservers.collectList())
                .map(tuple -> {
                    task.setAuthor(tuple.getT1());
                    task.setAssignee(tuple.getT2());
                    task.setObservers(new HashSet<>(tuple.getT3()));

                    return task;
                });
    }
}
