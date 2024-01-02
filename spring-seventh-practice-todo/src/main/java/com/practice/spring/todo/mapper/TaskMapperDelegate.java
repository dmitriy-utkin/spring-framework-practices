package com.practice.spring.todo.mapper;

import com.practice.spring.todo.model.Task;
import com.practice.spring.todo.web.model.task.SimpleTaskResponse;
import com.practice.spring.todo.web.model.task.TaskResponse;
import com.practice.spring.todo.web.model.task.UpsertTaskRequest;

public abstract class TaskMapperDelegate implements TaskMapper {

    @Override
    public Task requestToTask(String authorId, UpsertTaskRequest request) {
        return Task.builder()
                .name(request.getName())
                .description(request.getDescription())
                .status(request.getStatus())
                .authorId(authorId)
                .assigneeId(request.getAssigneeId())
                .observerIds(request.getObserverIds())
                .build();
    }

    @Override
    public Task requestToTask(UpsertTaskRequest request) {
        return Task.builder()
                .name(request.getName())
                .description(request.getDescription())
                .status(request.getStatus())
                .assigneeId(request.getAssigneeId())
                .observerIds(request.getObserverIds())
                .build();
    }

    @Override
    public TaskResponse taskToResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .status(task.getStatus().toString())
                .author(task.getAuthor())
                .assignee(task.getAssignee())
                .observers(task.getObservers())
                .build();
    }

    @Override
    public SimpleTaskResponse taskToSimpleTaskResponse(Task task) {
        return SimpleTaskResponse.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .status(task.getStatus().toString())
                .authorId(task.getAuthorId())
                .assigneeId(task.getAssigneeId())
                .observerIds(task.getObserverIds())
                .build();
    }
}
