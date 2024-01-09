package ru.example.news.mapper;

import ru.example.news.model.Topic;
import ru.example.news.web.model.topic.SimpleTopicResponse;
import ru.example.news.web.model.topic.TopicListResponse;
import ru.example.news.web.model.topic.TopicResponse;
import ru.example.news.web.model.topic.UpsertTopicRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TopicMapper {

    Topic topicStrToTopic(String topic);

    String topicToTopicStr(Topic topic);

    Topic requestToTopic(UpsertTopicRequest request);

    Topic requsetToTopic(Long id, UpsertTopicRequest request);

    SimpleTopicResponse topicToSimpleTopicResponse(Topic topic);

    default TopicResponse topicToTopicResponse(Topic topic) {
        return TopicResponse.builder()
                .topic(topic.getTopic())
                .newsCount(topic.getNews().size())
                .build();
    }

    default TopicListResponse topicListToTopicListResponse(List<Topic> topics) {
        return TopicListResponse.builder()
                .topics(topics.stream().map(this::topicToTopicResponse).toList())
                .build();
    }
}
