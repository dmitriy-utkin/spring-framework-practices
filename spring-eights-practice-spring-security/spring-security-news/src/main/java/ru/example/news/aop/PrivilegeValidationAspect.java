package ru.example.news.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.example.news.exception.IllegalArguments;
import ru.example.news.exception.NotAllowedChangeRequestException;
import ru.example.news.model.*;
import ru.example.news.service.CommentService;
import ru.example.news.service.NewsService;
import ru.example.news.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
@ConditionalOnProperty(prefix = "app.validation", name = "enable", havingValue = "true")
public class PrivilegeValidationAspect {

    @Autowired
    private NewsService newsService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Before("@annotation(PrivilegeValidation)")
    public void validate(JoinPoint joinPoint) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();

        String requestedUri = httpServletRequest.getRequestURI();
        String method = joinPoint.getSignature().getName();

        ValidationType validationType = getValidationType(requestedUri, method);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (validationType == null) {
            throw new IllegalArguments("Unexpected request.");
        }
        if (!validate(validationType, userDetails.getUsername(), joinPoint.getArgs())) {
            throw new NotAllowedChangeRequestException("You are not authorized for this action.");
        }
    }

    private boolean validate(ValidationType validationType,
                          String username,
                          Object[] args) {

        User requester = userService.findByUsername(username);
        Set<RoleType> requesterRoles = requester.getRoles();

        Object objectToGetOwnerId = Arrays.stream(args).findFirst().orElse(null);

        if (objectToGetOwnerId == null) {
            return false;
        }

        switch (validationType) {
            case USER_UPDATE, USER_DELETE, USER_FIND_BY_ID, NEWS_DELETE, COMMENTS_DELETE -> {
                if (isNotAdminOrModerator(requesterRoles) && isNotOwnerOfEntity(validationType,
                        objectToGetOwnerId,
                        requester.getId())) {
                    return false;
                }
            }

            case TOPIC_SAVE, TOPIC_DELETE, TOPIC_UPDATE -> {
                if (isNotAdminOrModerator(requesterRoles)) {
                    return false;
                }
            }
                case NEWS_UPDATE, COMMENTS_UPDATE -> {
                if (isNotOwnerOfEntity(validationType, objectToGetOwnerId, requester.getId())) {
                    return false;
                }
            }
            default -> {
                return false;
            }
        }
        return true;
    }

    private ValidationType getValidationType(String requestedUri, String method) {

        if (requestedUri.contains("user") && method.contains("update")) {
            return ValidationType.USER_UPDATE;
        }

        if (requestedUri.contains("user") && method.contains("delete")) {
            return ValidationType.USER_DELETE;
        }

        if (requestedUri.contains("user") && method.contains("find")) {
            return ValidationType.USER_FIND_BY_ID;
        }

        if (requestedUri.contains("news") && method.contains("update")) {
            return ValidationType.NEWS_UPDATE;
        }

        if (requestedUri.contains("news") && method.contains("delete")) {
            return ValidationType.NEWS_DELETE;
        }

        if (requestedUri.contains("comment") && method.contains("update")) {
            return ValidationType.COMMENTS_UPDATE;
        }

        if (requestedUri.contains("comment") && method.contains("delete")) {
            return ValidationType.COMMENTS_DELETE;
        }

        if (requestedUri.contains("topic") && method.contains("save")) {
            return ValidationType.TOPIC_SAVE;
        }

        if (requestedUri.contains("topic") && method.contains("update")) {
            return ValidationType.TOPIC_UPDATE;
        }

        if (requestedUri.contains("topic") && method.contains("delete")) {
            return ValidationType.TOPIC_DELETE;
        }

        return null;
    }

    private boolean isNotAdminOrModerator(Set<RoleType> requesterRoles) {
        return !requesterRoles.contains(RoleType.ROLE_ADMIN) || !requesterRoles.contains(RoleType.ROLE_MODERATOR);
    }

    private boolean isNotOwnerOfEntity(ValidationType validationType,
                                       Object objectToGetOwnerId,
                                       Long requesterId){

        switch (validationType) {
            case USER_DELETE, USER_FIND_BY_ID -> {
                return !Objects.equals(objectToGetOwnerId, requesterId);
            }
            case USER_UPDATE -> {
                return !Objects.equals(getOwnerIdForUpdate(
                        objectToGetOwnerId, true, false, false), requesterId
                );
            }
            case NEWS_UPDATE -> {
                return !Objects.equals(
                        getOwnerIdForUpdate(objectToGetOwnerId, false, true, false), requesterId
                );
            }
            case COMMENTS_UPDATE -> {
                return !Objects.equals(
                        getOwnerIdForUpdate(objectToGetOwnerId, false, false, true), requesterId
                );
            }
            case NEWS_DELETE -> {
                return !Objects.equals(
                        getOwnerIdForDelete((Long) objectToGetOwnerId, true, false), requesterId
                );
            }
            case COMMENTS_DELETE -> {
                return !Objects.equals(
                        getOwnerIdForDelete((Long) objectToGetOwnerId, false, true), requesterId
                );
            }
            default -> {
                return true;
            }
        }
    }

    @SneakyThrows
    private static Long getOwnerIdForUpdate(Object object, boolean user, boolean news, boolean comments) {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (user && field.getName().equals(User.Fields.id)) {
                return (Long) field.get(object);
            }
            if ((news || comments) &&
                    (field.getName().equals(News.Fields.user) || field.getName().equals(Comment.Fields.user))) {
                User userObj = (User) field.get(object);
                return userObj.getId();
            }
        }
        return null;
    }

    private Long getOwnerIdForDelete(Long id, boolean news, boolean comments) throws RuntimeException {
        if (news) {
            return newsService.findById(id).getUser().getId();
        }
        if (comments) {
            return commentService.findById(id).getUser().getId();
        }
        return null;
    }

}
