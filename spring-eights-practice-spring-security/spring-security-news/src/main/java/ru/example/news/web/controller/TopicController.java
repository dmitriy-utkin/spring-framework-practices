package ru.example.news.web.controller;

import ru.example.news.mapper.TopicMapper;
import ru.example.news.model.Topic;
import ru.example.news.service.TopicService;
import ru.example.news.web.model.defaults.ErrorResponse;
import ru.example.news.web.model.defaults.FindAllSettings;
import ru.example.news.web.model.topic.SimpleTopicResponse;
import ru.example.news.web.model.topic.TopicListResponse;
import ru.example.news.web.model.topic.TopicResponse;
import ru.example.news.web.model.topic.UpsertTopicRequest;
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
@RequestMapping("/api/topic")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;
    private final TopicMapper topicMapper;

    @Operation(
            summary = "Get all topics",
            description = "To get all topics with pagination (default value for page size is 10).",
            tags = {"topic-controller", "topic"}
    )
    @ApiResponse(
            responseCode = "200",
            content = {
                    @Content(schema = @Schema(implementation = TopicListResponse.class), mediaType = "application/json")
            }
    )
    @GetMapping
    public ResponseEntity<TopicListResponse> findAll(@RequestBody @Valid FindAllSettings findAllSettings) {
        return ResponseEntity.ok(
                topicMapper.topicListToTopicListResponse(
                        topicService.findAll(findAllSettings)
                )
        );
    }

    @Operation(
            summary = "Get topic by id",
            description = "To get topic  by ID",
            tags = {"topic-controller", "topic", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = TopicResponse.class), mediaType = "application/json")
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
    public ResponseEntity<TopicResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                topicMapper.topicToTopicResponse(
                        topicService.findById(id)
                )
        );
    }

    @Operation(
            summary = "Create topic",
            description = "To create topic",
            tags = {"topic-controller", "topic", "create"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = {
                            @Content(schema = @Schema(implementation = SimpleTopicResponse.class), mediaType = "application/json")
                    }
            )
    })
    @PostMapping
    public ResponseEntity<SimpleTopicResponse> create(@RequestBody @Valid UpsertTopicRequest request) {
        Topic newTopic = topicService.save(topicMapper.requestToTopic(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                topicMapper.topicToSimpleTopicResponse(newTopic)
        );
    }

    @Operation(
            summary = "Update topic by id",
            description = "To update topic by ID. " +
                    "Please pay your attention: only Admin can update it",
            tags = {"topic-controller", "topic", "update", "validation"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = SimpleTopicResponse.class), mediaType = "application/json")
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
    public ResponseEntity<SimpleTopicResponse> update(@PathVariable Long id,
                                                @Valid @RequestBody UpsertTopicRequest request) {
        Topic updatedTopic = topicService.update(topicMapper.requsetToTopic(id, request));
        return ResponseEntity.ok(topicMapper.topicToSimpleTopicResponse(updatedTopic));
    }

    @Operation(
            summary = "Delete topic by id",
            description = "To delete topic by ID. " +
                    "Please pay your attention: only Admin can delete some topic.",
            tags = {"topic-controller", "topic", "delete", "validation"}
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
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        topicService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
