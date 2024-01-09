package com.practice.spring.todo.web.controller;

import com.practice.spring.todo.mapper.TaskMapper;
import com.practice.spring.todo.publisher.UpdatePublisher;
import com.practice.spring.todo.service.TaskService;
import com.practice.spring.todo.web.model.task.SimpleTaskResponse;
import com.practice.spring.todo.web.model.task.TaskResponse;
import com.practice.spring.todo.web.model.task.UpsertTaskRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final UpdatePublisher publisher;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'MANAGER')")
    public Flux<TaskResponse> getAllTasks() {
        return taskService.findAll().map(taskMapper::taskToResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER')")
    public Mono<ResponseEntity<TaskResponse>> getTaskById(@PathVariable String id) {
        return taskService.findById(id)
                .map(taskMapper::taskToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public Mono<ResponseEntity<SimpleTaskResponse>> createTask(Mono<Principal> principal, @RequestBody UpsertTaskRequest request) {
        return principal.map(Principal::getName)
                .flatMap(authorUsername -> taskService.create(taskMapper.requestToTask(request), authorUsername)
                .map(taskMapper::taskToSimpleTaskResponse)
                .doOnSuccess(publisher::publish)
                .flatMap(newTask -> Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(newTask))));
    }

    @PutMapping("/observers/{taskId}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER')")
    public Mono<ResponseEntity<SimpleTaskResponse>> addObserver(@PathVariable String taskId, @RequestParam String observerId) {
        return taskService.addObserver(taskId, observerId)
                .map(taskMapper::taskToSimpleTaskResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public Mono<ResponseEntity<SimpleTaskResponse>> updateTaskById(@PathVariable String id,
                                                                   @RequestBody UpsertTaskRequest request) {
        return taskService.updateById(id, taskMapper.requestToTask(request))
                .map(taskMapper::taskToSimpleTaskResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public Mono<ResponseEntity<Void>> deleteTaskById(@PathVariable String id) {
        return taskService.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
    }

}
