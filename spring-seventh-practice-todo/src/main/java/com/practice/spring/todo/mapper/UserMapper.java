package com.practice.spring.todo.mapper;

import com.practice.spring.todo.model.User;
import com.practice.spring.todo.web.model.UpsertUserRequest;
import com.practice.spring.todo.web.model.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User requestToUser(UpsertUserRequest request);

    UserResponse userToResponse(User user);

}
