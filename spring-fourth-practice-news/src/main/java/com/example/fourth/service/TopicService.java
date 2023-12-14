package com.example.fourth.service;

import com.example.fourth.model.Topic;
import com.example.fourth.web.model.defaults.FindAllSettings;

import java.util.List;

public interface TopicService {

    List<Topic> findAll(FindAllSettings findAllSettings);
    Topic findById(Long id);
    Topic save(Topic topic);
    Topic update(Topic topic);
    void deleteById(Long id);
    void deleteByIds(List<Long> ids);
    Long count();
    boolean existsByTopic(String topic);
    Topic findByTopic(String topic);
    Topic getOrCreateTopic(String topic, boolean withNullOption);
}
