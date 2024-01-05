package com.practice.spring.todo.web.controller;

import com.practice.spring.todo.mapper.UserMapper;
import com.practice.spring.todo.publisher.UpdatePublisher;
import com.practice.spring.todo.service.UserService;
import com.practice.spring.todo.web.model.user.UpsertUserRequest;
import com.practice.spring.todo.web.model.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UpdatePublisher publisher;

    @GetMapping
    public Flux<UserResponse> getAllUsers() {
        return userService.findAll().map(userMapper::userToResponse);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> getUserById(@PathVariable String id) {
        return userService.findById(id)
                .map(userMapper::userToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public Mono<ResponseEntity<UserResponse>> getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(userMapper::userToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<UserResponse>> createUser(@RequestBody UpsertUserRequest request) {
        return userService.create(userMapper.requestToUser(request))
                .map(userMapper::userToResponse)
                .doOnSuccess(publisher::publish)
                .flatMap(newUser -> Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(newUser)));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> updateUserById(@PathVariable String id,
                                                             @RequestBody UpsertUserRequest request) {
        return userService.updateById(id, userMapper.requestToUser(request))
                .map(userMapper::userToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUserById(@PathVariable String id) {
        return userService.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
    }

}
