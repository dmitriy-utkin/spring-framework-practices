package com.practice.spring.todo.web.model.task;

import com.practice.spring.todo.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleTaskResponse {

    private String id;

    private String name;

    private String description;

    private Instant createdAt;

    private Instant updatedAt;

    private String status;

    private String authorId;

    private String assigneeId;

    private Set<String> observerIds;
}
