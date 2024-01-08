package com.example.fourth.web.interceptor;

import com.example.fourth.configuration.AppUserConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class UserRequestInterceptor implements HandlerInterceptor {

    @Autowired
    private AppUserConfig appUserConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        response.addHeader(appUserConfig.getUserHeader(), appUserConfig.getUserName());
        return true;
    }
}
