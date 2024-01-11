package ru.example.news.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import ru.example.news.mapper.UserMapper;
import ru.example.news.model.User;
import ru.example.news.service.UserService;
import ru.example.news.web.model.defaults.ErrorResponse;
import ru.example.news.web.model.defaults.FindAllSettings;
import ru.example.news.web.model.user.UpsertUserRequest;
import ru.example.news.web.model.user.UserListResponse;
import ru.example.news.web.model.user.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(
            summary = "Get all users",
            description = "To get all users with pagination (default value for page size is 10).",
            tags = {"user-controller", "user"}
    )
    @ApiResponse(
            responseCode = "200",
            content = {
                    @Content(schema = @Schema(implementation = UserListResponse.class), mediaType = "application/json")
            }
    )
    @GetMapping("/filter")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserListResponse> findAllWithFilter(@RequestBody @Valid FindAllSettings findAllSettings) {
        return ResponseEntity.ok(
                userMapper.userListToUserListResponse(
                        userService.findAll(findAllSettings)
                )
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserListResponse> findAll() {
        return ResponseEntity.ok(
                userMapper.userListToUserListResponse(
                        userService.findAll()
                )
        );
    }

    @Operation(
            summary = "Get user by id",
            description = "To get user  by ID",
            tags = {"user-controller", "user", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = UserResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            )
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRoles('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                userMapper.userToUserResponse(
                        userService.findById(id)
                )
        );
    }

    @Operation(
            summary = "Create user",
            description = "To create user. " +
                    "Please pay your attention: user can be created by admin or automatically on starting this application only",
            tags = {"user-controller", "user", "create", "validation"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = {
                            @Content(schema = @Schema(implementation = UserResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "418",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            )
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UpsertUserRequest request) {
        User newUser = userService.save(userMapper.requstToUser(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.userToUserResponse(newUser));
    }

    @Operation(
            summary = "Update user by id",
            description = "To update user by ID. " +
                    "Please pay your attention: only owner can update user info.. or Admin",
            tags = {"user-controller", "user", "update", "validation"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = UserResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            )
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRoles('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    public ResponseEntity<UserResponse> update(@RequestBody @Valid UpsertUserRequest request,
                                               @PathVariable("id") Long clientId) {
        User updatedClient = userService.update(userMapper.requestToUser(clientId, request));
        return ResponseEntity.ok(
                userMapper.userToUserResponse(updatedClient)
        );
    }

    @Operation(
            summary = "Delete user by id",
            description = "To delete user  by ID. " +
                    "Please pay your attention: only Admin can delete some user. User \"admin\" cant be deleted ever",
            tags = {"user-controller", "user", "delete", "validation"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204"
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            )
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRoles('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
