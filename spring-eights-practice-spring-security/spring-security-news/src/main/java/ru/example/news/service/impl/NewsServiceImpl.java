package ru.example.news.service.impl;

import ru.example.news.aop.PrivilegeValidation;
import ru.example.news.aop.ValidationType;
import ru.example.news.exception.EntityNotFoundException;
import ru.example.news.model.News;
import ru.example.news.repository.NewsRepository;
import ru.example.news.repository.NewsSpecification;
import ru.example.news.service.NewsService;
import ru.example.news.service.UserService;
import ru.example.news.utils.BeanUtils;
import ru.example.news.web.model.defaults.FindAllSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final UserService userService;

    @Override
    public List<News> findAll(FindAllSettings findAllSettings) {
        return newsRepository.findAll(NewsSpecification.withFilter(findAllSettings.getFilter()),
                PageRequest.of(findAllSettings.getPageNum(), findAllSettings.getPageSize())).getContent();
    }

    @Override
    public List<News> findAll() {
        return newsRepository.findAll();
    }

    @Override
    public News findById(Long id) {
        return newsRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("News with ID " + id + " not found"));
    }

    @Override
    public News save(News news, String username) {
        news.setUser(userService.findByUsername(username));
        return newsRepository.save(news);
    }

    @Override
    @PrivilegeValidation(type = ValidationType.NEWS_UPDATE)
    public News update(News news) {
        News existedNews = findById(news.getId());
        BeanUtils.copyNonNullProperties(news, existedNews);
        return newsRepository.save(existedNews);
    }

    @Override
    @PrivilegeValidation(type = ValidationType.NEWS_DELETE)
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
