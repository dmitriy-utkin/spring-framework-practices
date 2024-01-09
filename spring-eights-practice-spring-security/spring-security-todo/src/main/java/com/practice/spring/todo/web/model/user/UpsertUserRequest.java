package com.practice.spring.todo.web.model.user;

import com.practice.spring.todo.model.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpsertUserRequest {
    private String username;
    private String email;
    private String password;

    @Builder.Default
    private Set<RoleType> roles = new HashSet<>();
}
