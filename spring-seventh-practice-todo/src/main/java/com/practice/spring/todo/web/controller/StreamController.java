package com.practice.spring.todo.web.controller;

import com.practice.spring.todo.publisher.UpdatePublisher;
import com.practice.spring.todo.web.model.task.SimpleTaskResponse;
import com.practice.spring.todo.web.model.task.TaskResponse;
import com.practice.spring.todo.web.model.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/stream")
@RequiredArgsConstructor
public class StreamController {

    private final UpdatePublisher publisher;

    @GetMapping(value = "/user", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<UserResponse>> getUserUpdate() {
        return publisher.getUserUpdateSink().asFlux()
                .map(item -> ServerSentEvent.builder(item).build());
    }

    @GetMapping(value = "/task", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<SimpleTaskResponse>> getTaskUpdate() {
        return publisher.getTaskUpdateSink().asFlux()
                .map(item -> ServerSentEvent.builder(item).build());
    }
}
