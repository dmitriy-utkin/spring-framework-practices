package com.example.fourth.repository;

import com.example.fourth.model.News;
import com.example.fourth.model.Topic;
import com.example.fourth.model.User;
import com.example.fourth.web.model.news.NewsFilter;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public interface NewsSpecification {

    static Specification<News> withFilter(NewsFilter newsFilter) {
        return Specification.where(byUserId(newsFilter.getUserId()))
                .and(byTopicId(newsFilter.getTopicId()))
                .and(byUserName(newsFilter.getUserName()))
                .and(byTopic(newsFilter.getTopic()));
    }

    static Specification<News> byTopic(String topic) {
        return (root, query, cb) -> {
            if (topic == null) {
                return null;
            }
            return cb.equal(root.get(News.Fields.topic).get(Topic.Fields.topic), topic.toLowerCase(Locale.ROOT));
        };
    }


    static Specification<News> byUserName(String authorName) {
        return (root, query, cb) -> {
            if (authorName == null) {
                return null;
            }
            return cb.equal(root.get(News.Fields.user).get(User.Fields.name), authorName);
        };
    }



    static Specification<News> byTopicId(Long topicId) {
        return (root, query, cb) -> {
            if (topicId == null) {
                return null;
            }
            return cb.equal(root.get(News.Fields.topic).get(Topic.Fields.id), topicId);
        };
    }

    static Specification<News> byUserId(Long userId) {
        return (root, query, cb) -> {
            if (userId == null) {
                return null;
            }
            return cb.equal(root.get(News.Fields.user).get(User.Fields.id), userId);
        };
    }


}
