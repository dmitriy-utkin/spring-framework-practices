package com.contacts_practiceOne.services.upload;

import com.contacts_practiceOne.config.IOConfig;
import com.contacts_practiceOne.services.contacts.ContactService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Component
@Profile("init")
public class InitUploadServiceImpl implements UploadService {

    private final ContactService contactService;
    private final IOConfig ioConfig;

    public InitUploadServiceImpl(ContactService contactService, IOConfig ioConfig) {
        this.ioConfig = ioConfig;
        System.out.println("Running InitUploadServiceImpl");
        this.contactService = contactService;
        init();
    }

    @Override
    public void init() {
        String csvFile = ioConfig.getInput();
        String line = "";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                contactService.addContact(line.replace(",", "; "));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
