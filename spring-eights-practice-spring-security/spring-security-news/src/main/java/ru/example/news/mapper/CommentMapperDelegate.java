package ru.example.news.mapper;

import ru.example.news.configuration.AppUserConfig;
import ru.example.news.model.Comment;
import ru.example.news.model.User;
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

    @Autowired
    private AppUserConfig appUserConfig;

    @Override
    public Comment requestToComment(UpsertCommentRequest request) {
        User user = request.getUserId() == null ? userService.findByName(appUserConfig.getUserName()) :
                userService.findById(request.getUserId());
        return Comment.builder()
                .comment(appUserConfig.getUserName().equals("admin") ? "(Upd by admin) " + request.getComment() : request.getComment())
                .user(user)
                .news(newsService.findById(request.getNewsId()))
                .build();
    }

    @Override
    public Comment requestToComment(Long id, UpsertCommentRequest request) {
        request.setUserId(commentService.findById(id).getUser().getId());
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
