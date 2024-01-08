package com.example.fourth.mapper;

import com.example.fourth.configuration.AppUserConfig;
import com.example.fourth.model.News;
import com.example.fourth.model.User;
import com.example.fourth.service.CommentService;
import com.example.fourth.service.NewsService;
import com.example.fourth.service.TopicService;
import com.example.fourth.service.UserService;
import com.example.fourth.web.model.news.NewsResponse;
import com.example.fourth.web.model.news.SimpleNewsResponse;
import com.example.fourth.web.model.news.UpsertNewsRequest;
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
    private AppUserConfig appUserConfig;

    @Autowired
    private CommentService commentService;

    @Override
    public News requestToNews(UpsertNewsRequest request) {
        User user = request.getUserId() == null ? userService.findByName(appUserConfig.getUserName()) :
                userService.findById(request.getUserId());
        return News.builder()
                .title(appUserConfig.getUserName().equals("admin") ? "(Upd by admin) " + request.getTitle() : request.getTitle())
                .body(request.getBody())
                .user(user)
                .topic(topicService.getOrCreateTopic(request.getTopic(), true))
                .build();
    }

    @Override
    public News requestToNews(Long id, UpsertNewsRequest request) {
        request.setUserId(newsService.findById(id).getUser().getId());
        News news = requestToNews(request);
        news.setId(id);
        return news;
    }

    @Override
    public NewsResponse newsToNewsResponse(News news) {
        return NewsResponse.builder()
                .topic(news.getTopic().getTopic())
                .title(news.getTitle())
                .body(news.getBody())
                .comments(
                        news.getComments().stream()
                        .map(c -> commentMapper.commentToSimpleCommentResponse(c)).toList()
                )
                .build();
    }

    @Override
    public SimpleNewsResponse newsToSimpleNewsResponse(News news) {
        return SimpleNewsResponse.builder()
                .title(news.getTitle())
                .body(news.getBody())
                .topic(news.getTopic().getTopic())
                .commentCount(Math.toIntExact(commentService.countByNews(news)))
                .build();
    }
}
