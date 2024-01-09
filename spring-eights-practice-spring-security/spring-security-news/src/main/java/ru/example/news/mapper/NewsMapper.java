package ru.example.news.mapper;

import ru.example.news.model.News;
import ru.example.news.web.model.news.NewsResponse;
import ru.example.news.web.model.news.SimpleNewsListResponse;
import ru.example.news.web.model.news.SimpleNewsResponse;
import ru.example.news.web.model.news.UpsertNewsRequest;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@DecoratedWith(NewsMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,  uses = {CommentMapper.class,
        TopicMapper.class})
public interface NewsMapper {

    News requestToNews(UpsertNewsRequest request);

    News requestToNews(Long id, UpsertNewsRequest request);

    NewsResponse newsToNewsResponse(News news);

    SimpleNewsResponse newsToSimpleNewsResponse(News news);

    default SimpleNewsListResponse newsListToSimpleNewsListResponse(List<News> news) {
        return SimpleNewsListResponse.builder()
                .news(news.stream().map(this::newsToSimpleNewsResponse).toList())
                .build();
    }
}
