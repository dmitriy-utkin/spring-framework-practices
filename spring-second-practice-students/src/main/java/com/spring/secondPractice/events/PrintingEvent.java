package com.spring.secondPractice.events;

import lombok.Getter;

import java.time.Instant;

@Getter
public class PrintingEvent {
    private final Instant time;
    private final int studentListSize;

    public PrintingEvent(int studentListSize) {
        this.studentListSize = studentListSize;
        this.time = Instant.now();
    }
}
