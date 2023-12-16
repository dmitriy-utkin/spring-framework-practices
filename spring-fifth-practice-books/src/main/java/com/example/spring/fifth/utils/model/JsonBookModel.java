package com.example.spring.fifth.utils.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JsonBookModel {
    private final String name;
    private final String category;
}
