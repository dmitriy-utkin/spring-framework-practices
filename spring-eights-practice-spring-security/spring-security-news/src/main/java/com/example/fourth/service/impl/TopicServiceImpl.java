package com.example.fourth.service.impl;

import com.example.fourth.aop.IdLogger;
import com.example.fourth.aop.PrivilegeValidation;
import com.example.fourth.exception.EntityNotFoundException;
import com.example.fourth.model.Topic;
import com.example.fourth.repository.TopicRepository;
import com.example.fourth.service.TopicService;
import com.example.fourth.utils.BeanUtils;
import com.example.fourth.web.model.defaults.FindAllSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

    @Override
    public List<Topic> findAll(FindAllSettings findAllSettings) {
        return topicRepository.findAll(PageRequest.of(findAllSettings.getPageNum(), findAllSettings.getPageSize())).getContent();
    }

    @Override
    @IdLogger
    public Topic findById(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Topic with ID " + id + " not found"));
    }

    @Override
    @IdLogger
    public Topic save(Topic topic) {
        return topicRepository.save(topic);
    }

    @Override
    @IdLogger
    @PrivilegeValidation
    public Topic update(Topic topic) {
        Topic existedTopic = findById(topic.getId());
        BeanUtils.copyNonNullProperties(topic, existedTopic);
        return topicRepository.save(existedTopic);
    }

    @Override
    @PrivilegeValidation
    public void deleteById(Long id) {
        if (!topicRepository.existsById(id)) {
            throw new EntityNotFoundException("Topic with ID " + id + " not found");
        }
        topicRepository.deleteById(id);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        topicRepository.deleteAllById(ids);
    }

    @Override
    public Long count() {
        return topicRepository.count();
    }

    @Override
    public boolean existsByTopic(String topic) {
        return topicRepository.existsByTopic(topic);
    }

    @Override
    public Topic findByTopic(String topic) {
        return topicRepository.findByTopic(topic).orElse(null);
    }

    @Override
    public Topic getOrCreateTopic(String topic, boolean withNullOption) {
        if (withNullOption && topic == null) { topic = "default"; }
        topic = topic.toLowerCase(Locale.ROOT);
        Topic existedTopic = findByTopic(topic);
        if (existedTopic == null) {
            return save(Topic.builder().topic(topic).build());
        }
        return existedTopic;
    }
}
