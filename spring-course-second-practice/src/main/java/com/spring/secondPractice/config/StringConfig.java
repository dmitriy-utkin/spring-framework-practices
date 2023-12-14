package com.spring.secondPractice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.strings")
public class StringConfig {

    private String messagePrefix;

    private String saveMessageFormat;
    private String successStudentRemovingFormat;
    private String unsuccessfulStudentRemovingFormat;
    private String removingAllStudentsFormat;
    private String emptyListFormat;
    private String wrongInputAgeFormat;
    private String alreadyExistedStudentFormat;
    private String uploadingMessageFormat;

    private String listenerOnLaunchMessage;
    private String listenerOnAddingMessageFormat;
    private String listenerOnRemovingMessageFormat;
    private String listenerOnRemovingAllMessageFormat;
    private String listenerOnPrintingMessageFormat;
    private String listenerOnUploadingMessageFormat;
}
