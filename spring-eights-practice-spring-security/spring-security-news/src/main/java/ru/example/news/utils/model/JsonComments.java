package ru.example.news.utils.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JsonComments {

    private String comment;
}
