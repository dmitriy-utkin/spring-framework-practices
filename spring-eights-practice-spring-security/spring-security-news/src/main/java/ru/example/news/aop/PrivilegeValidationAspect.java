package ru.example.news.aop;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.example.news.exception.IllegalArguments;
import ru.example.news.exception.NotAllowedChangeRequestException;
import ru.example.news.model.Comment;
import ru.example.news.model.News;
import ru.example.news.model.RoleType;
import ru.example.news.model.User;
import ru.example.news.service.CommentService;
import ru.example.news.service.NewsService;
import ru.example.news.service.UserService;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

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
    public void validateCredential(JoinPoint joinPoint) {

        ValidationType type = getValidationType(joinPoint);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!validate(type, userDetails.getUsername(), joinPoint.getArgs())) {
            throw new NotAllowedChangeRequestException("You are not authorized for this action.");
        }
    }

    @SneakyThrows
    private ValidationType getValidationType(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Class<?>[] parameters = methodSignature.getMethod().getParameterTypes();
        PrivilegeValidation validationAnnotation = joinPoint.getTarget().getClass()
                .getMethod(joinPoint.getSignature().getName(), parameters)
                .getAnnotation(PrivilegeValidation.class);
        if (validationAnnotation != null) {
            return validationAnnotation.type();
        }
        throw new IllegalArguments("Validation type is unexpected");
    }

    private boolean validate(ValidationType validationType, String username, Object[] args) {

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

    private boolean isNotAdminOrModerator(Set<RoleType> requesterRoles) {
        return !requesterRoles.stream().anyMatch(role -> role.equals(RoleType.ROLE_ADMIN) || role.equals(RoleType.ROLE_MODERATOR));
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
    private Long getOwnerIdForUpdate(Object object, boolean user, boolean news, boolean comments) {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (user && field.getName().equals(User.Fields.id)) {
                return (Long) field.get(object);
            }
            if (news && (field.getName().equals(News.Fields.id))) {
                Long newsId = (Long) field.get(object);
                return newsService.findById(newsId).getUser().getId();
            }
            if (comments && (field.getName().equals(Comment.Fields.id))) {
                Long commentId = (Long) field.get(object);
                return commentService.findById(commentId).getUser().getId();
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
