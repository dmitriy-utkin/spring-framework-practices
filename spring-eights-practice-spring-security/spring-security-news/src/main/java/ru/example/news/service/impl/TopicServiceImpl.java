package ru.example.news.service.impl;

import ru.example.news.aop.PrivilegeValidation;
import ru.example.news.aop.ValidationType;
import ru.example.news.exception.EntityNotFoundException;
import ru.example.news.model.Topic;
import ru.example.news.repository.TopicRepository;
import ru.example.news.service.TopicService;
import ru.example.news.utils.BeanUtils;
import ru.example.news.web.model.defaults.FindAllSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

    @Override
    public List<Topic> findAll(FindAllSettings findAllSettings) {
        return topicRepository.findAll(PageRequest.of(findAllSettings.getPageNum(), findAllSettings.getPageSize())).getContent();
    }

    @Override
    public List<Topic> findAll() {
        return topicRepository.findAll();
    }

    @Override
    public Topic findById(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Topic with ID " + id + " not found"));
    }

    @Override
    @PrivilegeValidation(type = ValidationType.TOPIC_SAVE)
    public Topic save(Topic topic) {
        return topicRepository.save(topic);
    }

    @Override
    public Topic update(Topic topic) {
        Topic existedTopic = findById(topic.getId());
        BeanUtils.copyNonNullProperties(topic, existedTopic);
        return topicRepository.save(existedTopic);
    }

    @Override
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

}
