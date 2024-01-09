package com.practice.spring.todo.listener;

import com.practice.spring.todo.model.RoleType;
import com.practice.spring.todo.model.User;
import com.practice.spring.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app.listening", name = "enable", havingValue = "true")
public class ListenerOnReady {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @EventListener(ApplicationStartedEvent.class)
    public void uploadUsers() {
        userRepository.deleteAll().block();
        userRepository.saveAll(createUserList(3, RoleType.ROLE_USER)).blockLast();
        userRepository.saveAll(createUserList(1, RoleType.ROLE_MANAGER)).blockLast();
    }

    private List<User> createUserList(int count, RoleType role) {
        String username = role.name().toLowerCase(Locale.ROOT).replace("role_", "");
        List<User> users = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            users.add(User.builder()
                    .id(UUID.randomUUID().toString())
                    .username(username + i)
                    .email(username + i + "@email.com")
                    .password(passwordEncoder.encode("pass"))
                    .roles(Collections.singleton(role))
                    .build());
        }

        return users;
    }
}
