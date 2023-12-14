package com.spring.secondPractice.services.listening;

import com.spring.secondPractice.events.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

public interface EventListenerService {

    void onLaunch(ApplicationReadyEvent event);

    @EventListener
    void onAdding(AddingEvent event);

    @EventListener
    void onRemoving(RemovingEvent event);

    @EventListener
    void onUploading(UploadingEvent event);

    @EventListener
    void onRemovingAll(RemovingAllEvent event);

    @EventListener
    void onPrinting(PrintingEvent event);
}
