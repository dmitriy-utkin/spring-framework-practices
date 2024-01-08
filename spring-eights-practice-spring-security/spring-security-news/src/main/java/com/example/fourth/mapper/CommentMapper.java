package com.example.fourth.mapper;

import com.example.fourth.model.Comment;
import com.example.fourth.web.model.comment.*;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@DecoratedWith(CommentMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    Comment requestToComment(UpsertCommentRequest request);

    Comment requestToComment(Long id, UpsertCommentRequest request);

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
