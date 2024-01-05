package com.practice.spring.todo.web;

import com.practice.spring.todo.model.Task;
import com.practice.spring.todo.model.TaskStatus;
import com.practice.spring.todo.model.User;
import com.practice.spring.todo.repository.TaskRepository;
import com.practice.spring.todo.repository.UserRepository;
import com.practice.spring.todo.web.model.task.TaskResponse;
import com.practice.spring.todo.web.model.user.UserResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Testcontainers
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class TestAbstract {

    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected TaskRepository taskRepository;

    protected static String FIRST_USER_ID = UUID.randomUUID().toString();
    protected static String SECOND_USER_ID = UUID.randomUUID().toString();

    protected static String FIRST_TASK_ID = UUID.randomUUID().toString();
    protected static String SECOND_TASK_ID = UUID.randomUUID().toString();

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.8")
            .withReuse(true);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    public void setup() {
        List<User> users = List.of(
                new User(FIRST_USER_ID, "user1", "user1@email.com"),
                new User(SECOND_USER_ID, "user2", "user2@email.com")
        );

        List<Task> tasks = List.of(
                createDefaultTask(FIRST_USER_ID, FIRST_TASK_ID, 1),
                createDefaultTask(SECOND_USER_ID, SECOND_TASK_ID, 2)
        );

        userRepository.saveAll(users).collectList().block();
        taskRepository.saveAll(tasks).collectList().block();
    }

    @AfterEach
    public void after() {
        userRepository.deleteAll().block();
        taskRepository.deleteAll().block();
    }

    protected Task createDefaultTask(String userId, String taskId, int taskNum) {
        return Task.builder()
                .id(taskId)
                .name("task" + taskNum)
                .description("description" + taskNum)
                .status(TaskStatus.TODO)
                .authorId(userId)
                .observerIds(new HashSet<>())
                .build();
    }

    protected TaskResponse createDefaultTaskResponse(int userNum, String userId, String taskId, int taskNum) {
        return TaskResponse.builder()
                .id(taskId)
                .name("task" + taskNum)
                .description("description" + taskNum)
                .author(new User(userId, ("user" + userNum), ("user" + userNum + "@email.com")))
                .assignee(User.emptyUser())
                .status(TaskStatus.TODO.toString())
                .observers(new HashSet<>())
                .build();
    }

    protected UserResponse createDefaultUserResponse(int userNum, String userId) {
        return UserResponse.builder()
                .id(userId)
                .username("user" + userNum)
                .email("user" + userNum + "@email.com")
                .build();
    }
}
