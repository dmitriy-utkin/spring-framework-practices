package com.spring.secondPractice.events;

import lombok.Getter;

import java.time.Instant;

@Getter
public class RemovingAllEvent {
    private final Instant time;
    private final int listSize;

    public RemovingAllEvent(int listSize) {
        this.listSize = listSize;
        this.time = Instant.now();
    }
}
