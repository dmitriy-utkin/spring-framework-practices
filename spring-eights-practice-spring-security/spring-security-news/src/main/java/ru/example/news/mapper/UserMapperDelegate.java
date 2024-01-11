package ru.example.news.mapper;

import ru.example.news.model.User;
import ru.example.news.web.model.user.UpsertUserRequest;

public abstract class UserMapperDelegate implements UserMapper {

    @Override
    public User requstToUser(UpsertUserRequest request) {
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .roles(request.getRoles())
                .build();
    }

    @Override
    public User requestToUser(Long id, UpsertUserRequest request) {
        var user = requstToUser(request);
        user.setId(id);
        return user;
    }
}
