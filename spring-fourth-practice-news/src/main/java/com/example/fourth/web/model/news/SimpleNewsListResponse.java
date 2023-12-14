package com.example.fourth.web.model.news;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleNewsListResponse {

    @Builder.Default
    List<SimpleNewsResponse> news = new ArrayList<>();
}
