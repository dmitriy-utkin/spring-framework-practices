package com.example.fourth.web.controller;

import com.example.fourth.mapper.CommentMapper;
import com.example.fourth.model.Comment;
import com.example.fourth.service.CommentService;
import com.example.fourth.web.model.comment.*;
import com.example.fourth.web.model.defaults.ErrorResponse;
import com.example.fourth.web.model.defaults.FindAllSettings;
import com.example.fourth.web.model.user.UserListResponse;
import com.example.fourth.web.model.user.UserResponse;
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
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @Operation(
            summary = "Get all comment",
            description = "To get all comments",
            tags = {"comment-controller", "comment"}
    )
    @ApiResponse(
            responseCode = "200",
            content = {
                    @Content(schema = @Schema(implementation = SimpleCommentListResponse.class), mediaType = "application/json")
            }
    )
    @GetMapping
    public ResponseEntity<SimpleCommentListResponse> findAll(@RequestBody @Valid FindAllSettings findAllSettings) {
        return ResponseEntity.ok(
                commentMapper.commentListToSimpleCommentListResponse(
                        commentService.findAll(findAllSettings)
                )
        );
    }

    @Operation(
            summary = "Get comment by id",
            description = "To get comment by ID",
            tags = {"comment-controller", "comment", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = CommentResponse.class), mediaType = "application/json")
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
    public ResponseEntity<CommentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                commentMapper.commentToCommentResponse(
                        commentService.findById(id)
                )
        );
    }

    @Operation(
            summary = "Create comment",
            description = "To create comment.",
            tags = {"comment-controller", "comment", "create"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = {
                            @Content(schema = @Schema(implementation = CommentResponse.class), mediaType = "application/json")
                    }
            )
    })
    @PostMapping
    public ResponseEntity<CommentResponse> create(@Valid @RequestBody UpsertCommentRequest request) {
        Comment newComment = commentService.save(commentMapper.requestToComment(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                commentMapper.commentToCommentResponse(newComment)
        );
    }

    @Operation(
            summary = "Update comment by id",
            description = "To update comment by ID. " +
                    "Please pay your attention: only owner can update comment info.. or Admin",
            tags = {"comment-controller", "comment", "update", "validation"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = CommentResponse.class), mediaType = "application/json")
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
    public ResponseEntity<CommentResponse> update(@PathVariable Long id,
                                                        @Valid @RequestBody UpsertCommentRequest request) {
        Comment updatedComment = commentService.update(commentMapper.requestToComment(id, request));
        return ResponseEntity.ok(commentMapper.commentToCommentResponse(updatedComment));
    }

    @Operation(
            summary = "Delete comment by id",
            description = "To delete comment by ID. " +
                    "Please pay your attention: only Admin can delete some comment.",
            tags = {"comment-controller", "comment", "delete", "validation"}
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
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
