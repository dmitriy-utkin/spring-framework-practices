package com.example.fourth.web.controller;

import com.example.fourth.mapper.NewsMapper;
import com.example.fourth.model.News;
import com.example.fourth.service.NewsService;
import com.example.fourth.web.model.defaults.ErrorResponse;
import com.example.fourth.web.model.defaults.FindAllSettings;
import com.example.fourth.web.model.news.NewsFilter;
import com.example.fourth.web.model.news.SimpleNewsListResponse;
import com.example.fourth.web.model.news.NewsResponse;
import com.example.fourth.web.model.news.UpsertNewsRequest;
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
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;
    private final NewsMapper newsMapper;

    @Operation(
            summary = "Get all news",
            description = "To get all news with pagination (default value for page size is 10). " +
                    "Also you can set the userId/userName, topicId/topicName",
            tags = {"news-controller", "news"}
    )
    @ApiResponse(
            responseCode = "200",
            content = {
                    @Content(schema = @Schema(implementation = SimpleNewsListResponse.class), mediaType = "application/json")
            }
    )
    @GetMapping
    public ResponseEntity<SimpleNewsListResponse> findAll(@RequestBody @Valid FindAllSettings findAllSettings) {
        return ResponseEntity.ok(
                newsMapper.newsListToSimpleNewsListResponse(
                        newsService.findAll(findAllSettings)
                )
        );
    }

    @Operation(
            summary = "Get news by id",
            description = "To get news by ID",
            tags = {"news-controller", "news", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = NewsResponse.class), mediaType = "application/json")
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
    public ResponseEntity<NewsResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                newsMapper.newsToNewsResponse(
                        newsService.findById(id)
                )
        );
    }

    @Operation(
            summary = "Create news",
            description = "To create news.",
            tags = {"news-controller", "news", "create"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = {
                            @Content(schema = @Schema(implementation = UserResponse.class), mediaType = "application/json")
                    }
            )
    })
    @PostMapping
    public ResponseEntity<NewsResponse> create(@RequestBody @Valid UpsertNewsRequest request) {
        News newNews = newsService.save(newsMapper.requestToNews(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                newsMapper.newsToNewsResponse(newNews)
        );
    }

    @Operation(
            summary = "Update news by id",
            description = "To update news by ID. " +
                    "Please pay your attention: only owner can update news info.. or Admin",
            tags = {"news-controller", "news", "update", "validation"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = NewsResponse.class), mediaType = "application/json")
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
    public ResponseEntity<NewsResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody UpsertNewsRequest request) {
        News updatedNews = newsService.update(newsMapper.requestToNews(id, request));
        return ResponseEntity.ok(
                newsMapper.newsToNewsResponse(updatedNews)
        );
    }

    @Operation(
            summary = "Delete news by id",
            description = "To delete news by ID. " +
                    "Please pay your attention: only Admin can delete some news.",
            tags = {"news-controller", "news", "delete", "validation"}
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
        newsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
