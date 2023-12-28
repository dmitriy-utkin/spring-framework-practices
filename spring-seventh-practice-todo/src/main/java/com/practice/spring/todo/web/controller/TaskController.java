package com.practice.spring.todo.web.controller;

import com.practice.spring.todo.configuration.UserConfig;
import com.practice.spring.todo.mapper.TaskMapper;
import com.practice.spring.todo.publisher.UpdatePublisher;
import com.practice.spring.todo.service.TaskService;
import com.practice.spring.todo.web.model.task.TaskResponse;
import com.practice.spring.todo.web.model.task.UpsertTaskRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final UpdatePublisher publisher;
    private final UserConfig userConfig;

    @GetMapping
    public Flux<TaskResponse> getAllTasks() {
        return taskService.findAll().map(taskMapper::taskToResponse);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<TaskResponse>> getTaskById(@PathVariable String id) {
        return taskService.findById(id)
                .map(taskMapper::taskToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<TaskResponse>> createTask(@RequestBody UpsertTaskRequest request) {
        return taskService.create(taskMapper.requestToTask(userConfig.getId(), request))
                .map(taskMapper::taskToResponse)
                .doOnSuccess(publisher::publish)
                .flatMap(newTask -> Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(newTask)));
    }

    @PutMapping("/observers/{taskId}")
    public Mono<ResponseEntity<TaskResponse>> addObserver(@PathVariable String taskId, @RequestParam String observerId) {
        return taskService.addObserver(taskId, observerId)
                .map(taskMapper::taskToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<TaskResponse>> updateTaskById(@PathVariable String id,
                                                             @RequestBody UpsertTaskRequest request) {
        return taskService.updateById(id, taskMapper.requestToTask(request))
                .map(taskMapper::taskToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteTaskById(@PathVariable String id) {
        return taskService.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
    }

}
