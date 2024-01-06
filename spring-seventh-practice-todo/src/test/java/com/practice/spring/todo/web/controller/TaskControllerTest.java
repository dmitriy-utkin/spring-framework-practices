package com.practice.spring.todo.web.controller;

import com.practice.spring.todo.model.TaskStatus;
import com.practice.spring.todo.web.TestAbstract;
import com.practice.spring.todo.web.model.task.SimpleTaskResponse;
import com.practice.spring.todo.web.model.task.TaskResponse;
import com.practice.spring.todo.web.model.task.UpsertTaskRequest;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Set;

public class TaskControllerTest extends TestAbstract {

    @Test
    public void whenGetAllTask_thenReturnAllTask() {
        var expectedData = List.of(
                createDefaultTaskResponse(1, FIRST_USER_ID, FIRST_TASK_ID, 1),
                createDefaultTaskResponse(2, SECOND_USER_ID, SECOND_TASK_ID, 2)
        );

        webTestClient.get().uri("/api/task")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TaskResponse.class)
                .hasSize(2)
                .contains(expectedData.toArray(TaskResponse[]::new));
    }

    @Test
    public void whenGetTaskById_theReturnOneTask() {
        var expectedDate = createDefaultTaskResponse(1, FIRST_USER_ID, FIRST_TASK_ID, 1);

        webTestClient.get().uri("/api/task/" + FIRST_TASK_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponse.class)
                .isEqualTo(expectedDate);
    }

    @Test
    public void whenCreateTask_thenReturnNewItemWithIdAndPublishNewEvent() {
        StepVerifier.create(taskRepository.count())
                .expectNext(2L)
                .expectComplete()
                .verify();

        UpsertTaskRequest request = UpsertTaskRequest.builder()
                .name("newTask")
                .description("newDescription")
                .status(TaskStatus.TODO)
                .build();

        webTestClient.post().uri("/api/task")
                .body(Mono.just(request), UpsertTaskRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(SimpleTaskResponse.class)
                .value(response -> {
                    assertNotNull(response.getId());
                });

        StepVerifier.create(taskRepository.count())
                .expectNext(3L)
                .expectComplete()
                .verify();

        webTestClient.get().uri("/api/stream/task")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .returnResult(new ParameterizedTypeReference<ServerSentEvent<SimpleTaskResponse>>() {
                })
                .getResponseBody()
                .take(1L)
                .as(StepVerifier::create)
                .consumeNextWith(serverSentEvent -> {
                    var event = serverSentEvent.data();
                    assertNotNull(event);
                    assertEquals("newTask", event.getName());
                    assertNotNull(event.getId());
                })
                .thenCancel()
                .verify();
    }

    @Test
    public void whenUpdateTaskById_thenReturnUpdatedItem() {
        UpsertTaskRequest request = UpsertTaskRequest.builder()
                .name("updatedTask")
                .status(TaskStatus.IN_PROGRESS)
                .build();

        webTestClient.put().uri("/api/task/{id}", FIRST_TASK_ID)
                .body(Mono.just(request), UpsertTaskRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SimpleTaskResponse.class)
                .value(response -> {
                    assertEquals(response.getStatus(), "IN_PROGRESS");
                    assertEquals(response.getName(), "updatedTask");
                });
    }

    @Test
    public void whenAddNewObserverToTaskById_thenReturnUpdatedTask() {
        webTestClient.put().uri("/api/task/observers/{taskId}?observerId={observerId}", FIRST_TASK_ID, SECOND_USER_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SimpleTaskResponse.class)
                .value(response -> {
                    assertEquals(response.getId(), FIRST_TASK_ID);
                    assertEquals(response.getObserverIds(), Set.of(SECOND_USER_ID));
                });
    }

    @Test
    public void whenDeleteTaskById_thenReturnNoContentAndChangedItemNumbers() {
        StepVerifier.create(taskRepository.count())
                .expectNext(2L)
                .expectComplete()
                .verify();

        webTestClient.delete().uri("/api/task/{id}", FIRST_TASK_ID)
                        .exchange()
                        .expectStatus().isNoContent();

        StepVerifier.create(taskRepository.count())
                .expectNext(1L)
                .expectComplete()
                .verify();
    }
}
