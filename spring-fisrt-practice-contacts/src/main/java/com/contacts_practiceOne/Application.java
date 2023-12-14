package com.contacts_practiceOne;

import com.contacts_practiceOne.config.AppConfig;
import com.contacts_practiceOne.services.launcher.LauncherService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@EnableConfigurationProperties
public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        context.getBean(LauncherService.class).launch();
    }
}
