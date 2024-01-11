package ru.example.news.listener;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.example.news.configuration.GenericDataConfig;
import ru.example.news.model.*;
import ru.example.news.repository.CommentRepository;
import ru.example.news.repository.NewsRepository;
import ru.example.news.repository.TopicRepository;
import ru.example.news.repository.UserRepository;
import ru.example.news.utils.JsonUtil;
import ru.example.news.utils.model.JsonComments;
import ru.example.news.utils.model.JsonNews;
import ru.example.news.utils.model.JsonUser;
import ru.example.news.utils.model.PreparedJsonData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(prefix = "app.generic-data", name = "enable", havingValue = "true")
public class AppStartedListener {

    private final GenericDataConfig dataConfig;
    private final UserRepository userRepository;
    private final NewsRepository newsRepository;
    private final CommentRepository commentRepository;
    private final TopicRepository topicRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationStartedEvent.class)
    @Order(1)
    public void uploadDataToDatabase() {
        if (dataConfig.isEnable() && isEmptyDatabase()) {
            log.info("AppStartedListener -> uploadDataToDatabase was started");
            PreparedJsonData data = JsonUtil.createDataFromResource(dataConfig);
            List<User> savedUsers = saveAllUsersFromPreparedList(data.getJsonUsers());
            List<News> savedNews = saveNewsAndTopics(savedUsers, data.getJsonNews());
            List<Comment> savedComments = saveComments(savedUsers, savedNews, data.getJsonComments());

            log.info("Was saved: -> {} users; -> {} news; -> {} topics; -> {} comments",
                    savedUsers.size(), savedNews.size(), topicRepository.count(), savedComments.size());
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
                    .map(c -> commentRepository.save(Comment.builder()
                            .comment(c.getComment())
                            .user(savedUsers.get(rn.nextInt(maxUsers - min + 1)))
                            .news(savedNews.get(rn.nextInt(maxNews - min + 1)))
                            .build()))
                    .toList());
        }

        return savedComments;
    }


    private boolean isEmptyDatabase() {
        return (newsRepository.count() + topicRepository.count() + commentRepository.count()) == 0;
    }

    private List<User> saveAllUsersFromPreparedList(List<JsonUser> jsonUserList) {
        List<User> users = jsonUserList.stream()
                .map(u -> User.builder()
                        .username(u.getName())
                        .email(u.getEmail())
                        .password(passwordEncoder.encode("pass"))
                        .roles(getRandomRole())
                        .build()
                ).toList();
        return userRepository.saveAll(users);
    }

    private Set<RoleType> getRandomRole() {
        List<RoleType> roles = new ArrayList<>(List.of(
                RoleType.ROLE_USER,
                RoleType.ROLE_ADMIN,
                RoleType.ROLE_MODERATOR
        ));
        Collections.shuffle(roles);
        return Collections.singleton(roles.get(0));
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
                .map(n -> {
                    Topic topic;
                    if (topicRepository.existsByTopic(n.getTopic())) {
                        topic = topicRepository.findByTopic(n.getTopic()).get();
                    } else {
                        topic = topicRepository.save(Topic.builder().topic(n.getTopic()).build());
                    }
                    return newsRepository.save(News.builder()
                                    .topic(topic)
                                    .body(n.getBody())
                                    .user(user)
                                    .title(n.getTitle())
                                    .build());
                    }
                ).collect(Collectors.toList());
    }
}