package ru.example.news.utils;

import ru.example.news.aop.RequestInfo;
import ru.example.news.exception.IllegalArguments;
import ru.example.news.model.Comment;
import ru.example.news.model.News;
import ru.example.news.model.Topic;
import ru.example.news.model.User;
import ru.example.news.service.CommentService;
import ru.example.news.service.NewsService;
import ru.example.news.service.UserService;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Arrays;

@UtilityClass
@Slf4j
public class AspectUtils {

    @SneakyThrows
    public Long getEntityId(Object object) {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equals(News.Fields.id)
                    || field.getName().equals(Comment.Fields.id)
                    || field.getName().equals(User.Fields.id)
                    || field.getName().equals(Topic.Fields.id)) {
                return (Long) field.get(object);
            }
        }
        return null;
    }

    @SneakyThrows
    public Long getOwnerId(RequestInfo requestInfo, NewsService newsService, CommentService commentService, UserService userService) {

        Object object = Arrays.stream(requestInfo.getArgs()).findFirst().orElse(null);

        if (object == null) {
            throw new IllegalArguments("Found illegal args");
        }

        if (requestInfo.getRequestedUri().contains("topic")) {
            return requestInfo.getUserId();
        }

        if (requestInfo.getRequestedUri().contains("user") && requestInfo.getMethod().contains("save")) {
            return requestInfo.getUserId();
        }

        if (requestInfo.getRequestedUri().contains("user") && requestInfo.getMethod().contains("update")) {
            return getOwnerIdForUpdateUser(object);
        }

        if (requestInfo.getRequestedUri().contains("user") && requestInfo.getMethod().contains("delete")) {
            return (Long) object;
        }

        if (requestInfo.getMethod().contains("update")) {
            return getUserIdForUpdate(object);
        }
        if (requestInfo.getRequestedUri().contains("news")) {
            return getOwnerIdForDeleteNews((Long) object, newsService);
        }

        if (requestInfo.getRequestedUri().contains("comment")) {
            return getOwnerIdForDeleteComment((Long) object, commentService);
        }

        return null;
    }

    private static Long getOwnerIdForUpdateUser(Object object) throws IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equals(User.Fields.id)) {
                return (Long) field.get(object);
            }
        }
        return null;
    }

    private Long getUserIdForUpdate(Object object) throws IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equals(News.Fields.user) || field.getName().equals(Comment.Fields.user)) {
                User user = (User) field.get(object);
                return user.getId();
            }
        }
        return null;
    }

    private Long getOwnerIdForDeleteNews(Long id, NewsService newsService) throws RuntimeException {
        return newsService.findById(id).getUser().getId();
    }

    private Long getOwnerIdForDeleteComment(Long id, CommentService commentService) throws RuntimeException {
        return commentService.findById(id).getUser().getId();
    }

}
