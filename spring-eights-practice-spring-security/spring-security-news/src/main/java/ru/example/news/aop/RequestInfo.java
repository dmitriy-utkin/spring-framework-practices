package ru.example.news.aop;

import ru.example.news.configuration.AppUserConfig;
import ru.example.news.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.aspectj.lang.JoinPoint;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Getter
public class RequestInfo {

    public RequestInfo(JoinPoint joinPoint, UserService userService, AppUserConfig appUserConfig) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletResponse httpServletResponse = ((ServletRequestAttributes) requestAttributes).getResponse();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();

        assert httpServletResponse != null;
        String userName = httpServletResponse.getHeader(appUserConfig.getUserHeader());
        Long userId = userService.findByName(userName).getId();
        this.userName = userName;
        this.userId = userId;
        this.args = joinPoint.getArgs();
        this.requestedUri = httpServletRequest.getRequestURI();
        this.method = joinPoint.getSignature().getName();
    }

    private final String method;
    private final String requestedUri;
    private final Object[] args;
    private final String userName;
    private final Long userId;

}
