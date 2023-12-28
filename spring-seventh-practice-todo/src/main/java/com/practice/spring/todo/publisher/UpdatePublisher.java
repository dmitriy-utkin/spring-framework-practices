package com.practice.spring.todo.publisher;

import com.practice.spring.todo.web.model.task.TaskResponse;
import com.practice.spring.todo.web.model.user.UserResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

@Component
public class UpdatePublisher {

    private final Sinks.Many<UserResponse> userUpdateSink;
    private final Sinks.Many<TaskResponse> taskUpdateSink;

    public UpdatePublisher() {
        this.userUpdateSink = Sinks.many().multicast().onBackpressureBuffer();
        this.taskUpdateSink = Sinks.many().multicast().onBackpressureBuffer();
    }

    public void publish(UserResponse userResponse) {
        userUpdateSink.tryEmitNext(userResponse);
    }

    public void publish(TaskResponse taskResponse) {
        taskUpdateSink.tryEmitNext(taskResponse);
    }

    public Sinks.Many<UserResponse> getUserUpdateSink() {
        return userUpdateSink;
    }

    public Sinks.Many<TaskResponse> getTaskUpdateSink() {
        return taskUpdateSink;
    }
}
