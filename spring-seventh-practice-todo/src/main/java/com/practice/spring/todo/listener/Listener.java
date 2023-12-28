package com.practice.spring.todo.listener;

import com.practice.spring.todo.configuration.UserConfig;
import com.practice.spring.todo.model.User;
import com.practice.spring.todo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "app.listening", name = "enable", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
@Slf4j
public class Listener {

    private final UserService userService;
    private final UserConfig userConfig;

    @Order(1)
    @EventListener(ApplicationStartedEvent.class)
    public void userAuthentication() {
        User user = Boolean.TRUE.equals(userService.existsByUsername(userConfig.getUsername()).block()) ?
                userService.findByUsername(userConfig.getUsername()).block() :
                userService.create(User.builder()
                                        .username(userConfig.getUsername())
                                        .email(userConfig.getEmail())
                                        .build()).block();

        userConfig.setId(user.getId());
        log.info("Signed as {} with id {}", user.getUsername(), user.getId());
    }

    @Order(2)
    @EventListener(ApplicationStartedEvent.class)
    public void addUsers() {
        for (int i = 0; i < 10; i++) {
            userService.create(User.builder()
                    .username("User " + (i + 1))
                    .email("email" + (i + 1) + "@some.com")
                    .build()).block();
        }
    }

}
