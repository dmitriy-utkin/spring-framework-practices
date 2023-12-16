package com.example.spring.fifth.web.model.book;

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
public class BookListResponse {

    @Builder.Default
    private List<BookResponse> books = new ArrayList<>();
}
