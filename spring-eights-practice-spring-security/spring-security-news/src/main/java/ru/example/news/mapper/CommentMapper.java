package ru.example.news.mapper;

import ru.example.news.model.Comment;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.example.news.web.model.comment.*;

import java.util.List;

@DecoratedWith(CommentMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    Comment requestToComment(UpsertCommentRequest request, String username);

    Comment requestToComment(Long id, UpsertCommentRequest request, String username);

    SimpleCommentResponse commentToSimpleCommentResponse(Comment comment);

    default SimpleCommentListResponse commentListToSimpleCommentListResponse(List<Comment> comments) {
        return SimpleCommentListResponse.builder()
                .comments(comments.stream().map(this::commentToSimpleCommentResponse).toList())
                .build();
    }

    CommentResponse commentToCommentResponse(Comment comment);

    default CommentListResponse commentListToCommentListResponse(List<Comment> comments) {
        return CommentListResponse.builder()
                .comments(comments.stream().map(this::commentToCommentResponse).toList())
                .build();
    }
}
