package ru.example.fourth.web;

import ru.example.news.model.Comment;
import ru.example.news.model.News;
import ru.example.news.model.Topic;
import ru.example.news.model.User;
import ru.example.news.web.model.defaults.FindAllSettings;
import ru.example.news.web.model.news.NewsFilter;
import ru.example.news.web.model.news.SimpleNewsResponse;
import ru.example.news.web.model.user.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class AbstractTestController {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected FindAllSettings getFindAllSettings() {
        return FindAllSettings.builder()
                .pageNum(0)
                .pageSize(15)
                .filter(new NewsFilter())
                .build();
    }

    protected User createUser(Long id, News news, Comment comment) {
        User newUser = User.builder()
                .id(id)
                .username("user" + id)
                .email("email" + id + "@email.com")
                .news(new ArrayList<>())
                .comments(new ArrayList<>())
                .build();
        if (news != null) {
            newUser.getNews().add(news);
        }
        if (comment != null) {
            newUser.getComments().add(comment);
        }
        return newUser;
    }

    protected News createNews(Long id, User user, List<Comment> comments) {
        return News.builder()
                .id(id)
                .title("Title" + id)
                .body("Body" + id)
                .topic(Topic.builder().topic("Topic" + id).createAt(Instant.now()).id(id).build())
                .user(user)
                .comments(comments)
                .createAt(Instant.now())
                .updateAt(Instant.now())
                .build();
    }

    protected UserResponse createUserResponse(Long id) {
        return UserResponse.builder()
                .username("user" + id)
                .email("email" + id + "@email.com")
                .build();
    }

    protected SimpleNewsResponse createSimpleNewsResponse(Long id, int commentCount) {
        return SimpleNewsResponse.builder()
                .title("Title" + id)
                .body("Body" + id)
                .topic("Topic" + id)
                .commentCount(commentCount)
                .build();
    }
}
