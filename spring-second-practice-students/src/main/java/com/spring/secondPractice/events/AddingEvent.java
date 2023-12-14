package com.spring.secondPractice.events;

import lombok.Getter;

import java.time.Instant;

@Getter
public class AddingEvent {

    private final Instant time;
    private final int id;

    public AddingEvent(int id) {
        this.id = id;
        this.time = Instant.now();
    }
}
