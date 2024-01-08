package com.example.fourth.web.model.news;

import com.example.fourth.web.model.comment.SimpleCommentListResponse;
import com.example.fourth.web.model.comment.SimpleCommentResponse;
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
