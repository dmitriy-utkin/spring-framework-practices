package ru.example.news.web.model.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleCommentListResponse {

    @Builder.Default
    List<SimpleCommentResponse> comments = new ArrayList<>();
}
