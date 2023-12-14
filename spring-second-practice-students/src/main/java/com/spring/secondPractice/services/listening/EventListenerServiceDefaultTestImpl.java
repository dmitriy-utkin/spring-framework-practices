package com.spring.secondPractice.services.listening;

import com.spring.secondPractice.events.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile(value = {"default", "test"})
@ConditionalOnProperty(name = "app.listening.enable", havingValue = "true")
@Slf4j
public class EventListenerServiceDefaultTestImpl implements EventListenerService {

    @PostConstruct
    public void postConstruct() {
        log.info("--> PostConstruct (default/test): EventListenerServiceDefaultImpl was created");
    }

    @Override
    public void onLaunch(ApplicationReadyEvent event) {
        log.info("Under construction: {onLaunch}");
    }

    @Override
    public void onAdding(AddingEvent event) {
        log.info("Under construction: {onAdding}");
    }

    @Override
    public void onRemoving(RemovingEvent event) {
        log.info("Under construction: {onRemoving}");
    }

    @Override
    public void onUploading(UploadingEvent event) {
        log.info("Under construction: {onUploading}");
    }

    @Override
    public void onRemovingAll(RemovingAllEvent event) {
        log.info("Under construction: {onRemovingAll}");
    }

    @Override
    public void onPrinting(PrintingEvent event) {
        log.info("Under construction: {onPrinting}");
    }


}
