package ru.example.news.mapper;

import org.mapstruct.DecoratedWith;
import ru.example.news.model.User;
import ru.example.news.web.model.user.UpsertUserRequest;
import ru.example.news.web.model.user.UserListResponse;
import ru.example.news.web.model.user.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(UserMapperDelegate.class)
public interface UserMapper {

    User requstToUser(UpsertUserRequest request);

    User requestToUser(Long id, UpsertUserRequest request);

    UserResponse userToUserResponse(User user);

    default UserListResponse userListToUserListResponse(List<User> users) {
        return UserListResponse.builder()
                .users(users.stream().map(this::userToUserResponse).toList())
                .build();
    }
}
