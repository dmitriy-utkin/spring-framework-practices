package ru.example.news.web.model.news;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsFilter {

    private Long topicId;

    private Long userId;

    private String userName;

    private String topic;
}
