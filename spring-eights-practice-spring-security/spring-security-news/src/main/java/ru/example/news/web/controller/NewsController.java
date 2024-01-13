package ru.example.news.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import ru.example.news.mapper.NewsMapper;
import ru.example.news.model.News;
import ru.example.news.service.NewsService;
import ru.example.news.web.model.defaults.ErrorResponse;
import ru.example.news.web.model.defaults.FindAllSettings;
import ru.example.news.web.model.news.SimpleNewsListResponse;
import ru.example.news.web.model.news.NewsResponse;
import ru.example.news.web.model.news.UpsertNewsRequest;
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
    @RequestMapping("/filter")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    public ResponseEntity<SimpleNewsListResponse> findAllWithFilter(@RequestBody @Valid FindAllSettings findAllSettings) {
        return ResponseEntity.ok(
                newsMapper.newsListToSimpleNewsListResponse(
                        newsService.findAll(findAllSettings)
                )
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    public ResponseEntity<SimpleNewsListResponse> findAll() {
        return ResponseEntity.ok(
                newsMapper.newsListToSimpleNewsListResponse(
                        newsService.findAll()
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    public ResponseEntity<NewsResponse> create(@AuthenticationPrincipal UserDetails userDetails,
                                               @RequestBody @Valid UpsertNewsRequest request) {
        News newNews = newsService.save(newsMapper.requestToNews(request), userDetails.getUsername());
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        newsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
