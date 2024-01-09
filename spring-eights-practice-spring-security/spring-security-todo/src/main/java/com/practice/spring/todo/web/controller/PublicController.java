package com.practice.spring.todo.web.controller;

import com.practice.spring.todo.mapper.UserMapper;
import com.practice.spring.todo.model.User;
import com.practice.spring.todo.publisher.UpdatePublisher;
import com.practice.spring.todo.service.UserService;
import com.practice.spring.todo.web.model.user.UpsertUserRequest;
import com.practice.spring.todo.web.model.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UpdatePublisher publisher;

    @PostMapping("/account")
    public Mono<ResponseEntity<UserResponse>> createUser(@RequestBody UpsertUserRequest request) {

        return userService.create(userMapper.requestToUser(request))
                .map(userMapper::userToResponse)
                .doOnSuccess(publisher::publish)
                .flatMap(newUser -> Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(newUser)));
    }
}
