package com.example.fourth.mapper;

import com.example.fourth.model.User;
import com.example.fourth.web.model.user.UpsertUserRequest;
import com.example.fourth.web.model.user.UserListResponse;
import com.example.fourth.web.model.user.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User requstToUser(UpsertUserRequest request);

    User requsetToUser(Long id, UpsertUserRequest request);

    UserResponse userToUserResponse(User user);

    default UserListResponse userListToUserListResponse(List<User> users) {
        return UserListResponse.builder()
                .users(users.stream().map(this::userToUserResponse).toList())
                .build();
    }
}
