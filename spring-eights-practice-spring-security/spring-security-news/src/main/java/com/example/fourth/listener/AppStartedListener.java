package com.example.fourth.listener;

import com.example.fourth.configuration.AppUserConfig;
import com.example.fourth.configuration.GenericDataConfig;
import com.example.fourth.model.Comment;
import com.example.fourth.model.News;
import com.example.fourth.model.User;
import com.example.fourth.service.CommentService;
import com.example.fourth.service.NewsService;
import com.example.fourth.service.TopicService;
import com.example.fourth.service.UserService;
import com.example.fourth.utils.JsonUtil;
import com.example.fourth.utils.model.JsonComments;
import com.example.fourth.utils.model.JsonNews;
import com.example.fourth.utils.model.JsonUser;
import com.example.fourth.utils.model.PreparedJsonData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.beans.BeanProperty;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("prod")
public class AppStartedListener {

    private final GenericDataConfig dataConfig;
    private final UserService userService;
    private final NewsService newsService;
    private final TopicService topicService;
    private final CommentService commentService;
    private final AppUserConfig appUserConfig;

    @EventListener(ApplicationStartedEvent.class)
    @Order(1)
    public void createAdminUserIfNotExists() {
        log.info("AppStartedListener -> createAdminUserIfNotExists was started");
        User admin;
        if (!userService.existsByName("admin")) {
            admin = userService.saveWithoutPrivilegeValidation(User.builder()
                            .name("admin")
                            .email("admin@admin.ru")
                            .build());
        } else {
            admin = userService.findByName("admin");
        }
        log.info("Admin ID {}", admin.getId());
    }


    @EventListener(ApplicationStartedEvent.class)
    @Order(2)
    public void uploadDataToDatabase() {
        if (dataConfig.isEnable() && isEmptyDatabase()) {
            log.info("AppStartedListener -> uploadDataToDatabase was started");
            PreparedJsonData data = JsonUtil.createDataFromResource(dataConfig);
            List<User> savedUsers = saveAllUsersFromPreparedList(data.getJsonUsers());
            List<News> savedNews = saveNewsAndTopics(savedUsers, data.getJsonNews());
            List<Comment> savedComments = saveComments(savedUsers, savedNews, data.getJsonComments());

            log.info("Was saved: -> {} users; -> {} news; -> {} topics; -> {} comments",
                    savedUsers.size(), savedNews.size(), topicService.count(), savedComments.size());
        }
    }

    @EventListener(ApplicationStartedEvent.class)
    @Order(3)
    public void uploadAppUserToDatabase() {
        log.info("AppStartedListener -> uploadAppUserToDatabase was started");
        if (!userService.existsByName(appUserConfig.getUserName())) {
            User savedUser = userService.saveWithoutPrivilegeValidation(User.builder()
                    .name(appUserConfig.getUserName())
                    .email("default")
                    .build());
            log.info("Saved app user: {}", savedUser);
        } else {
            log.info("User {} already exists", appUserConfig.getUserName());
        }
    }

    private List<Comment> saveComments(List<User> savedUsers,
                                       List<News> savedNews,
                                       List<JsonComments> jsonComments) {
        int multiplier = dataConfig.getMultiplier();
        List<Comment> savedComments = new ArrayList<>(jsonComments.size() * multiplier);
        Collections.shuffle(savedUsers);
        Collections.shuffle(savedNews);
        Collections.shuffle(jsonComments);

        int maxUsers = savedUsers.size();
        int maxNews = savedNews.size();
        int min = 1;
        Random rn = new Random();

        for (int i = 0; i < multiplier; i++) {
            savedComments.addAll(jsonComments.stream()
                    .map(c -> commentService.save(Comment.builder()
                            .comment(c.getComment())
                            .user(savedUsers.get(rn.nextInt(maxUsers - min + 1)))
                            .news(savedNews.get(rn.nextInt(maxNews - min + 1)))
                            .build()))
                    .toList());
        }

        return savedComments;
    }


    private boolean isEmptyDatabase() {
        return (newsService.count() + topicService.count() + commentService.count()) == 0;
    }

    private List<User> saveAllUsersFromPreparedList(List<JsonUser> jsonUserList) {
        List<User> users = jsonUserList.stream()
                .map(u -> User.builder().name(u.getName()).email(u.getEmail()).build())
                .toList();
        return userService.saveAll(users);
    }

    private List<News> saveNewsAndTopics(List<User> savedUsers, List<JsonNews> jsonNewsList) {
        Collections.shuffle(savedUsers);
        List<News> savedNews = new ArrayList<>(jsonNewsList.size());
        int newsCount = jsonNewsList.size();
        int newsPerOneUser =  newsCount / savedUsers.size();
        int start = 0;
        for (User user : savedUsers) {
            savedNews.addAll(saveNewsWithLinkToUserAndTopics(user,
                    jsonNewsList.subList(start, Math.min(start + newsPerOneUser, jsonNewsList.size())))
            );
            start += newsPerOneUser;
        }
        return savedNews;
    }

    private List<News> saveNewsWithLinkToUserAndTopics(User user, List<JsonNews> jsonNews) {
        return jsonNews.stream()
                .map(n -> newsService.save(News.builder()
                        .topic(topicService.getOrCreateTopic(n.getTopic(), true))
                        .body(n.getBody())
                        .user(user)
                        .title(n.getTitle())
                        .build())
                ).collect(Collectors.toList());
    }

}
