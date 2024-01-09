package ru.example.news.web.model.news;

import ru.example.news.web.model.comment.SimpleCommentResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsResponse {
    private String title;
    private String body;
    private String topic;
    private List<SimpleCommentResponse> comments;
}
