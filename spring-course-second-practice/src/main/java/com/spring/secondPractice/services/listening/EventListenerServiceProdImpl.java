package com.spring.secondPractice.services.listening;

import com.spring.secondPractice.config.StringConfig;
import com.spring.secondPractice.events.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;


@Component
@Profile("prod")
@ConditionalOnProperty(name = "app.listening.enable", havingValue = "true")
@Slf4j
public class EventListenerServiceProdImpl implements EventListenerService {

    @PostConstruct
    public void postConstruct() {
        log.info("--> PostConstruct (prod): EventListenerServiceProdImpl was created");
    }

    @Autowired
    private StringConfig stringConfig;

    @Override
    @EventListener
    public void onLaunch(ApplicationReadyEvent event) {
        log.info(stringConfig.getListenerOnLaunchMessage());
    }

    @Override
    public void onAdding(AddingEvent event) {
        log.info(MessageFormat.format(stringConfig.getListenerOnAddingMessageFormat(), event.getId()));
    }

    @Override
    public void onRemoving(RemovingEvent event) {
        log.info(MessageFormat.format(stringConfig.getListenerOnRemovingMessageFormat(),
                event.isRemoved(), event.getId()));
    }

    @Override
    public void onUploading(UploadingEvent event) {
        log.info(MessageFormat.format(stringConfig.getListenerOnUploadingMessageFormat(), event.isUploaded()));
    }

    @Override
    public void onRemovingAll(RemovingAllEvent event) {
        log.info(MessageFormat.format(stringConfig.getListenerOnRemovingAllMessageFormat(), event.getListSize()));
    }

    @Override
    public void onPrinting(PrintingEvent event) {
        log.info(MessageFormat.format(stringConfig.getListenerOnPrintingMessageFormat(), event.getStudentListSize()));
    }
}
