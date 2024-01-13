package ru.example.news.mapper;

import ru.example.news.model.News;
import ru.example.news.model.Topic;
import ru.example.news.service.CommentService;
import ru.example.news.service.NewsService;
import ru.example.news.service.TopicService;
import ru.example.news.service.UserService;
import ru.example.news.web.model.news.NewsResponse;
import ru.example.news.web.model.news.SimpleNewsResponse;
import ru.example.news.web.model.news.UpsertNewsRequest;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class NewsMapperDelegate implements NewsMapper {

    @Autowired
    private UserService userService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public News requestToNews(UpsertNewsRequest request) {
        Topic topic;
        if (topicService.existsByTopic(request.getTopic())) {
            topic = topicService.findByTopic(request.getTopic());
        } else {
            topic = topicService.save(Topic.builder().topic(request.getTopic()).build());
        }
        return News.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .topic(topic)
                .build();
    }

    @Override
    public News requestToNews(Long id, UpsertNewsRequest request) {
        News news = requestToNews(request);
        news.setId(id);
        return news;
    }

    @Override
    public NewsResponse newsToNewsResponse(News news) {
        return NewsResponse.builder()
                .id(news.getId())
                .topic(news.getTopic().getTopic())
                .title(news.getTitle())
                .body(news.getBody())
                .user(userMapper.userToUserResponse(news.getUser()))
                .comments(
                        news.getComments().stream()
                        .map(c -> commentMapper.commentToSimpleCommentResponse(c)).toList()
                )
                .build();
    }

    @Override
    public SimpleNewsResponse newsToSimpleNewsResponse(News news) {
        return SimpleNewsResponse.builder()
                .id(news.getId())
                .title(news.getTitle())
                .body(news.getBody())
                .topic(news.getTopic().getTopic())
                .userId(news.getUser().getId())
                .commentCount(Math.toIntExact(commentService.countByNews(news)))
                .build();
    }
}
