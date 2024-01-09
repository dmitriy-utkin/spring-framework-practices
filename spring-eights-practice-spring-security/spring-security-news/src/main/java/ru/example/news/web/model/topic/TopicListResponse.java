package ru.example.news.web.model.topic;

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
public class TopicListResponse {

    @Builder.Default
    List<TopicResponse> topics = new ArrayList<>();
}
