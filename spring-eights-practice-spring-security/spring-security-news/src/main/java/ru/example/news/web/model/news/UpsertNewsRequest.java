package ru.example.news.web.model.news;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpsertNewsRequest {

    @NotBlank(message = "Title should be field")
    @Size(min = 10, max = 100, message = "News title length should be between 10 and 100 characters")
    private String title;

    @NotBlank(message = "Body should be field")
    @Size(min = 100, max = 600, message = "News body length should be between 100 and 600 characters")
    private String body;

    @NotBlank(message = "News topic should be field")
    @Size(min = 2, max = 40, message = "News topic length should be between 2 and 40 characters")
    private String topic;

    @JsonIgnore
    private Long userId;
}
