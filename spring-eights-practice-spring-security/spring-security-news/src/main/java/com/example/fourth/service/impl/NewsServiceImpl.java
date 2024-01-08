package com.example.fourth.service.impl;

import com.example.fourth.aop.IdLogger;
import com.example.fourth.aop.PrivilegeValidation;
import com.example.fourth.exception.EntityNotFoundException;
import com.example.fourth.model.News;
import com.example.fourth.repository.NewsRepository;
import com.example.fourth.repository.NewsSpecification;
import com.example.fourth.service.NewsService;
import com.example.fourth.utils.BeanUtils;
import com.example.fourth.web.model.defaults.FindAllSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    @Override
    public List<News> findAll(FindAllSettings findAllSettings) {
        return newsRepository.findAll(NewsSpecification.withFilter(findAllSettings.getFilter()),
                PageRequest.of(findAllSettings.getPageNum(), findAllSettings.getPageSize())).getContent();
    }

    @Override
    @IdLogger
    public News findById(Long id) {
        return newsRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("News with ID " + id + " not found"));
    }

    @Override
    @IdLogger
    public News save(News news) {
        return newsRepository.save(news);
    }

    @Override
    @PrivilegeValidation
    @IdLogger
    public News update(News news) {
        News existedNews = findById(news.getId());
        BeanUtils.copyNonNullProperties(news, existedNews);
        return newsRepository.save(existedNews);
    }

    @Override
    @PrivilegeValidation
    public void deleteById(Long id) {
        if (!newsRepository.existsById(id)) {
            throw new EntityNotFoundException("News with ID " + id + " not found");
        }
        newsRepository.deleteById(id);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        newsRepository.deleteAllById(ids);
    }

    @Override
    public Long count() {
        return newsRepository.count();
    }
}
