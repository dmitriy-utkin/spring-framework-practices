package ru.example.news.aop;

import ru.example.news.utils.AspectUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class IdLoggerAspect {

    @AfterReturning(pointcut = "@annotation(IdLogger)", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        Long id = AspectUtils.getEntityId(result);
        log.info("Executed method \"{}\", {} -> ID {}", joinPoint.getSignature().getName(), result.getClass(), id);
    }

}
