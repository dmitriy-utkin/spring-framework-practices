package com.example.spring.third.service.tools;

import com.example.spring.third.config.InputConfig;
import com.example.spring.third.service.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(value = "app.input.enable", havingValue = "true")
public class AppListener {

    private final ContactService contactService;
    private final InputConfig inputConfig;

    @EventListener(ApplicationStartedEvent.class)
    public void createData() {
        contactService.createGenericContacts(inputConfig.getNumberOfGenericContacts());
    }
}
