package com.example.fourth.web.model.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpsertCommentRequest {

    @NotNull(message = "News ID for comment should be field")
    private Long newsId;

    @JsonIgnore
    private Long userId;

    @NotBlank(message = "Comment should be field")
    @Size(min = 20, max = 300, message = "News body length should be between 20 and 300 characters")
    private String comment;
}
