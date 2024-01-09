package ru.example.news.aop;

import ru.example.news.configuration.AppUserConfig;
import ru.example.news.exception.NotAllowedChangeRequestException;
import ru.example.news.service.CommentService;
import ru.example.news.service.NewsService;
import ru.example.news.service.UserService;
import ru.example.news.utils.AspectUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@ConditionalOnProperty(value = "app.validation", name = "enable", havingValue = "true")
public class PrivilegeValidationAspect {

    @Autowired
    private AppUserConfig appUserConfig;

    @Autowired
    private NewsService newsService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Before("@annotation(PrivilegeValidation)")
    public void validate(JoinPoint joinPoint) {
        RequestInfo requestInfo = new RequestInfo(joinPoint, userService, appUserConfig);
        log.info(prepareAspectReport(requestInfo));
        Long ownerId = AspectUtils.getOwnerId(requestInfo, newsService, commentService, userService);
        validate(requestInfo, ownerId);
    }

    private void validate(RequestInfo requestInfo, Long ownerId) {

        if (ownerId == null) {
            throw new NotAllowedChangeRequestException("Owner of entity not found for changing");
        }

        if (!requestInfo.getUserName().equals("admin") && (requestInfo.getRequestedUri().contains("topic"))) {
            throw new NotAllowedChangeRequestException("You cant "
                    + (requestInfo.getMethod().contains("delete") ? "delete" : "update")
                    + " it, only admin can do it");
        }

        if (!requestInfo.getUserName().equals("admin") && (requestInfo.getMethod().contains("save"))
                && (requestInfo.getRequestedUri().contains("user"))) {
            throw new NotAllowedChangeRequestException("New user can be saved by admin or automatically only");
        }

        if (!requestInfo.getUserName().equals("admin") && (requestInfo.getMethod().contains("delete"))
                && (requestInfo.getRequestedUri().contains("user"))) {
            throw new NotAllowedChangeRequestException("You cant delete accounts");
        }

        if (requestInfo.getUserName().equals("admin") && (requestInfo.getMethod().contains("delete"))
                && (requestInfo.getRequestedUri().contains("user"))
                && userService.findById(ownerId).getUsername().equals("admin")) {
            throw new NotAllowedChangeRequestException("You cant delete admin account");
        }

        if (!requestInfo.getUserName().equals("admin") && (!ownerId.equals(requestInfo.getUserId()))) {
            throw new NotAllowedChangeRequestException("You cant "
                    + (requestInfo.getMethod().contains("delete") ? "delete" : "update")
                    + " it, because you are not an owner");
        }
    }

    private String prepareAspectReport(RequestInfo requestInfo) {
        return  "Aspect: " + requestInfo.getMethod() + " for user \"" + requestInfo.getUserName() +  "\", ID = "
                + requestInfo.getUserId();
    }

}
