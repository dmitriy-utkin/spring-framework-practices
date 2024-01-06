package com.practice.spring.todo.web.model.task;

import com.practice.spring.todo.model.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertTaskRequest {

    private String name;

    private String description;

    private TaskStatus status;

    private String assigneeId;

    @Builder.Default
    private Set<String> observerIds = new HashSet<>();
}
