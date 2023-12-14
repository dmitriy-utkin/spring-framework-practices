package com.contacts_practiceOne.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource("classpath:application.properties")
public class LauncherConfig {

    @Value("${app.contacts.launcher.appName}")
    private String appName;
    @Value("${app.contacts.launcher.greetings}")
    private String greetings;
    @Value("${app.contacts.launcher.baseInstruction}")
    private String baseInstruction;

    @Value("${app.contacts.launcher.exitApp}")
    private String exitApp;
    @Value("${app.contacts.launcher.finalInputFormat}")
    private String finalInputFormat;
    @Value("${app.contacts.launcher.fullNameFormat}")
    private String fullNameFormat;
    @Value("${app.contacts.launcher.phoneNumberFormat}")
    private String phoneNumberFormat;
    @Value("${app.contacts.launcher.emailFormat}")
    private String emailFormat;
    @Value("${app.contacts.launcher.invalidRequestMainPage}")
    private String invalidRequestMainPage;

}
