package ru.example.news.web.controller.defaults;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.example.news.mapper.UserMapper;
import ru.example.news.service.UserService;
import ru.example.news.web.model.user.UpsertUserRequest;
import ru.example.news.web.model.user.UserResponse;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/account")
    public ResponseEntity<UserResponse> createAccount(@RequestBody @Valid UpsertUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                userMapper.userToUserResponse(
                        userService.save(userMapper.requstToUser(request))
                )
        );
    };
}
