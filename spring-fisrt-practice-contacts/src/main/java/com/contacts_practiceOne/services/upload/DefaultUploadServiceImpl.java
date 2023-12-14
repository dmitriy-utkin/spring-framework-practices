package com.contacts_practiceOne.services.upload;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("default")
@Component
public class DefaultUploadServiceImpl implements UploadService {

    public DefaultUploadServiceImpl() {
        System.out.println("Running DefaultUploadServiceImpl");
    }

    @Override
    public void init() {
        System.out.println("Method \"init()\" in the DefaultUploadService is under construction.");
    }
}
