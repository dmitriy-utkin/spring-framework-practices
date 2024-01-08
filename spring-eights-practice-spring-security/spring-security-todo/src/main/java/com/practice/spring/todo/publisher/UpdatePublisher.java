package com.practice.spring.todo.publisher;

import com.practice.spring.todo.web.model.task.SimpleTaskResponse;
import com.practice.spring.todo.web.model.user.UserResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

@Component
public class UpdatePublisher {

    private final Sinks.Many<UserResponse> userUpdateSink;
    private final Sinks.Many<SimpleTaskResponse> taskUpdateSink;

    public UpdatePublisher() {
        this.userUpdateSink = Sinks.many().multicast().onBackpressureBuffer();
        this.taskUpdateSink = Sinks.many().multicast().onBackpressureBuffer();
    }

    public void publish(UserResponse userResponse) {
        userUpdateSink.tryEmitNext(userResponse);
    }

    public void publish(SimpleTaskResponse taskResponse) {
        taskUpdateSink.tryEmitNext(taskResponse);
    }

    public Sinks.Many<UserResponse> getUserUpdateSink() {
        return userUpdateSink;
    }

    public Sinks.Many<SimpleTaskResponse> getTaskUpdateSink() {
        return taskUpdateSink;
    }
}
