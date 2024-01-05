package com.practice.spring.todo.web.controller;

import com.practice.spring.todo.web.TestAbstract;
import com.practice.spring.todo.web.model.user.UpsertUserRequest;
import com.practice.spring.todo.web.model.user.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserControllerTest extends TestAbstract {

    @Test
    public void whenGetAllUsers_thenReturnAllUsers() {
        var expectedData = List.of(
                createDefaultUserResponse(1, FIRST_USER_ID),
                createDefaultUserResponse(2, SECOND_USER_ID)
        );

        webTestClient.get().uri("/api/user")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponse.class)
                .hasSize(2)
                .contains(expectedData.toArray(UserResponse[]::new));
    }

    @Test
    public void whenGetUserById_thenReturnUser() {
        var expectedData = createDefaultUserResponse(1, FIRST_USER_ID);

        webTestClient.get().uri("/api/user/{id}", FIRST_USER_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .isEqualTo(expectedData);
    }

    @Test
    public void whenGetUserByUsername_thenReturnUser() {
        var expectedData = createDefaultUserResponse(2, SECOND_USER_ID);

        webTestClient.get().uri("/api/user/username/{username}", "user2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .isEqualTo(expectedData);
    }

    @Test
    public void whenCreateUser_thenReturnUserWithIdAndPublishEvent() {
        StepVerifier.create(userRepository.count())
                .expectNext(2L)
                .expectComplete()
                .verify();

        UpsertUserRequest request = UpsertUserRequest.builder()
                .username("newUser")
                .email("newUser@email.com")
                .build();

        webTestClient.post().uri("/api/user")
                        .body(Mono.just(request), UpsertUserRequest.class)
                        .exchange()
                        .expectStatus().isCreated()
                        .expectBody(UserResponse.class)
                        .value(response -> {
                            assertNotNull(response.getId());
                        });

        StepVerifier.create(userRepository.count())
                .expectNext(3L)
                .expectComplete()
                .verify();

        webTestClient.get().uri("/api/stream/user")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .returnResult(new ParameterizedTypeReference<ServerSentEvent<UserResponse>>() {
                })
                .getResponseBody()
                .take(1L)
                .as(StepVerifier::create)
                .consumeNextWith(serverSentEvent -> {
                    var event = serverSentEvent.data();
                    assertNotNull(event);
                    assertEquals("newUser", event.getUsername());
                    assertNotNull(event.getId());
                    assertEquals("newUser@email.com", event.getEmail());
                })
                .thenCancel()
                .verify();
    }

    @Test
    public void whenUpdateUser_thenReturnUser() {
        StepVerifier.create(userRepository.count())
                .expectNext(2L)
                .expectComplete()
                .verify();

        UpsertUserRequest request = UpsertUserRequest.builder()
                .username("updatedUsername")
                .build();

        webTestClient.put().uri("/api/user/{id}", FIRST_USER_ID)
                .body(Mono.just(request), UpsertUserRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .value(response -> {
                    assertEquals("updatedUsername", response.getUsername());
                    assertEquals("user1@email.com", response.getEmail());
                    assertNotNull(response.getId());
                });

        StepVerifier.create(userRepository.count())
                .expectNext(2L)
                .expectComplete()
                .verify();
    }

    @Test
    public void whenDeleteUserById_thenReturnNoContentAndChangeUserNumbers() {
        StepVerifier.create(userRepository.count())
                .expectNext(2L)
                .expectComplete()
                .verify();

        webTestClient.delete().uri("/api/user/{id}", FIRST_USER_ID)
                        .exchange()
                        .expectStatus().isNoContent();

        StepVerifier.create(userRepository.count())
                .expectNext(1L)
                .expectComplete()
                .verify();
    }
}
