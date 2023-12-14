package com.spring.secondPractice.events;

import lombok.Getter;

import java.time.Instant;

@Getter
public class RemovingEvent {
    private final Instant time;
    private final boolean isRemoved;
    private final int id;

    public RemovingEvent(boolean isRemoved, int id) {
        this.id = id;
        this.isRemoved = isRemoved;
        this.time = Instant.now();
    }
}
