package ru.example.news.mapper;

import ru.example.news.model.Comment;
import ru.example.news.service.CommentService;
import ru.example.news.service.NewsService;
import ru.example.news.service.UserService;
import ru.example.news.web.model.comment.CommentResponse;
import ru.example.news.web.model.comment.UpsertCommentRequest;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CommentMapperDelegate implements CommentMapper {

    @Autowired
    private NewsService newsService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Override
    public Comment requestToComment(UpsertCommentRequest request) {
        return Comment.builder()
                .comment(request.getComment())
                .news(newsService.findById(request.getNewsId()))
                .build();
    }

    @Override
    public Comment requestToComment(Long id, UpsertCommentRequest request) {
        Comment comment = requestToComment(request);
        comment.setId(id);
        return comment;
    }

    @Override
    public CommentResponse commentToCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .comment(comment.getComment())
                .newsId(comment.getNews().getId())
                .userId(comment.getUser().getId())
                .build();
    }
}
