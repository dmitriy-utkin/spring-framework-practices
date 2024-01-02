package com.practice.spring.todo.mapper;

import com.practice.spring.todo.model.Task;
import com.practice.spring.todo.web.model.task.SimpleTaskResponse;
import com.practice.spring.todo.web.model.task.TaskResponse;
import com.practice.spring.todo.web.model.task.UpsertTaskRequest;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@DecoratedWith(TaskMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    Task requestToTask(UpsertTaskRequest request);

    Task requestToTask(String authorId, UpsertTaskRequest request);

    TaskResponse taskToResponse(Task task);

    SimpleTaskResponse taskToSimpleTaskResponse(Task task);
}
