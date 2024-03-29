package ru.example.news.web.model.news;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleNewsResponse {

    private Long id;
    private String title;
    private String body;
    private String topic;
    private Long userId;
    private int commentCount;
}
