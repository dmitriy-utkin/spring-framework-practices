package com.spring.secondPractice.events;

import lombok.Getter;

import java.time.Instant;

@Getter
public class UploadingEvent {
    private final Instant time;
    private final boolean isUploaded;

    public UploadingEvent(boolean isUploaded) {
        this.isUploaded = isUploaded;
        this.time = Instant.now();
    }
}
